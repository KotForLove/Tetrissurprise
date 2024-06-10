package PPO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TransitDialog extends JDialog {
    public TransitDialog(JFrame owner) {
        super(owner, "ALERT!!!", true);

        // Створення кнопки з текстом "Я Згоден"
        JButton okButton = new JButton("Я Згоден");

        // Додавання слухача подій для кнопки
        okButton.addActionListener(e -> {
            // Код для переходу в гру
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
        new SoundPlayer("src/sounds/soldier_shout.wav").play();

        setSize(400, 400);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
