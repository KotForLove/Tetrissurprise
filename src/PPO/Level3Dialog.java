package PPO;

import javax.swing.*;
import java.awt.*;

public class Level3Dialog extends JDialog {
    public Level3Dialog(JFrame owner) {
        super(owner, "New Level", true);

        // Create the button with the text "Move to level 2"
        JButton okButton = new JButton("Move to level 3");

        // Add an action listener to the button
        okButton.addActionListener(e -> {
            // Code to transition to the game
            dispose(); // Closes the dialog
        });

        // Create the text area with the message
        JTextArea text = new JTextArea("Congrats! You have accomplished level 2. Now you will be fighting in the night and some missiles can only be seen on radar.");
        text.setFont(new Font("Arial", Font.BOLD, 18));
        text.setOpaque(false);
        text.setEditable(false);
        text.setFocusable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setAlignmentX(JTextArea.CENTER_ALIGNMENT);

        // Create a panel with GridBagLayout to center the text
        JPanel textPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        textPanel.add(text, gbc);
        textPanel.setOpaque(false);

        // Set the layout for the dialog and add components
        setLayout(new BorderLayout());
        add(textPanel, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);

        // Set the size and location of the dialog
        setSize(400, 400);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
