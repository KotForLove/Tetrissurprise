package PPO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private JSlider speedSlider;
    private JSlider buttonScaleSlider;
    private MainMenu mainMenu;

    public SettingsDialog(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("Settings");
        setSize(400, 300);
        setLayout(new GridLayout(3, 1));

        // Налаштування швидкості ракет
        JPanel speedPanel = new JPanel();
        speedPanel.add(new JLabel("PPO.Missile Speed:"));
        speedSlider = new JSlider(1, 10, GamePanel.getMissileSpeed());
        speedPanel.add(speedSlider);
        add(speedPanel);

        // Налаштування масштабу кнопок
        JPanel scalePanel = new JPanel();
        scalePanel.add(new JLabel("Button Scale:"));
        buttonScaleSlider = new JSlider(1, 20, 10);
        scalePanel.add(buttonScaleSlider);
        add(scalePanel);

        // Кнопка збереження
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenu.setButtonScale(buttonScaleSlider.getValue() / 10.0f);
                GamePanel.setMissileSpeed(speedSlider.getValue());
                dispose();
            }
        });
        add(saveButton);
    }
}
