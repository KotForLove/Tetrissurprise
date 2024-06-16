package PPO;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private GamePanel gamePanel;
    private ControlPanel controlPanel;

    public GameFrame() {
        setTitle("PPO Defense");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        controlPanel = new ControlPanel();
        gamePanel = new GamePanel(controlPanel);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        controlPanel.setPreferredSize(new Dimension(GamePanel.WIDTH / 5, GamePanel.HEIGHT));

        pack();
        setSize(GamePanel.WIDTH + GamePanel.WIDTH / 5, GamePanel.HEIGHT);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }
}
