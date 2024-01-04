import javax.swing.*;

public class game extends JFrame {
    public game() {
        initializeUI();
    }

    private void initializeUI() {
        var gameBoard = new board();
        add(gameBoard);
        gameBoard.startTest();

        setTitle("Tetris Game by wazder");
        setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            game tetrisGame = new game();
            tetrisGame.setVisible(true);
        });
    }
}