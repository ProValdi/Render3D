package Math;

import Shapes.Vertex;

public class Matrix3 {
    private final double[] values;

    public Matrix3(double[] values) {
        this.values = values;
    }

    public double[] getValues () {
        return values;
    }

    public Matrix3 multiply(Matrix3 other) {
        double[] result = new double[9];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] += this.values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
        return new Matrix3(result);
    }

    public Vertex transform(Vertex in) {
        double x = in.getX();
        double y = in.getY();
        double z = in.getZ();

        return new Vertex(x * values[0] + y * values[3] + z * values[6],
                x * values[1] + y * values[4] + z * values[7],
                x * values[2] + y * values[5] + z * values[8]);
    }
}
