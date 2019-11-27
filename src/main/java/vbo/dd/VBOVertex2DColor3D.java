package vbo.dd;

import lombok.Data;

@Data
public class VBOVertex2DColor3D {
    private double x;
    private double y;
    private double red;
    private double green;
    private double blue;
    public static final double DEFAULT_RED = 1.0;
    public static final double DEFAULT_GREEN = 1.0;
    public static final double DEFAULT_BLUE = 1.0;

    public VBOVertex2DColor3D(double x, double y, double red, double green, double blue) {
        this.x = x;
        this.y = y;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public VBOVertex2DColor3D(double x, double y) {
        this(x, y, DEFAULT_RED, DEFAULT_GREEN, DEFAULT_BLUE);
    }

    public VBOVertex2DColor3D() {
        this(0.0, 0.0);
    }

    public double[] toVBOArray() {
        return new double[] {x, y, red, green, blue};
    }
}
