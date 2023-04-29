package main.java.anivanchen.tracer.shapes;

import main.java.anivanchen.tracer.pixel.Color;
import main.java.anivanchen.tracer.utils.Ray;
import main.java.anivanchen.tracer.utils.Vector3d;

public class Box extends Solid {
    private Vector3d min, max;

    public Box(Vector3d position, Vector3d scale, Color color, float reflectivity, float emission) {
        super(position, color, reflectivity, emission);
        this.max = position.add(scale.divide(2));
        this.min = position.subtract(scale.divide(2));
    }

    @Override
    public Vector3d calculateIntersection(Ray ray) {
        float t1 = Float.NEGATIVE_INFINITY;
        float t2 = Float.NEGATIVE_INFINITY;
        float tnear = Float.NEGATIVE_INFINITY;
        float tfar = Float.POSITIVE_INFINITY;
        float temp;

        boolean intersectFlag = true;
        float[] rayDirection = ray.getDirection().toArray();
        float[] rayOrigin = ray.getOrigin().toArray();

        float[] b1 = min.toArray();
        float[] b2 = max.toArray();

        for (int i = 0; i < 3; i++) {
            if (rayDirection[i] == 0) {
                if (rayOrigin[i] < b1[i] || rayOrigin[i] > b2[i]) {
                    intersectFlag = false;
                    break;
                }
            } else {
                t1 = (b1[i] - rayOrigin[i]) / rayDirection[i];
                t2 = (b2[i] - rayOrigin[i]) / rayDirection[i];

                if (t1 > t2) {
                    temp = t1;
                    t1 = t2;
                    t2 = temp;
                }

                if (t1 > tnear) {
                    tnear = t1;
                }

                if (t2 < tfar) {
                    tfar = t2;
                }

                if (tnear > tfar || tfar < 0) {
                    intersectFlag = false;
                    break;
                }
            }
        }
        if (intersectFlag)
            return ray.getOrigin().add(ray.getDirection().multiply(tnear));
        return null;
    }

    public boolean contains(Vector3d point) {
        return point.getX() >= min.getX() && point.getY() >= min.getY() && point.getZ() >= min.getZ()
                && point.getX() <= max.getX() && point.getY() <= max.getY() && point.getZ() <= max.getZ();
    }
    
    @Override
    public Vector3d getNormalAt(Vector3d point) {
        float[] direction = point.subtract(position).toArray();
        float biggestVal = Float.NaN;

        for (int i = 0; i < 3; i++) {
            if (Float.isNaN(biggestVal) || Math.abs(direction[i]) > Math.abs(biggestVal)) {
                biggestVal = direction[i];
            }
        }

        if (biggestVal == 0) {
            return new Vector3d(0, 0, 0);
        } else {
            for (int i = 0; i < 3; i++) {
                float[] normal = new float[] { 0, 0, 0 };
                normal[i] = direction[i] > 0 ? 1 : -1;
                return new Vector3d(normal[0], normal[1], normal[2]);
            }
        }

        return new Vector3d(0, 0, 0);
    } 
}
