package main.java.anivanchen.tracer.utils;

import main.java.anivanchen.tracer.shapes.Line;

public class Ray {
    private Vector3d origin, direction;

    public Ray(Vector3d origin, Vector3d direction) {
        this.origin = origin;
        if (direction.length() != 1) {
            this.direction = direction.normalize();
        }
        this.direction = direction;
    }

    public Line asLine(float length) {
        return new Line(origin, origin.add(direction.multiply(length)));
    }

    public Vector3d getOrigin() {
        return origin;
    }

    public Vector3d getDirection() {
        return direction;
    }
}
