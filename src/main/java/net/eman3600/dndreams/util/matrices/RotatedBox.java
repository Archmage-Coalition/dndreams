package net.eman3600.dndreams.util.matrices;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class RotatedBox extends Box {
    private float yaw;


    public RotatedBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);

        yaw = 0;
    }

    public RotatedBox(BlockPos pos) {
        super(pos);

        yaw = 0;
    }

    public RotatedBox(BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);

        yaw = 0;
    }

    public RotatedBox(Vec3d pos1, Vec3d pos2) {
        super(pos1, pos2);

        yaw = 0;
    }

    public RotatedBox(Vec3d pos1, Vec3d pos2, float yaw) {
        super(pos1, pos2);

        this.yaw = yaw;
    }

    public RotatedBox(Vec3d pos1, Vec3d pos2, float yaw, float pitch) {
        super(pos1, pos2);

        this.yaw = yaw;
    }

    public RotatedBox(double d, double e, double f, double g, double h, double i, float yaw) {
        super(d, e, f, g, h, i);

        this.yaw = yaw;
    }


    public static boolean isSimple(Box box) {
        return !(box instanceof RotatedBox);
    }

    public static RotatedBox as(Box box) {
        if (box instanceof RotatedBox) {
            return (RotatedBox)box;
        }
        return new RotatedBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    public Vec3d minVec() {
        return new Vec3d(minX, minY, minZ);
    }

    public Vec3d maxVec() {
        return new Vec3d(maxX, maxY, maxZ);
    }


    public RotatedBox rotated(float yaw) {
        Vec3d center = getCenter();

        Vec3d min = minVec();
        Vec3d max = minVec();

        min = min.subtract(center).rotateY((float)Math.toRadians(-yaw)).add(center);
        max = max.subtract(center).rotateY((float)Math.toRadians(-yaw)).add(center);

        return new RotatedBox(min, max, this.yaw + yaw);
    }


    public Box straighten() {
        return new Box(minVec(), maxVec());
    }


    public static Vec3d matrixColToVec3d(Matrix matrix, int c) {
        double[] nums = new double[3];

        for (int i = 0; i < 3 && i < matrix.rows(); i++) {
            nums[i] = matrix.get(i, c);
        }

        return new Vec3d(nums[0], nums[1], nums[2]);
    }



    public static Matrix rotateMatrixCols(Matrix matrix, Vec3d center, float yaw) {
        Matrix result = matrix.transpose();

        for (int i = 0; i < result.rows(); i++) {
            Vec3d vec = matrixColToVec3d(matrix, i);

            vec = vec.subtract(center).rotateY((float)Math.toRadians(-yaw)).add(center);

            result.setRow(new MatrixRow(vec), i);
        }

        return result.transpose();
    }


    public Matrix toMatrix() {
        MatrixRow[] rows = new MatrixRow[8];

        rows[0] = new MatrixRow(minX, minY, minZ);
        rows[1] = new MatrixRow(maxX, minY, minZ);
        rows[2] = new MatrixRow(minX, maxY, minZ);
        rows[3] = new MatrixRow(maxX, maxY, minZ);
        rows[4] = new MatrixRow(minX, minY, maxZ);
        rows[5] = new MatrixRow(maxX, minY, maxZ);
        rows[6] = new MatrixRow(minX, maxY, maxZ);
        rows[7] = new MatrixRow(maxX, maxY, maxZ);

        Matrix matrix = new Matrix(rows).transpose();

        return rotateMatrixCols(matrix, getCenter(), yaw);
    }


    @Override
    public boolean intersects(Box box) {
        RotatedBox other;

        if (box instanceof RotatedBox) other = (RotatedBox) box;
        else other = as(box);

        return collides(other) || other.collides(this);
    }

    @Override
    public boolean intersects(Vec3d pos1, Vec3d pos2) {
        return intersects(new Box(pos1, pos2));
    }

    @Override
    public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return intersects(new Box(minX, minY, minZ, maxX, maxY, maxZ));
    }

    private boolean collides(RotatedBox box) {
        Matrix self = toMatrix();

        for (int i = 0; i < 8; i++) {
            Vec3d vertex = matrixColToVec3d(self, i);

            if (box.contains(vertex)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return contains(new Vec3d(x, y, z));
    }

    @Override
    public boolean contains(Vec3d vec) {
        Matrix matrix = toMatrix();

        for (int j = 0; j < 4; j++) {
            Vec3d in = matrixColToVec3d(matrix, j);
            Vec3d out = matrixColToVec3d(matrix, 7 - j);

            if (!vec.isInRange(in, in.distanceTo(out))) break;

            if (j == 3) return true;
        }

        return false;
    }

    public Box engulf() {
        Matrix matrix = toMatrix();

        double[] smallest = new double[] {
                matrix.get(0, 0),
                matrix.get(1, 0),
                matrix.get(2, 0)
        };

        double[] largest = new double[] {
                matrix.get(0, 0),
                matrix.get(1, 0),
                matrix.get(2, 0)
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 1; j < 8; j++) {
                if (matrix.get(i, j) < smallest[i]) {
                    smallest[i] = matrix.get(i, j);
                }
                if (matrix.get(i, j) > largest[i]) {
                    largest[i] = matrix.get(i, j);
                }
            }
        }

        return new Box(smallest[0], smallest[1], smallest[2], largest[0], largest[1], largest[2]);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && ((RotatedBox)o).yaw == this.yaw;
    }

    @Override
    public RotatedBox expand(double value) {
        return this.expand(value, value, value);
    }

    @Override
    public RotatedBox expand(double x, double y, double z) {
        double d = this.minX - x;
        double e = this.minY - y;
        double f = this.minZ - z;
        double g = this.maxX + x;
        double h = this.maxY + y;
        double i = this.maxZ + z;
        return new RotatedBox(d, e, f, g, h, i, yaw);
    }
}
