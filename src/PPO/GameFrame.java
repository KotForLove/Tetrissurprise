package PPO;

import Common.SoundPlayer;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private ControlPanel controlPanel;

    public GameFrame(SoundPlayer vietnamPlayer) {
        setTitle("PPO Defense");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        controlPanel = new ControlPanel();
        gamePanel = new GamePanel(this, controlPanel, vietnamPlayer);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        controlPanel.setPreferredSize(new Dimension(GamePanel.WIDTH / 5, GamePanel.HEIGHT));

        pack();
        setSize(GamePanel.WIDTH + GamePanel.WIDTH / 5, GamePanel.HEIGHT);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }
}
