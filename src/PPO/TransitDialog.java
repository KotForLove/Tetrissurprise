package PPO;

import javax.swing.*;

public class TransitDialog extends JDialog {
    public TransitDialog(JFrame owner) {
        super(owner, "ALERT!!!", true);

        JButton okButton = new JButton("Become a PPO soldier");

        okButton.addActionListener(e -> dispose());

        add(okButton);

        new SoundPlayer("src/sounds/soldier_shout.wav").play();

        setSize(400, 400);

        setVisible(true);
        pack();
        setLocationRelativeTo(owner);
    }
}
