package Tetris;

import Common.SoundPlayer;

import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {
    private Board board;
    private JPanel menuPanel;
    private JLabel scoreLabel;
    private int score = 0;
    private SoundPlayer beatboxSoundPlayer;


    public Tetris() {
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 800);  // Збільшено розмір вікна
        setLocationRelativeTo(null);
        setResizable(false);

        menuPanel = new JPanel(new BorderLayout());
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> startGame());
        menuPanel.add(startButton, BorderLayout.SOUTH);

        // Load and add image to menu
        ImageIcon imageIcon = new ImageIcon("src/images/tetris.png");
        JLabel imageLabel = new JLabel(imageIcon);
        menuPanel.add(imageLabel, BorderLayout.CENTER);

        beatboxSoundPlayer = new SoundPlayer("src/sounds/tetris-beatbox.wav");

        add(menuPanel, BorderLayout.CENTER);

        beatboxSoundPlayer.playLoop();

        setVisible(true);
    }

    public void stopMusic() {
        beatboxSoundPlayer.stop();
    }

    public void startGame() {
        if (board == null) {
            board = new Board(this);
            add(board, BorderLayout.CENTER);

            JPanel sidePanel = new JPanel();
            sidePanel.setPreferredSize(new Dimension(200, getHeight()));  // Ширина панелі для лічильника очків
            scoreLabel = new JLabel("Score: 0");
            sidePanel.add(scoreLabel);
            add(sidePanel, BorderLayout.EAST);

            board.start();
        }
        menuPanel.setVisible(false);
        board.setVisible(true);
        board.requestFocusInWindow();
    }

    public void updateScore(int points) {
        score += points;
        scoreLabel.setText("Score: " + score);
    }

    public static void main(String[] args) {
        Tetris game = new Tetris();
    }
}
