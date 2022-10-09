package net.eman3600.dndreams.util.matrices;

import net.minecraft.util.math.Vec3d;

public class MatrixRow {
    private final double[] row;

    protected MatrixRow(int rows) {
        row = new double[rows];
    }

    protected MatrixRow(double... row) {
        this.row = row;
    }

    protected MatrixRow(Vec3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    protected MatrixRow(MatrixRow matrixRow) {
        row = new double[matrixRow.row.length];
        for (int i = 0; i < row.length; i++) {
            row[i] = matrixRow.row[i];
        }
    }


    protected MatrixRow scale(double scalar) {
        MatrixRow matrix = new MatrixRow(this);
        for (int i = 0; i < row.length; i++) {
            matrix.row[i] *= scalar;
        }
        return matrix;
    }

    protected MatrixRow simplify(int index) {
        MatrixRow matrix = new MatrixRow(this);
        double scalar = row[index];

        if (scalar == 1) {
            return matrix;
        }

        for (int i = 0; i < row.length; i++) {
            matrix.row[i] /= scalar;
        }
        matrix.row[index] = 1;
        return matrix;
    }

    protected void set(int index, double val) {
        row[index] = val;
    }

    protected double get(int index) {
        return row[index];
    }

    protected int length() {
        return row.length;
    }

    protected MatrixRow add(double[] matrixRow) {
        if (matrixRow.length != row.length) {
            throw new ArithmeticException();
        }

        //System.out.println(this + " +");
        //System.out.println(new MatrixRow(matrixRow) + " =");

        MatrixRow newRow = new MatrixRow(row.length);

        for (int i = 0; i < row.length; i++) {
            newRow.set(i, row[i] + matrixRow[i]);
        }

        //System.out.println(newRow);

        return newRow;
    }

    protected MatrixRow add(MatrixRow matrixRow) {
        return add(matrixRow.row);
    }

    protected MatrixRow negate() {
        MatrixRow result = new MatrixRow(this);

        for (int i = 0; i < row.length; i++) {
            result.set(i, - result.get(i));
        }

        return result;
    }

    protected MatrixRow augmentedSplit(int index, boolean solution) {
        int startingIndex;
        int endingIndex;
        MatrixRow matrix;

        if (!solution) {
            startingIndex = 0;
            endingIndex = index;
        } else {
            startingIndex = index;
            endingIndex = row.length;
        }

        matrix = new MatrixRow(endingIndex - startingIndex);

        for (int i = startingIndex; i < endingIndex; i++) {
            matrix.set(i - startingIndex, row[i]);
        }
        return matrix;
    }

    protected MatrixRow augment(MatrixRow matrix) {
        int length = row.length + matrix.length();
        MatrixRow result = new MatrixRow(length);

        for (int i = 0; i < row.length; i++) {
            result.set(i, row[i]);
        }
        for (int i = 0; i < matrix.length(); i++) {
            result.set(i + row.length, matrix.get(i));
        }

        return result;
    }

    protected MatrixRow reduce(MatrixRow matrix, int index) {
        MatrixRow input = new MatrixRow(this);

        if (row[index] != 1.0) {
            input = scale(1 / row[index]);
        }

        input = input.scale(matrix.get(index));

        return matrix.add(input.negate());
    }

    protected double multiply(MatrixRow matrix) {
        if (length() != matrix.length()) {
            throw new ArithmeticException();
        }

        double result = 0;

        for (int i = 0; i < length(); i++) {
            result += get(i) * matrix.get(i);
        }

        return result;
    }

    public String toString() {
        String str = "[";
        for (int j = 0; j < length(); j++) {
            str += get(j) + "\t";
        }
        str += "]";
        return str;
    }
}
