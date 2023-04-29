package main.java.anivanchen.tracer.scene;

import main.java.anivanchen.tracer.utils.Vector3d;

public class Camera {
    private Vector3d position;

    private float yaw, pitch, fov;

    public Camera() {
        this.position = new Vector3d(0, 0, 0);
        this.yaw = 0;
        this.pitch = 0;
        this.fov = 60;
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getFov() {
        return fov;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public void translate(Vector3d translation) {
        this.position = this.position.translate(translation);
    }
}
