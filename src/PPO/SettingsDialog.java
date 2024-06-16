package PPO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static PPO.Livescore.MISSILE_SPEED;

public class SettingsDialog extends JDialog {
    private JSlider missileSpeedSlider;

    public SettingsDialog(Frame owner) {
        super(owner, "Settings", true);

        missileSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, MISSILE_SPEED);
        missileSpeedSlider.setMajorTickSpacing(5);
        missileSpeedSlider.setMinorTickSpacing(1);
        missileSpeedSlider.setPaintTicks(true);
        missileSpeedSlider.setPaintLabels(true);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Missile Speed:"));
        panel.add(missileSpeedSlider);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MISSILE_SPEED = missileSpeedSlider.getValue();
                dispose();
            }
        });

        panel.add(saveButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(owner);
    }
}
