package main.java.anivanchen.tracer.shapes;

import main.java.anivanchen.tracer.utils.Ray;
import main.java.anivanchen.tracer.utils.Vector3d;

public class Line {
    public Vector3d pointA, pointB;

    public Line(Vector3d pointA, Vector3d pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public Ray asRay() {
        return new Ray(pointA, pointB.subtract(pointA).normalize());
    }
}
