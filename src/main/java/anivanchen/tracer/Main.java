package main.java.anivanchen.tracer;

import javax.swing.JFrame;

import main.java.anivanchen.tracer.scene.Viewport;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Ray Tracer");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        Viewport viewport = new Viewport(frame);
        frame.add(viewport);

        frame.setVisible(true);

        viewport.runMainLoop();
    }
}