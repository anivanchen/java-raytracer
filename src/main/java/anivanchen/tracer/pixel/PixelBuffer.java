package main.java.anivanchen.tracer.pixel;

public class PixelBuffer {
    private PixelData[][] buffer;
    private int width, height;

    public PixelBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        buffer = new PixelData[width][height];
    }

    public void setPixel(int x, int y, PixelData pixel) {
        buffer[x][y] = pixel;
    }

    public PixelData getPixel(int x, int y) {
        return buffer[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PixelBuffer add(PixelBuffer other) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (buffer[i][j] != null && other.getPixel(i, j) != null) {
                    buffer[i][j].add(other.getPixel(i, j));
                }
            }
        }
        return this;
    }
    
    public PixelBuffer multiply(float brightness) {
        for (PixelData[] row : buffer) {
            for (PixelData pixel : row) {
                if (pixel != null) {
                    pixel.multiply(brightness);
                }
            }
        }
        return this;
    }

    public PixelBuffer resize(int newWidth, int newHeight) {
        PixelBuffer copy = new PixelBuffer(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                copy.setPixel(i, j,
                        getPixel(i * this.getWidth() / copy.getWidth(), j * this.getHeight() / copy.getHeight()));
            }
        }
        return copy;
    }
}
