package main.java.anivanchen.tracer.scene;

import java.awt.Graphics;

import main.java.anivanchen.tracer.pixel.Color;
import main.java.anivanchen.tracer.pixel.PixelBuffer;
import main.java.anivanchen.tracer.pixel.PixelData;
import main.java.anivanchen.tracer.shapes.Solid;
import main.java.anivanchen.tracer.utils.Ray;
import main.java.anivanchen.tracer.utils.RayHit;
import main.java.anivanchen.tracer.utils.Vector3d;

public class Renderer {

    private static final float GLOBAL_ILLUMINATION = 0.5f;
    private static final float SKY_EMISSION = 0.5f;
    private static final int MAX_REFLECTION_BOUNCES = 10;

    public static PixelBuffer renderScene(Scene scene, int width, int height) {
        PixelBuffer buffer = new PixelBuffer(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float[] screenUV = getNormalizedScreenCoordinates(x, y, width, height);
                buffer.setPixel(x, y, computePixel(scene, screenUV[0], screenUV[1]));    
            }
        }
        return buffer;
    }
    
    public static void renderScene(Scene scene, Graphics g, int width, int height, float resolution) {
        int blockSize = (int) (1 / resolution);

        for (int x = 0; x < width; x += blockSize) {
            for (int y = 0; y < height; y += blockSize) {
                float[] uv = getNormalizedScreenCoordinates(x, y, width, height);
                PixelData pixel = computePixel(scene, uv[0], uv[1]);
                g.setColor(pixel.getColor().toAWTColor());
                g.fillRect(x, y, blockSize, blockSize);
            }
        }
    }
    
    public static float[] getNormalizedScreenCoordinates(int x, int y, int width, int height) {
        float u = 0;
        float v = 0;
        if (width > height) {
            u = (float)(x - width / 2 + height / 2) / height * 2 - 1;
            v =  -((float) y / height * 2 - 1);
        } else {
            u = (float) x / width * 2 - 1;
            v =  -((float) (y - height / 2 + width / 2) / width * 2 - 1);
        }
        return new float[] { u, v };
    }

    public static PixelData computePixel(Scene scene, float u, float v) {
        Vector3d eyePose = new Vector3d(0, 0, (float) (-1 / Math.tan(Math.toRadians(scene.getCamera().getFov() / 2))));
        Camera cam = scene.getCamera();

        Vector3d rayDirection = new Vector3d(u, v, 0).subtract(eyePose).normalize().rotateYP(cam.getYaw(),
                cam.getPitch());
        RayHit hit = scene.raycast(new Ray(eyePose.add(cam.getPosition()), rayDirection));
        if (hit != null) {
            return computePixelAtHit(scene, hit, MAX_REFLECTION_BOUNCES);
        } else {
            return new PixelData(Color.BLACK, Float.POSITIVE_INFINITY, 0);
        }
    }

    private static PixelData computePixelAtHit(Scene scene, RayHit hit, int maxBounces) {
        Vector3d hitPose = hit.getHitPose();
        Vector3d rayDirection = hit.getRay().getDirection();
        Solid hitSolid = hit.getHitSolid();
        Color hitColor = hitSolid.getColor();
        float brightness = getDiffuseBrightness(scene, hit);
        float specularBrightness = getSpecularBrightness(scene, hit);
        float reflectivity = hitSolid.getReflectivity();
        float emission = hitSolid.getEmission();
        PixelData reflection;
        Vector3d reflectionVec = rayDirection
                .subtract(hit.getNormal().multiply(2 * Vector3d.dot(rayDirection, hit.getNormal())));
        Vector3d reflectionRayOrigin = hitPose.add(reflectionVec.multiply(0.001f));
        RayHit reflectionHit = maxBounces > 0 ? scene.raycast(new Ray(reflectionRayOrigin, reflectionVec)) : null;

        if (reflectionHit != null) {
            reflection = computePixelAtHit(scene, reflectionHit, maxBounces - 1);
        } else {
            reflection = new PixelData(Color.BLACK, Float.POSITIVE_INFINITY, Color.BLACK.getLuminance() * SKY_EMISSION);
        }

        Color pixelColor = Color.lerp(hitColor, reflection.getColor(), reflectivity)
                .multiply(brightness)
                .add(specularBrightness)
                .add(hitColor.multiply(emission))
                .add(reflection.getColor().multiply(reflection.getEmission() * reflectivity));
        
        return new PixelData(pixelColor, Vector3d.distance(scene.getCamera().getPosition(), hitPose),
                Math.min(1, emission + reflection.getEmission() * reflectivity + specularBrightness));
    }

    private static float getDiffuseBrightness(Scene scene, RayHit hit) {
        Light sceneLight = scene.getLight();

        RayHit blocker = scene
                .raycast(new Ray(sceneLight.getPosition(), hit.getHitPose().subtract(sceneLight.getPosition())));
        if (blocker != null && blocker.getHitSolid() != hit.getHitSolid()) {
            return GLOBAL_ILLUMINATION;
        }
        return Math.max(GLOBAL_ILLUMINATION,
                Math.min(1, Vector3d.dot(hit.getNormal(), sceneLight.getPosition().subtract(hit.getHitPose()))));
    }

    private static float getSpecularBrightness(Scene scene, RayHit hit) {
        Vector3d hitPose = hit.getHitPose();
        Vector3d camDirection = scene.getCamera().getPosition().subtract(hitPose).normalize();
        Vector3d lightDirection = hitPose.subtract(scene.getLight().getPosition().normalize());
        Vector3d lightReflection = lightDirection
                .subtract(hit.getNormal().multiply(2 * Vector3d.dot(lightDirection, hit.getNormal())));
        
        float specularFactor = Math.max(0, Math.min(1, Vector3d.dot(lightReflection, camDirection)));
        return (float) Math.pow(specularFactor, 2) * hit.getHitSolid().getReflectivity();
    }
}
