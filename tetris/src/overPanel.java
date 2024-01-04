import javax.swing.*;
import java.awt.*;

public class overPanel extends JPanel {
    public overPanel() {
        setPreferredSize(new Dimension(400, 200));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(gameOverLabel, BorderLayout.CENTER);
    }
}
