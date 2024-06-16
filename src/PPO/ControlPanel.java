package PPO;

import javax.swing.*;
import java.awt.*;
import static PPO.Livescore.LIVES;
import static PPO.Livescore.SCORE;
import static PPO.Livescore.LEVEL;

public class ControlPanel extends JPanel {
    private JLabel scoreLabel;
    private JLabel livesLabel;
    private JLabel levelLabel;

    public ControlPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        scoreLabel = new JLabel("Score: " + SCORE);
        livesLabel = new JLabel("Lives: " + LIVES);
        levelLabel = new JLabel("Level: " + LEVEL);

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        add(Box.createVerticalGlue()); // Add glue to push components to the center vertically
        add(createCenteredPanel(scoreLabel));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createCenteredPanel(livesLabel));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createCenteredPanel(levelLabel));
        add(Box.createVerticalGlue());
    }

    private JPanel createCenteredPanel(JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue()); // Add glue to push components to the center horizontally
        panel.add(label);
        panel.add(Box.createHorizontalGlue()); // Add glue to push components to the center horizontally
        panel.setOpaque(false); // Make the panel transparent
        return panel;
    }

    public void updateScore() {
        scoreLabel.setText("Score: " + SCORE);
    }

    public void updateLives() {
        livesLabel.setText("Lives: " + LIVES);
    }

    public void updateLevel() {
        levelLabel.setText("Level: " + LEVEL);
    }

    public void updateInfo() {
        updateLevel();
        updateScore();
        updateLives();
    }
}

