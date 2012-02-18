package net.mindengine.oculus.frontend.domain.trm;

public class Dimension {

    private int width;
    private int height;
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
