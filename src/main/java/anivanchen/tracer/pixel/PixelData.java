package main.java.anivanchen.tracer.pixel;

public class PixelData {
    private Color color;
    private float depth;
    private float emission;

    public PixelData(Color color, float depth, float emission) {
        this.color = color;
        this.depth = depth;
        this.emission = emission;
    }

    public Color getColor() {
        return color;
    }

    public float getDepth() {
        return depth;
    }

    public float getEmission() {
        return emission;
    }

    public void add(PixelData other) {
        this.color.addSelf(other.getColor());
        this.depth = (this.depth + other.getDepth()) / 2;
        this.emission += other.getEmission();
    }

    public void multiply(float brightness) {
        this.color = this.color.multiply(brightness);
    }
}
