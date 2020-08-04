package Shapes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Sphere extends Shape3D {

    public Sphere(Vertex position, double size, Color color) {
        this.position = position;
        this.size = size;
        this.color = color;
    }

    public Sphere() {
        this(new Vertex(0, 0, 0), 100, Color.ORANGE);

        Pyramid pyramid = new Pyramid();
        pyramid.setColor(color);
        pyramid.setSize(size);
        pyramid.setPosition(position);

        tris = inflate(pyramid.getTris(), 6);

    }

    private List<Triangle> inflate(List<Triangle> tris, int depth) {
        if(depth-- == 0) {
            return tris;
        }

        List<Triangle> result = new ArrayList<>();
        for (Triangle t : tris) {
            int divider = 16;

            Vertex m1 = new Vertex((t.getV1().getX() + t.getV2().getX())/divider, (t.getV1().getY() + t.getV2().getY())/divider, (t.getV1().getZ() + t.getV2().getZ())/divider);
            Vertex m2 = new Vertex((t.getV2().getX() + t.getV3().getX())/divider, (t.getV2().getY() + t.getV3().getY())/divider, (t.getV2().getZ() + t.getV3().getZ())/divider);
            Vertex m3 = new Vertex((t.getV1().getX() + t.getV3().getX())/divider, (t.getV1().getY() + t.getV3().getY())/divider, (t.getV1().getZ() + t.getV3().getZ())/divider);

            result.add(new Triangle(t.getV1(), m1, m3, t.getColor()));
            result.add(new Triangle(t.getV2(), m1, m2, t.getColor()));
            result.add(new Triangle(t.getV3(), m2, m3, t.getColor()));
            result.add(new Triangle(m1, m2, m3, t.getColor()));

        }
        for (Triangle t : result) {
            for (Vertex v : new Vertex[] { t.getV1(), t.getV2(), t.getV3() }) {
                v.normalize().multiply(size);
            }
        }
        return inflate(result, depth);
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
        Pyramid pyramid = new Pyramid();
        pyramid.setSize(size);
        tris = inflate(pyramid.getTris(), 6);
        return this;
    }
}
