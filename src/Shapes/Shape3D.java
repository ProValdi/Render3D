package Shapes;

import java.awt.*;
import java.util.List;

public class Shape3D {

    protected Color color = Color.ORANGE;
    protected Color skeletonColor = Color.GREEN;
    protected double size = 100;
    protected Vertex position = new Vertex(0, 0 , 0);

    protected List<Triangle> tris = null;

    public Shape3D () {

    }

    public Shape3D(List<Triangle> tris) {
        this.tris = tris;
    }

    public Color getColor() {
        return color;
    }

    public Color getSkeletonColor() {
        return skeletonColor;
    }

    public double getSize() {
        return size;
    }

    public Shape3D resize(double size) {
        this.size += size;
        return this;
    }

    public List<Triangle> getTris() {
        return tris;
    }

    public Vertex getPosition() {
        return position;
    }

    public Shape3D setColor(Color color) {
        this.color = color;
        return this;
    }

    public Shape3D setSkeletonColor(Color color) {
        this.skeletonColor = color;
        return this;
    }

    public Shape3D setSize(double size) {
        this.size = size;
        return this;
    }

    public Shape3D setPosition(Vertex position) {
        this.position = position;
        return this;
    }
}
