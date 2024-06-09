package PPO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GamePanel extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;


    private java.util.Queue<Missile> missiles;
    private java.util.Queue<EnemyMissile> enemyMissiles;
    private Timer timer;
    private int gunX, gunY;
    private Random random;
    private boolean nextMissileFromLeft;
    private int score;
    private int lives;
    private SoundPlayer shootSound;
    private static int missileSpeed = 2;
    private ImageIcon missileImage;
    private ImageIcon enemyMissileImage;
    private ImageIcon gunImage;
    private ImageIcon background;
    private ImageIcon leftPanelImage;
    private ImageIcon rightPanelImage;

    public GamePanel() {
        missiles = new ConcurrentLinkedQueue<>();
        enemyMissiles = new ConcurrentLinkedQueue<>();
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
        leftPanelImage = loadImage("src/images/leftpanel.jpg");
        rightPanelImage = loadImage("src/images/rightpanel.jpg");

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

    private void shootMissile(int targetX, int targetY) {
        int missileX = gunX + 50; // Зміщено для більших розмірів пушки
        int missileY = gunY;
        missiles.add(new Missile(missileX, missileY, targetX, targetY, missileImage));
        shootSound.play();
    }

    private void spawnEnemyMissile() {
        int startX = random.nextInt(getWidth());
        int targetX = getWidth() / 2;
        int targetY = getHeight() - 300;
        enemyMissiles.add(new EnemyMissile(startX, 0, targetX, targetY, enemyMissileImage));
    }

    private void updateGame() {
        for (Missile missile : missiles) {
            missile.update();
        }

        for (EnemyMissile enemyMissile : enemyMissiles) {
            enemyMissile.update();
        }

        Iterator<Missile> missileIterator = missiles.iterator();

        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            if (!missile.isVisible()) {
                missileIterator.remove();
            }
        }

        missileIterator = missiles.iterator();
        Iterator<EnemyMissile> enemyMissileIterator;
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            enemyMissileIterator = enemyMissiles.iterator();

            while (enemyMissileIterator.hasNext()) {
                EnemyMissile enemyMissile = enemyMissileIterator.next();
                if (missile.getBounds().intersects(enemyMissile.getBounds())) {
                    enemyMissiles.remove(enemyMissile);
                    score++;
                }
            }
        }

        enemyMissileIterator = enemyMissiles.iterator();
        while (enemyMissileIterator.hasNext()) {
            EnemyMissile enemyMissile = enemyMissileIterator.next();
            if (!enemyMissile.isVisible()) {
                enemyMissileIterator.remove();
                // Віднімання життя при досягненні цілі
                lives--;
                if (lives <= 0) {
                    gameOver();
                }
            }
        }

        repaint();

        if (random.nextInt(200) < 2) {
            spawnEnemyMissile();
        }
    }

    private void gameOver() {
        timer.stop();
        int option = JOptionPane.showOptionDialog(this, "Game Over. Your score: " + score,
                "Game Over", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Restart"}, "Restart");

        if (option == 0) {
            resetGame();
        } else {
            // Повернутись на головне меню (імплементуйте ваш метод для цього)
            goToMainMenu();
        }
    }

    private void resetGame() {
        missiles.clear();
        enemyMissiles.clear();
        score = 0;
        lives = 3;
        timer.start();
    }

    private void goToMainMenu() {
        // Імплементуйте ваш метод для повернення на головне меню
    }

    public static int getMissileSpeed() {
        return missileSpeed;
    }

    public static void setMissileSpeed(int speed) {
        missileSpeed = speed;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Малювання фону
        if (background != null) {
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        }

        // Малювання пушки
        gunX = getWidth() / 2 - 50; // Збільшено для більших розмірів пушки
        gunY = getHeight() - 200; // Піднято вище
        if (gunImage != null) {
            g.drawImage(gunImage.getImage(), gunX, gunY, 100, 100, null); // Збільшено для більших розмірів пушки
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
        g.fillRect(0, getHeight() - 100, getWidth(), 100);

        // Малювання сканера
        if (leftPanelImage != null && rightPanelImage != null) {
            int scannerCenterX = getWidth() / 2 - 50;
            int scannerCenterY = getHeight() - 100; // Зміщено нижче
            g.setColor(Color.BLACK);
            g.fillOval(scannerCenterX, scannerCenterY, 100, 100); // Тло сканера
            g.setColor(Color.GREEN);
            g.drawOval(scannerCenterX, scannerCenterY, 100, 100);

            // Малювання зображень панелі керування
            int panelHeight = 200;
            g.drawImage(leftPanelImage.getImage(), 0, getHeight() - 100, scannerCenterX, panelHeight, null); // Розтягнуто на всю ліву частину
            g.drawImage(rightPanelImage.getImage(), scannerCenterX + 100, getHeight() - 100, getWidth() - scannerCenterX - 100, panelHeight, null); // Розтягнуто на всю праву частину
        }

        // Малювання рахунку та життів
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, getHeight() - 80);
        g.drawString("Lives: " + lives, 10, getHeight() - 60);

        // Малювання ворожих ракет на сканері
        for (EnemyMissile enemyMissile : enemyMissiles) {
            int radarX = getWidth() / 2 - 50 + (enemyMissile.getX() * 100 / getWidth());
            int radarY = getHeight() - 100 + (enemyMissile.getY() * 100 / getHeight());
            g.setColor(Color.RED);
            g.fillOval(radarX, radarY, 5, 5);
        }
    }
}
