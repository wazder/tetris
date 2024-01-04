import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class board extends JPanel {
    private final int width = 12;
    private final int height = 24;
    private int currentX;
    private int currentY;

    private boolean isFallingFinished;
    private gameStatus gameStatus;
    private boolean isPaused;

    private tetromino[] board;
    private shape currentPiece;
    private final Timer timer = new Timer(300, new GameCycle());

    private final Color backgroundColor = new Color(100, 100, 100);
    private boolean tester = true;
    void startTest() {
        setStarted();
    }
    private int squareWIDTH() {
        return (int) getSize().getWidth() / width;
    }

    private int squareHEIGHT() {
        return (int) getSize().getHeight() / height;
    }

    private void initializeBoard() {
        setFocusable(true);
        addKeyListener(new TAdapter());
    }

    public board() {
        initializeBoard();
    }

    private tetromino shapeAt(int x, int y) {
        return board[(y * width) + x];
    }

    void setStarted() {
        gameStatus = gameStatus.PLAYING;
        isFallingFinished = false;
        isPaused = false;

        board = new tetromino[width * height];

        currentPiece = new shape();
        currentPiece.setRandomShape();

        clearBoard();
        newPiece();
        timer.start();

    }


    private void clearBoard() {
        for (int i = 0; i < height * width; ++i) {
            board[i] = tetromino.NO_SHAPE;
        }
    }

    private void newPiece() {
        currentPiece.setRandomShape();
        currentX = width / 2 + 1;
        currentY = height - 1 + currentPiece.minCordForY();
        if (!tryMove(currentPiece, currentX, currentY)) {
            currentPiece.setShape(tetromino.NO_SHAPE);
            timer.stop();
            gameStatus = gameStatus.GAME_OVER;
        }
    }

    private void oneLineDown() {
        if (!tryMove(currentPiece, currentX, currentY - 1)) {
            pieceDropped();
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            board[(y * width) + x] = currentPiece.getShape();
        }
        removeFullLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = height - 1; i >= 0; --i) {
            boolean lineIsFull = true;
            for (int j = 0; j < width; ++j) {
                if (shapeAt(j, i) == tetromino.NO_SHAPE) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < height - 1; ++k) {
                    for (int j = 0; j < width; ++j) {
                        board[(k * width) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }
        if (numFullLines > 0) {
            isFallingFinished = true;
            currentPiece.setShape(tetromino.NO_SHAPE);
            repaint();
        }
    }

    private class GameCycle implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private void update() {
        if (isPaused) {
            return;
        }
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    private boolean tryMove(shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return false;
            }
            if (shapeAt(x, y) != tetromino.NO_SHAPE) {
                return false;
            }
        }
        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        repaint();
        return true;
    }

    private void drawSquare(Graphics g, int x, int y, tetromino shape) {
        Color[] colors = {new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102), new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204), new Color(218, 170, 0)};
        Color color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWIDTH() - 2, squareHEIGHT() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHEIGHT() - 1, x, y);
        g.drawLine(x, y, x + squareWIDTH() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHEIGHT() - 1, x + squareWIDTH() - 1, y + squareHEIGHT() - 1);
        g.drawLine(x + squareWIDTH() - 1, y + squareHEIGHT() - 1, x + squareWIDTH() - 1, y + 1);

    }
    private void doDrawing(Graphics g) {
        var size = getSize();
        int boardTop = (int) size.getHeight() - height * squareHEIGHT();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                tetromino shape = shapeAt(j, height - i - 1);
                if (shape != tetromino.NO_SHAPE) {
                    drawSquare(g, j * squareWIDTH(), boardTop + i * squareHEIGHT(), shape);
                }
            }
        }
        if (currentPiece.getShape() != tetromino.NO_SHAPE) {
            for (int i = 0; i < 4; ++i) {
                int x = currentX + currentPiece.x(i);
                int y = currentY - currentPiece.y(i);
                drawSquare(g, x * squareWIDTH(), boardTop + (height - y - 1) * squareHEIGHT(), currentPiece.getShape());
            }
        }
    }
    public void paintComponent(Graphics g) {
        setBackground(backgroundColor);
        super.paintComponent(g);
        doDrawing(g);
    }
    private void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentPiece,currentX,newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    // inputs
    private class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (gameStatus == gameStatus.NO_GAME) {
                return;
            }
            int keycode = e.getKeyCode();
            if(keycode == 's' || keycode == 'S') {
                if (tester) {
                    setStarted();
                    isPaused = false;
                    tester = false;
                    return;
                }
            }
            if (keycode == 'r' || keycode == 'R') {
                setStarted();
                timer.stop();
                timer.start();
                return;
            }
            if (keycode == 'c' || keycode == 'C') {
                clearBoard();

                repaint();

                return;
            }
            if (keycode == 'p' || keycode == 'P') {
                isPaused = !isPaused;

                return;
            }
            switch (keycode) {
                case KeyEvent.VK_LEFT -> tryMove(currentPiece, currentX - 1, currentY);
                case KeyEvent.VK_RIGHT -> tryMove(currentPiece, currentX + 1, currentY);
                case KeyEvent.VK_DOWN -> tryMove(currentPiece.rotateRight(), currentX, currentY);
                case KeyEvent.VK_UP -> tryMove(currentPiece.rotateLeft(), currentX, currentY);
                case KeyEvent.VK_SPACE -> dropDown();
                case 'd', 'D' -> oneLineDown();
            }
        }
    }
}