package Shapes;

public class Vertex {
    private double x;
    private double y;
    private double z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vertex addX(double x) {
        this.x += x;
        return this;
    }

    public Vertex addY(double y) {
        this.y += y;
        return this;
    }

    public Vertex addZ(double z) {
        this.z += z;
        return this;
    }

    public Vertex add (double v) {
        addX(v);
        addY(v);
        addZ(v);
        return this;
    }

    public Vertex resizeX (double v) {
        x += x / Math.abs(x) * v;
        return this;
    }

    public Vertex resizeY (double v) {
        y += y / Math.abs(y) * v;
        return this;
    }

    public Vertex resizeZ (double v) {
        z += z / Math.abs(z) * v;
        return this;
    }

    public Vertex resize (double v) {
        resizeX(v);
        resizeY(v);
        resizeZ(v);
        return this;
    }

    public Vertex multiply(double v) {
        x *= v;
        y *= v;
        z *= v;
        return this;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    public Vertex normalize() {
        double l = Math.sqrt(x * x + y * y + z * z);
        x /= l;
        y /= l;
        z /= l;
        return this;
    }

    public static Vertex getVectorMult(Vertex a, Vertex b) {
        return new Vertex(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
    }

    @Override
    public String toString() {
        return "x=" + x + " " + "y=" + y + " " + "z=" + z + " ";
    }

}
