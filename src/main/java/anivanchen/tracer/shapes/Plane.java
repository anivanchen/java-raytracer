package main.java.anivanchen.tracer.shapes;

import main.java.anivanchen.tracer.pixel.Color;
import main.java.anivanchen.tracer.utils.Ray;
import main.java.anivanchen.tracer.utils.Vector3d;

public class Plane extends Solid {
    private boolean checkerboard;

    public Plane(float height, Color color, boolean checkerboard, float reflectivity, float emission) {
        super(new Vector3d(0, height, 0), color, reflectivity, emission);
        this.checkerboard = checkerboard;
    }

    @Override
    public Vector3d calculateIntersection(Ray ray) {
        float t = (position.y - ray.getOrigin().y) / ray.getDirection().y;
        if (t > 0 && Float.isFinite(t)) {
            return ray.getOrigin().add(ray.getDirection().multiply(t));
        }
        return null;
    }

    @Override
    public Vector3d getNormalAt(Vector3d point) {
        return new Vector3d(0, 1, 0);
    }

    @Override
    public Color getTextureColor(Vector3d point) {
        if (checkerboard) {
            if (((point.getX() > 0) & (point.getZ() > 0)) || ((point.getX() < 0) & (point.getZ() < 0))) {
                if ((int)point.getX() % 2 == 0 ^ (int)point.getZ() % 2 != 0) {
                    return Color.GRAY;
                } else {
                    return Color.DARK_GRAY;
                }
            } else {
                if ((int)point.getX() % 2 == 0 ^ (int)point.getZ() % 2 != 0) {
                    return Color.DARK_GRAY;
                } else {
                    return Color.GRAY;
                }
            }
        }
        return getColor();
    }
}
