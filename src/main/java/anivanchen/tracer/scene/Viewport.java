package main.java.anivanchen.tracer.scene;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.java.anivanchen.tracer.shapes.*;
import main.java.anivanchen.tracer.utils.*;
import main.java.anivanchen.tracer.pixel.Color;

public class Viewport extends JPanel {
    private JFrame frame;
    private Font font;
    private Scene scene;
    private Robot robot;

    private BufferedImage buffer;

    private Camera camera;
    private Vector3d camPose;
    private float movementSpeed;
    private Vector3d cameraMotion;

    private long dt;
    private float camYaw;
    private float camPitch;
    private float resolution;

    private boolean captureCursor;
    private Cursor blankCursor;
    private float mouseSensitivity;

    public Viewport(JFrame frame) {
        setFocusable(true);
        this.frame = frame;
        font = new Font("Consolas", Font.PLAIN, getWidth() / 100);
        this.scene = new Scene();
        
        this.camera = scene.getCamera();
        this.camPose = camera.getPosition();
        this.movementSpeed = 1f;
        this.cameraMotion = new Vector3d(0, 0, 0);
        
        this.dt = 1;
        this.camYaw = camera.getYaw();
        this.camPitch = camera.getPitch();
        this.resolution = 0.125f;

        this.captureCursor = false;
        this.mouseSensitivity = 0.1f;

        try {
            this.robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        this.blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

        scene.addSolid(new Sphere(new Vector3d(-0.5f, 0.5f, 1.5f), 0.4F, Color.RED, 0.5F, 0F));
        scene.addSolid(new Sphere(new Vector3d(0f, 0f, 2f), 0.4F, Color.GREEN, 0.5F, 0F));
        scene.addSolid(new Sphere(new Vector3d(0.5f, -0.5f, 2.5f), 0.4F, Color.BLUE, 0.5F, 0F));
    
        scene.addSolid(new Box(new Vector3d(1.5f, 0f, 2f), new Vector3d(0.5f, 3.0f, 3f), Color.RED, 0.0F, 0F));
        scene.addSolid(new Box(new Vector3d(-1.5f, 0f, 2f), new Vector3d(0.5f, 3.0f, 3f), Color.BLUE, 0.5F, 0F));
        scene.addSolid(new Box(new Vector3d(0f, 1.5f, 2f), new Vector3d(3f, 0.5f, 3f), Color.GREEN, 0.0F, 0F));
        scene.addSolid(new Box(new Vector3d(0f, 0f, 3.5f), new Vector3d(3f,3f, 0.5f), Color.BLUE, 0.0F, 0F));
        scene.addSolid(new Box(new Vector3d(0f, -1.5f, 2f), new Vector3d(3f,0.5f, 3f), Color.RED, 0.0F, 0F));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    cameraMotion.setX(0.2F);
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    cameraMotion.setX(-0.2F);
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    cameraMotion.setZ(0.2F);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    cameraMotion.setZ(-0.2F);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    cameraMotion.setY(0.2F);
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    cameraMotion.setY(-0.2F);
                } else if (e.getKeyCode() == KeyEvent.VK_1) {
                    resolution = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    resolution = 0.5F;
                } else if (e.getKeyCode() == KeyEvent.VK_3) {
                    resolution = 0.25F;
                } else if (e.getKeyCode() == KeyEvent.VK_4) {
                    resolution = 0.125F;
                } else if (e.getKeyCode() == KeyEvent.VK_F12) {
                    try {
                        renderToImage(3840, 2160);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                // } else if (e.getKeyCode() == KeyEvent.VK_H) {
                //     hideHUD = !hideHUD;
                // } else if (e.getKeyCode() == KeyEvent.VK_F1) {
                //     settingsDialog.setVisible(true);
                //     settingsDialog.setLocationRelativeTo(frame);
                //     setCaptureCursor(false);
                // }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    cameraMotion.setX(0);
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    cameraMotion.setX(0);
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    cameraMotion.setZ(0);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    cameraMotion.setZ(0);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    cameraMotion.setY(0);
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    cameraMotion.setY(0);
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setCaptureCursor(false);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (captureCursor) {
                    int centerX = frame.getX() + frame.getWidth() / 2;
                    int centerY = frame.getY() + frame.getHeight() / 2;

                    int mouseXOffset = e.getXOnScreen() - centerX;
                    int mouseYOffset = e.getYOnScreen() - centerY;
                    camYaw = (camYaw + mouseXOffset * mouseSensitivity);
                    camPitch = (Math.min(90, Math.max(-90, camPitch + mouseYOffset * mouseSensitivity)));
                    robot.mouseMove(centerX, centerY);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!captureCursor) setCaptureCursor(true);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                font = new Font("Consolas", Font.PLAIN, getWidth()/50);
            }
        });
    } 

    public void runMainLoop() {
        while (true) {
            long startTime = System.currentTimeMillis();

            if (cameraMotion.length() != 0) {
                camPose.translate(cameraMotion.rotateYP(camera.getYaw(), 0).multiply(dt / 50F * movementSpeed));
            }

            if (captureCursor) {
                camera.setYaw(camYaw);
                camera.setPitch(camPitch);
                camera.setPosition(camPose);
            }

            BufferedImage tempBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Renderer.renderScene(scene, tempBuffer.getGraphics(), getWidth(), getHeight(), resolution);
            buffer = tempBuffer;

            repaint();

            dt = System.currentTimeMillis() - startTime;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        if (buffer != null)
            g.drawImage(buffer, 0, 0, this);
        
        // HUD
        g.setColor(java.awt.Color.WHITE);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        String fps = String.valueOf(1000F / dt);
        if (fps.length() > 4)
            fps = fps.substring(0, 4);
        String fpsString = fps + " FPS";
        String resolutionString = (resolution * 100) + "%";

        Rectangle2D str1Bounds = fm.getStringBounds(fpsString, g);
        Rectangle2D str2Bounds = fm.getStringBounds(resolutionString, g);
        g.drawString(fpsString, 10, (int) (str1Bounds.getHeight()));
        g.drawString(resolutionString, 10, (int) (str1Bounds.getHeight() + str2Bounds.getHeight()));
    }

    private void setCaptureCursor(boolean captureCursor) {
        this.captureCursor = captureCursor;

        if (captureCursor) {
            // if (settingsDialog.isVisible())
            //     settingsDialog.setVisible(false);

            setCursor(blankCursor);

            int centerX = frame.getX() + frame.getWidth() / 2;
            int centerY = frame.getY() + frame.getHeight() / 2;
            robot.mouseMove(centerX, centerY);
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    public void renderToImage(int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.out.println("Rendering to image...");
        Renderer.renderScene(scene, image.getGraphics(), width, height, 1F);

        File imgFile = new File("output.png");
        ImageIO.write(image, "PNG", new FileOutputStream(imgFile));
        System.out.println("Image saved.");

        Desktop.getDesktop().open(imgFile);
    }
    

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setLightAngle(float hAngle, float vAngle, float distance) {
        float x = (float) (distance * Math.cos(hAngle) * Math.sin(vAngle));
        float y = (float) (distance * Math.cos(vAngle));
        float z = (float) (distance * Math.sin(hAngle) * Math.sin(vAngle));

        scene.getLight().setPosition(new Vector3d(x, y, z));
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
