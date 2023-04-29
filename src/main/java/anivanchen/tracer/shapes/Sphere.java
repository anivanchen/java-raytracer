package main.java.anivanchen.tracer.shapes;

import main.java.anivanchen.tracer.pixel.Color;
import main.java.anivanchen.tracer.utils.Ray;
import main.java.anivanchen.tracer.utils.Vector3d;

public class Sphere extends Solid {
    private float radius;

    public Sphere(Vector3d position, float radius, Color color, float reflectivity, float emission) {
        super(position, color, reflectivity, emission);
        this.radius = radius;
    }

    @Override
    public Vector3d calculateIntersection(Ray ray) {
        float t = Vector3d.dot(position.subtract(ray.getOrigin()), ray.getDirection());
        Vector3d p = ray.getOrigin().add(ray.getDirection().multiply(t));

        float y = position.subtract(p).length();
        if (y < radius) {
            float x = (float) Math.sqrt(radius * radius - y * y);
            float d = t - x;
            if (d > 0) {
                return ray.getOrigin().add(ray.getDirection().multiply(d));
            }
            return null;
        }
        return null;
    }

    @Override
    public Vector3d getNormalAt(Vector3d point) {
        return point.subtract(position).normalize();
    }
}
