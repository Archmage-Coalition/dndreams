package net.eman3600.dndreams.util.matrices;

public class Matrix {
    private MatrixRow[] rows;

    public Matrix(MatrixRow[] rows) {
        this.rows = rows;
    }

    public Matrix(Matrix matrix) {
        this(matrix.rows(), matrix.columns());
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new MatrixRow(matrix.get(i));
        }
    }

    public Matrix(double[][] vals) {
        rows = new MatrixRow[vals.length];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new MatrixRow(vals[i]);
        }
    }

    public Matrix(int r, int c) {
        rows = new MatrixRow[r];
        for (int i = 0; i < r; i++) {
            rows[i] = new MatrixRow(c);
        }
    }

    private Matrix(int r) {
        rows = new MatrixRow[r];
    }

    public MatrixRow get(int r) {
        return rows[r];
    }


    public void swapRows(int r1, int r2) {
        MatrixRow temp = rows[r1];

        rows[r1] = rows[r2];
        rows[r2] = temp;
    }

    public void cycleRows() {
        cycleRows(0);
    }

    public void cycleRows(int first) {
        for (int i = first; i < rows() - 1; i++) {
            swapRows(i, i + 1);
        }
    }

    public void set(int r, int c, double val) {
        rows[r].set(c, val);
    }

    public void setRow(MatrixRow row, int index) {
        rows[index] = row;
    }

    public void addRow(int target, int addend, int scalar) {
        rows[target] = rows[target].add(rows[addend].scale(scalar));
    }

    public void scaleRow(int row, double scalar) {
        rows[row] = rows[row].scale(scalar);
    }

    public void simplifyRow(int row, int column) {
        rows[row] = rows[row].simplify(column);
        //System.out.println("Simplified row " + row + "to " + rows[row]);
    }

    public int rows() {
        return rows.length;
    }

    public int columns() {
        return rows[0].length();
    }

    public Matrix augment(Matrix matrix) {
        Matrix result = new Matrix(rows.length);
        for (int i = 0; i < rows.length; i++) {
            result.setRow(rows[i].augment(matrix.get(i)), i);
        }
        return result;
    }

    public Matrix augmentedSplit(int index, boolean solution) {
        Matrix matrix = new Matrix(rows());

        for (int i = 0; i < rows(); i++) {
            matrix.setRow(rows[i].augmentedSplit(index, solution), i);
        }

        return matrix;
    }

    public Matrix reduce() {
        return reduce(false);
    }

    public Matrix reduce(boolean debug) {
        Matrix result = new Matrix(this);

        return result.reduce(0, 0, debug);
    }

    private Matrix reduce(int r, int c, boolean debug) {
        if (debug) {
            System.out.println();
            System.out.println("Row: " + r + " x Column: " + c);
        }
        try {
            for (int i = r; i < this.rows() && this.get(r, c) == 0; i++) {
                this.cycleRows(r);
                if (debug) System.out.println("Cycled");
            }
            if (this.get(r, c) == 0) {
                return reduce(r, c + 1, debug);
            }

            if (this.get(r, c) != 1) {
                if (debug) System.out.println("Simplified row " + r + " using the entry: " +
                        this.get(r, c));
                this.simplifyRow(r, c);
            }

            for(int i = 0; i < this.rows(); i++) {
                if (i != r) {
                    this.setRow(this.get(r).reduce(this.get(i), c), i);
                }
            }

            if (debug) {
                System.out.println(this);
            }

            return reduce(r + 1, c + 1, debug);
        } catch (IndexOutOfBoundsException e) {
            return this;
        }
    }

    public double get(int r, int c) {
        return rows[r].get(c);
    }

    public String toString() {
        String str = "";

        for (int i = 0; i < rows.length; i++) {
            str += rows[i].toString() + "\n";
        }

        return str;
    }

    public static Matrix identity(int size) {
        Matrix matrix = new Matrix(size, size);
        for (int i = 0; i < size; i++) {
            matrix.set(i, i, 1);
        }
        return matrix;
    }

    public Matrix add(Matrix matrix) {
        Matrix result = new Matrix(this);

        if (rows() != matrix.rows()) {
            throw new ArithmeticException();
        }
        for (int i = 0; i < rows(); i++) {
            result.setRow(rows[i].add(matrix.get(i)), i);
        }

        return result;
    }

    public Matrix negate() {
        Matrix result = new Matrix(this);
        for (int i = 0; i < rows(); i++) {
            result.rows[i] = result.rows[i].negate();
        }
        return result;
    }

    public Matrix transpose() {
        Matrix result = new Matrix(columns(), rows());

        for (int i = 0; i < columns(); i++) {
            for (int j = 0; j < rows(); j++) {
                result.set(i, j, get(j, i));
            }
        }

        return result;
    }

    public Matrix inverse() {
        if (rows() != columns()) {
            throw new ArithmeticException();
        }

        Matrix combined = augment(identity(rows()));
        combined = combined.reduce(true);
        return combined.augmentedSplit(rows(), true);
    }

    public Matrix multiply(Matrix matrix) {
        if (columns() != matrix.rows()) {
            throw new ArithmeticException();
        }

        Matrix result = new Matrix(rows(), matrix.columns());
        Matrix transpose = matrix.transpose();

        for (int i = 0; i < result.rows(); i++) {
            for (int j = 0; j < result.columns(); j++) {
                result.set(i, j, get(i).multiply(transpose.get(j)));
            }
        }

        return result;
    }


    public Matrix devolve(int r, int c) {
        Matrix result = new Matrix(rows() - 1, columns() - 1);

        int n = 0;

        for (int i = 0; i < rows(); i++) {
            if (i == r) {
                continue;
            }

            int m = 0;
            for (int j = 0; j < columns(); j++) {
                if (j == c) {
                    continue;
                }

                result.set(n, m, get(i, j));

                m++;
            }

            n++;
        }

        System.out.println(result);
        return result;
    }

    public double determinantSlow() {
        if (rows() != columns()) {
            throw new ArithmeticException();
        }

        if (rows() > 2) {
            double result = 0;
            for (int i = 0; i < rows(); i++) {
                result += get(i, 0) * (i % 2 == 1 ? -1 : 1) * devolve(i, 0).determinantSlow();
            }

            return result;
        } else if (rows() == 2) {
            return (get(0, 0) * get(1, 1)) - (get(1, 0) * get(0, 1));
        } else if (rows() == 1) {
            return get(0, 0);
        } else {
            return 0;
        }
    }

    public double determinant() {
        if (rows() != columns()) {
            throw new ArithmeticException();
        }
        return new Matrix(this).determinant(0);
    }

    private double determinant(int r) {
        if (isLowerTriangular()) {
            int result = 1;
            for (int i = 0; i < rows(); i++) {
                result *= get(i, i);
            }
            return result;
        }

        try {
            int mult = 1;

            for (int i = r; i < this.rows() && this.get(r, r) == 0; i++) {
                this.cycleRows(r);
                mult *= -1;
            }

            Matrix temp = new Matrix(this);

            temp.rows[r] = new MatrixRow(temp.get(r)).simplify(r);

            for (int i = r + 1; i < rows(); i++) {
                temp.setRow(temp.get(r).reduce(temp.get(i), r), i);
                this.setRow(new MatrixRow(temp.get(i)), i);
            }

            return mult * determinant(r + 1);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }



    public boolean isLowerTriangular() {
        for (int i = 1; i < rows(); i++) {
            for (int j = 0; j < i; j++) {
                if (get(i, j) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
