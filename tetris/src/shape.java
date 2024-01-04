import java.util.Random;

public class shape {
    private tetromino pieceShape;
    private final int[][] coords;

    public shape() {

        coords = new int[4][2];
        setShape(tetromino.NO_SHAPE);
    }

    void setShape(tetromino shape) {

        int[][][] coordsTable = new int[][][]{
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{0, -1}, {1,0}, {-1, 0}, {0, 1}}

        };
        for (int i = 0; i < 4; i++) {

            System.arraycopy(coordsTable[shape.ordinal()], 0, coords, 0, 4);
        }

        pieceShape = shape;
    }

    private void setX(int index, int x) {

        coords[index][0] = x;
    }

    private void setY(int index, int y) {

        coords[index][1] = y;
    }

    int x(int index) {

        return coords[index][0];
    }

    int y(int index) {

        return coords[index][1];
    }

    tetromino getShape() {

        return pieceShape;
    }

    void setRandomShape() {

        var r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;

        tetromino[] values = tetromino.values();
        setShape(values[x]);
    }


    int minCordForY() {

        int minimum = coords[0][1];

        for (int i = 0; i < 4; i++) {

            minimum = Math.min(minimum, coords[i][1]);
        }

        return minimum;
    }

    shape rotateLeft() {

        if (pieceShape == tetromino.SQUARE_SHAPE) {

            return this;
        }

        var result = new shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {

            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }
    shape rotateRight() {

        if (pieceShape == tetromino.SQUARE_SHAPE) {

            return this;
        }

        var result = new shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {

            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }

}