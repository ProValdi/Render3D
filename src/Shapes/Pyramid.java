package Shapes;

import java.awt.*;
import java.util.ArrayList;

public class Pyramid extends Shape3D {

    public Pyramid(Vertex position, double size, Color color) {
        this.position = position;
        this.size = size;
        this.color = color;
    }

    public Pyramid() {
        this(new Vertex(0, 0, 0), 100, Color.ORANGE);

        tris = new ArrayList<>();
        tris.add(new Triangle(new Vertex(size, size, size),
                new Vertex(-size, -size, size),
                new Vertex(-size, size, -size),
                color));

        tris.add(new Triangle(new Vertex(size, size, size),
                new Vertex(-size, -size, size),
                new Vertex(size, -size, -size),
                color));

        tris.add(new Triangle(new Vertex(-size, size, -size),
                new Vertex(size, -size, -size),
                new Vertex(size, size, size),
                color));

        tris.add(new Triangle(new Vertex(-size, size, -size),
                new Vertex(size, -size, -size),
                new Vertex(-size, -size, size),
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
