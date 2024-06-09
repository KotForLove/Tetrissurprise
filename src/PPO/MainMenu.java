package PPO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenu extends JFrame implements ActionListener {
    private JButton startButton;
    private JButton settingsButton;
    private JButton authorsButton;

    public MainMenu() {
        // Налаштування основного вікна
        setTitle("PPO Defense Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Створення панелі
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                File bgFile = new File("src/images/image0_0.jpg");
                if (!bgFile.exists()) {
                    System.err.println("Фонова картинка не знайдена: " + bgFile.getAbsolutePath());
                } else {
                    ImageIcon background = new ImageIcon(bgFile.getAbsolutePath());
                    Image image = background.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Створення кнопок
        startButton = createButton("Start", "src/images/start.png");
        settingsButton = createButton("Settings", "src/images/settings.png");
        authorsButton = createButton("Authors", "src/images/authors.png");

        // Додавання слухачів до кнопок
        startButton.addActionListener(this);
        settingsButton.addActionListener(this);
        authorsButton.addActionListener(this);

        // Додавання кнопок до панелі
        panel.add(Box.createRigidArea(new Dimension(0, 100)));
        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(settingsButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(authorsButton);

        // Вирівнювання кнопок по центру
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Додавання панелі до вікна
        add(panel);

        // Відображення вікна
        setVisible(true);
    }

    private JButton createButton(String text, String imagePath) {
        File imgFile = new File(imagePath);
        if (!imgFile.exists()) {
            System.err.println("Зображення для кнопки не знайдено: " + imgFile.getAbsolutePath());
        }
        ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
        JButton button = new JButton(new ImageIcon(icon.getImage().getScaledInstance(
                100, 50, Image.SCALE_SMOOTH)));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setText(text);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.WHITE); // колір тексту
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // Перейти до гри
            JFrame gameFrame = new JFrame("PPO Defense Game");
            GamePanel gamePanel = new GamePanel();
            gameFrame.add(gamePanel);
            gameFrame.setSize(800, 800);
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);
            this.dispose();
        } else if (e.getSource() == settingsButton) {
            // Відкрити налаштування
            SettingsDialog settingsDialog = new SettingsDialog(this);
            settingsDialog.setVisible(true);
        } else if (e.getSource() == authorsButton) {
            // Перейти до авторів
            System.out.println("Authors");
        }
    }

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
    }
}
