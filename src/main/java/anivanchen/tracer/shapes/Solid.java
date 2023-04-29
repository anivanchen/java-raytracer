package main.java.anivanchen.tracer.shapes;

import main.java.anivanchen.tracer.pixel.Color;
import main.java.anivanchen.tracer.utils.Ray;
import main.java.anivanchen.tracer.utils.Vector3d;

public abstract class Solid {
    protected Vector3d position;
    protected Color color;
    protected float reflectivity;
    protected float emission;

    public Solid(Vector3d position, Color color, float reflectivity, float emission) {
        this.position = position;
        this.color = color;
        this.reflectivity = reflectivity;
        this.emission = emission;
    }

    public abstract Vector3d calculateIntersection(Ray ray);
    public abstract Vector3d getNormalAt(Vector3d point);

    public Vector3d getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public Color getTextureColor(Vector3d point) {
        return getColor();
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getEmission() {
        return emission;
    }
}
