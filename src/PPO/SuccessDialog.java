package PPO;

import Common.SoundPlayer;

import javax.swing.*;
import java.awt.*;

public class SuccessDialog extends JDialog {
    public SuccessDialog(JFrame owner) {
        super(owner, "You have won!", true);

        JButton okButton = new JButton("Thanks!");

        // Додавання слухача подій для кнопки
        okButton.addActionListener(e -> {
            // Код для переходу в гру
            owner.dispose();
            dispose(); // Закриває діалогове вікно
        });

        // Завантаження та масштабування зображення
        ImageIcon originalIcon = new ImageIcon("src/images/soldier.png"); // Вкажіть шлях до вашого зображення
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);

        // Додавання зображення та кнопки до діалогового вікна
        setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);

        // Програвання звуку
        new SoundPlayer("src/sounds/thanks.wav").play();

        setSize(400, 400);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
