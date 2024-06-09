package PPO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel {
    private List<Missile> missiles;
    private List<EnemyMissile> enemyMissiles;
    private Timer timer;
    private int gunX, gunY;
    private Random random;
    private boolean nextMissileFromLeft;
    private int score;
    private int lives;
    private SoundPlayer shootSound;
    private static int missileSpeed = 5;
    private ImageIcon missileImage;
    private ImageIcon enemyMissileImage;
    private ImageIcon gunImage;
    private ImageIcon background;

    public GamePanel() {
        missiles = new ArrayList<>();
        enemyMissiles = new ArrayList<>();
        random = new Random();
        nextMissileFromLeft = true;
        score = 0;
        lives = 3;

        // Завантаження зображень та звуку
        shootSound = new SoundPlayer("src/sounds/shoot.wav"); // Вкажіть правильний шлях до файлу зі звуком
        missileImage = loadImage("src/images/missle.png");
        enemyMissileImage = loadImage("src/images/enemy.png");
        gunImage = loadImage("src/images/gun.png");
        background = loadImage("src/images/warbackg.png");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    shootMissile(e.getX(), e.getY());
                }
            }
        });

        timer = new Timer(10, e -> updateGame());
        timer.start();
    }

    private ImageIcon loadImage(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("Зображення не знайдено: " + file.getAbsolutePath());
            return null;
        }
        return new ImageIcon(file.getAbsolutePath());
    }

    public static int getMissileSpeed() {
        return missileSpeed;
    }

    public static void setMissileSpeed(int speed) {
        missileSpeed = speed;
    }

    private void shootMissile(int targetX, int targetY) {
        int startX = gunX + 15;
        int startY = gunY;
        missiles.add(new Missile(startX, startY, targetX, targetY, missileImage));
        if (shootSound != null) {
            shootSound.play();  // Відтворення звуку пострілу
        } else {
            System.err.println("Звуковий файл не завантажено.");
        }
    }

    private void spawnEnemyMissile() {
        int startX;
        int startY = 0;

        if (nextMissileFromLeft) {
            startX = 0;
        } else {
            startX = getWidth();
        }
        nextMissileFromLeft = !nextMissileFromLeft;

        enemyMissiles.add(new EnemyMissile(startX, startY, gunX + 15, gunY, enemyMissileImage));
    }

    private void updateGame() {
        // Оновлення положення ракет
        Iterator<Missile> missileIterator = missiles.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            missile.update();
            if (missile.isOffScreen()) {
                missileIterator.remove();
            }
        }

        // Оновлення положення ворожих ракет
        Iterator<EnemyMissile> enemyIterator = enemyMissiles.iterator();
        while (enemyIterator.hasNext()) {
            EnemyMissile enemyMissile = enemyIterator.next();
            enemyMissile.update();
            if (enemyMissile.isOffScreen()) {
                enemyIterator.remove();
                lives--;  // Зменшення життів при пропуску ворожої ракети
                if (lives <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
                    System.exit(0);
                }
            }
        }

        // Перевірка на зіткнення ракет
        for (Missile missile : missiles) {
            for (EnemyMissile enemyMissile : enemyMissiles) {
                if (missile.getBounds().intersects(enemyMissile.getBounds())) {
                    missile.setVisible(false);
                    enemyMissile.setVisible(false);
                    score++;
                }
            }
        }

        missiles.removeIf(missile -> !missile.isVisible());
        enemyMissiles.removeIf(enemyMissile -> !enemyMissile.isVisible());

        repaint();

        if (random.nextInt(100) < 2) {
            spawnEnemyMissile();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Малювання фону
        if (background != null) {
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        }

        // Малювання пушки
        gunX = getWidth() / 2 - 15;
        gunY = getHeight() - 100;
        if (gunImage != null) {
            g.drawImage(gunImage.getImage(), gunX, gunY, 30, 30, null);
        }

        // Малювання ракет
        for (Missile missile : missiles) {
            missile.draw(g);
        }

        // Малювання ворожих ракет
        for (EnemyMissile enemyMissile : enemyMissiles) {
            enemyMissile.draw(g);
        }

        // Малювання панелі керування
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, getHeight() - 50, getWidth(), 50);

        // Малювання сканера
        g.setColor(Color.GREEN);
        g.drawOval(getWidth() / 2 - 50, getHeight() - 100, 100, 100);

        // Малювання рахунку та життів
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, 10, 40);

        // Малювання ворожих ракет на сканері
        for (EnemyMissile enemyMissile : enemyMissiles) {
            int radarX = getWidth() / 2 - 50 + (enemyMissile.getX() * 100 / getWidth());
            int radarY = getHeight() - 100 + (enemyMissile.getY() * 100 / getHeight());
            g.fillOval(radarX, radarY, 5, 5);
        }
    }
}
