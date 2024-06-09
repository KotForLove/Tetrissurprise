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
        setLayout(new GridLayout(3, 1));

        // Налаштування швидкості ракет
        JPanel speedPanel = new JPanel();
        speedPanel.add(new JLabel("Missile Speed:"));
        speedSlider = new JSlider(1, 10, GamePanel.getMissileSpeed());
        speedPanel.add(speedSlider);
        add(speedPanel);

        // Кнопка збереження
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            GamePanel.setMissileSpeed(speedSlider.getValue());
            dispose();
        });
        add(saveButton);
    }
}
