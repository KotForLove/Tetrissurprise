package PPO;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {
    private JSlider speedSlider;
    private MainMenu mainMenu;

    public SettingsDialog(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("Settings");
        setSize(400, 300);

        add(getSettingsPanel());
    }

    private JPanel getSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        settingsPanel.setLayout(new GridLayout(3, 1));

        JPanel speedPanel = new JPanel();
        speedPanel.add(new JLabel("Missile Speed:"));
        speedSlider = new JSlider(1, 10, GamePanel.getMissileSpeed());
        speedPanel.add(speedSlider);
        settingsPanel.add(speedPanel);

        // Кнопка збереження
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            GamePanel.setMissileSpeed(speedSlider.getValue());
            dispose();
        });
        settingsPanel.add(saveButton);

        return settingsPanel;
    }
}
