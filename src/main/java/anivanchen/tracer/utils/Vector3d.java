package main.java.anivanchen.tracer.utils;

public class Vector3d {
    public float x, y, z;

    public Vector3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float newX) {
        this.x = newX;
    }

    public void setY(float newY) {
        this.y = newY;
    }

    public void setZ(float newZ) {
        this.z = newZ;
    }

    public Vector3d add(Vector3d other) {
        return new Vector3d(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3d subtract(Vector3d other) {
        return new Vector3d(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3d multiply(float scalar) {
        return new Vector3d(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public Vector3d multiply(Vector3d other) {
        return new Vector3d(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public Vector3d divide(float scalar) {
        return new Vector3d(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    public Vector3d divide(Vector3d other) {
        return new Vector3d(this.x / other.x, this.y / other.y, this.z / other.z);
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3d normalize() {
        float length = this.length();
        return new Vector3d(this.x / length, this.y / length, this.z / length);
    }

    public Vector3d translate(Vector3d translation) {
        this.x += translation.x;
        this.y += translation.y;
        this.z += translation.z;
        return this;
    }

    public static float distance(Vector3d a, Vector3d b) {
        return (float) Math
                .sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
    }

    public static float dot(Vector3d a, Vector3d b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public float[] toArray() {
        return new float[] { x, y, z };
    }

    public Vector3d rotateYP(float yaw, float pitch) {
        // Convert to radians
        double yawRads = Math.toRadians(yaw);
        double pitchRads = Math.toRadians(pitch);

        // Step one: Rotate around X axis (pitch)
        float ty = (float) (y*Math.cos(pitchRads) - z*Math.sin(pitchRads));
        float tz = (float) (y*Math.sin(pitchRads) + z*Math.cos(pitchRads));

        // Step two: Rotate around the Y axis (yaw)
        float tx = (float) (x*Math.cos(yawRads) + tz*Math.sin(yawRads));
        tz = (float) (-x*Math.sin(yawRads) + tz*Math.cos(yawRads));

        return new Vector3d(tx, ty, tz);
    }
}
