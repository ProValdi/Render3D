package main;

import Shapes.*;
import Math.Matrix3;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

public class Scene extends JPanel {

    boolean switchSkeleton = false;
    boolean switchShades = false;
    private WindowView view;
    private Shape3D shape;

    public Scene(WindowView view) {
        this.view = view;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());

        double horizontal = Math.toRadians(view.horizontalSlider.getValue());
        // rotate XZ across Y
        Matrix3 horizontalTransform = new Matrix3(new double[] {Math.cos(horizontal), 0, Math.sin(horizontal),
                0, 1, 0,
                -Math.sin(horizontal), 0, Math.cos(horizontal)});

        double vertical = Math.toRadians(view.verticalSlider.getValue());
        // rotate YZ across X
        Matrix3 verticalTransform = new Matrix3(new double[] {1, 0, 0,
                0, Math.cos(vertical), Math.sin(vertical),
                0, -Math.sin(vertical), Math.cos(vertical)});

        Matrix3 transform = horizontalTransform.multiply(verticalTransform);

        if(switchSkeleton) {
            g2.translate(getWidth() / 2, getHeight() / 2);
            g2.setColor(shape.getSkeletonColor());
        }

        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        double[] zBuffer = new double[img.getWidth() * img.getHeight()];

        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }

        for (Triangle t : shape.getTris()) {
            Vertex v1 = transform.transform(t.getV1());
            Vertex v2 = transform.transform(t.getV2());
            Vertex v3 = transform.transform(t.getV3());

            //System.out.println(v1.toString() + "\n" + v2.toString() + "\n" + v3.toString());

            if(switchSkeleton) {
                Path2D path = new Path2D.Double();
                path.moveTo(v1.getX(), v1.getY());
                path.lineTo(v2.getX(), v2.getY());
                path.lineTo(v3.getX(), v3.getY());
                path.closePath();
                g2.draw(path);

            } else {

                Vertex ab = new Vertex(v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ());
                Vertex ac = new Vertex(v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ() - v1.getZ());
                Vertex norm = Vertex.getVectorMult(ab, ac).normalize();

                double angleCos = Math.abs(norm.getZ());

                v1.addX(getWidth() / 2);
                v1.addY(getHeight() / 2);

                v2.addX(getWidth() / 2);
                v2.addY(getHeight() / 2);

                v3.addX(getWidth() / 2);
                v3.addY(getHeight() / 2);


                //System.out.println(v1.toString() + "\n" + v2.toString() + "\n" + v3.toString());

                // calculate square bounds
                int minX = (int) Math.max(0, Math.ceil(Math.min(v1.getX(), Math.min(v2.getX(), v3.getX()))));
                int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.getX(), Math.max(v2.getX(), v3.getX()))));
                int minY = (int) Math.max(0, Math.ceil(Math.min(v1.getY(), Math.min(v2.getY(), v3.getY()))));
                int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.getY(), Math.max(v2.getY(), v3.getY()))));

                double quasiTriangleArea = (v1.getY() - v3.getY()) * (v2.getX() - v3.getX()) + (v2.getY() - v3.getY()) * (v3.getX() - v1.getX());

                // find pixels, which are inside the triangle and color them due barycentric coordinates
                for (int y = minY; y <= maxY; y++) {
                    for (int x = minX; x <= maxX; x++) {
                        double b1 = ((y - v3.getY()) * (v2.getX() - v3.getX()) + (v2.getY() - v3.getY()) * (v3.getX() - x)) / quasiTriangleArea;
                        double b2 = ((y - v1.getY()) * (v3.getX() - v1.getX()) + (v3.getY() - v1.getY()) * (v1.getX() - x)) / quasiTriangleArea;
                        double b3 = ((y - v2.getY()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v2.getX() - x)) / quasiTriangleArea;

                        if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                            double depth = b1 * v1.getZ() + b2 * v2.getZ() + b3 * v3.getZ();
                            int zIndex = y * img.getWidth() + x;

                            if (zBuffer[zIndex] < depth) {
                                if(!switchShades) {
                                    img.setRGB(x, y, getShade(t.getColor(), angleCos).getRGB());
                                } else {
                                    img.setRGB(x, y, t.getColor().getRGB());
                                }
                                zBuffer[zIndex] = depth;
                            }
                        }
                    }
                }
            }
        }

        g2.drawImage(img, 0, 0, null);

    }

    public Color getShade(Color color, double shade) {
        int red = (int) (color.getRed() * shade);
        int green = (int) (color.getGreen() * shade);
        int blue = (int) (color.getBlue() * shade);

        return new Color(red, green, blue);
    }

    public Scene addShape(Shape3D shape) {
        this.shape = shape;
        return this;
    }

    public Shape3D getShape() {
        return shape;
    }
}
