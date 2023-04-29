package main.java.anivanchen.tracer.scene;

import main.java.anivanchen.tracer.utils.Vector3d;

public class Light {
    private Vector3d position;
    private float intensity;

    public Light(Vector3d position, float intensity) {
        this.position = position;
        this.intensity = intensity;
    }

    public Vector3d getPosition() {
        return position;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
