package Shapes;

import java.awt.*;
import java.util.ArrayList;

public class Cube extends Shape3D {

    public Cube(Vertex position, double size, Color color) {
        this.position = position;
        this.size = size;
        this.color = color;
    }

    public Cube() {
        this(new Vertex(0, 0, 0), 100, Color.ORANGE);

        tris = new ArrayList<>();
        //A
        tris.add(new Triangle(new Vertex(-size, size, size),
                new Vertex(size, size, size),
                new Vertex(-size, size, -size),
                color));
        //B
        tris.add(new Triangle(new Vertex(size, size, size),
                new Vertex(size, size, -size),
                new Vertex(-size, size, -size),
                color));
        //C
        tris.add(new Triangle(new Vertex(size, -size, size),
                new Vertex(size, size, -size),
                new Vertex(size, size, size),
                color));
        //D
        tris.add(new Triangle(new Vertex(size, -size, size),
                new Vertex(size, -size, -size),
                new Vertex(size, size, -size),
                color));
        //E
        tris.add(new Triangle(new Vertex(-size, -size, size),
                new Vertex(size, -size, size),
                new Vertex(-size, size, size),
                color));

        //F
        tris.add(new Triangle(new Vertex(size, -size, size),
                new Vertex(size, size, size),
                new Vertex(-size, size, size),
                color));
        //G
        tris.add(new Triangle(new Vertex(-size, -size, size),
                new Vertex(-size, size, size),
                new Vertex(-size, -size, -size),
                color));
        //H
        tris.add(new Triangle(new Vertex(-size, size, size),
                new Vertex(-size, size, -size),
                new Vertex(-size, -size, -size),
                color));
        //I
        tris.add(new Triangle(new Vertex(-size, size, -size),
                new Vertex(size, size, -size),
                new Vertex(-size, -size, -size),
                color));
        //J
        tris.add(new Triangle(new Vertex(-size, -size, -size),
                new Vertex(size, size, -size),
                new Vertex(size, -size, -size),
                color));
        //K
        tris.add(new Triangle(new Vertex(size, -size, size),
                new Vertex(-size, -size, size),
                new Vertex(-size, -size, -size),
                color));
        //L
        tris.add(new Triangle(new Vertex(-size, -size, -size),
                new Vertex(size, -size, -size),
                new Vertex(size, -size, size),
                color));

    }

    @Override
    public Shape3D setColor(Color color) {
        super.setColor(color);
        for (Triangle t: tris) {
            t.setColor(color);
        }
        return this;
    }

    @Override
    public Shape3D resize(double size) {
        super.resize(size);
        for (Triangle t: tris) {
            t.getV1().resize(size);
            t.getV2().resize(size);
            t.getV3().resize(size);
        }
        return this;
    }
}
