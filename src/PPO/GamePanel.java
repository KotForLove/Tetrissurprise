package PPO;

import Common.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static Common.ImageLoader.loadImage;
import static PPO.Livescore.*;

public class GamePanel extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private ControlPanel controlPanel;
    private java.util.Queue<Missile> missiles;
    private java.util.Queue<EnemyMissile> enemyMissiles;
    private Timer timer;
    private int gunX, gunY;
    private Random random;
    private SoundPlayer shootSound;
    private ImageIcon missileImage;
    private ImageIcon enemyMissileImage;
    private ImageIcon gunImage;
    private ImageIcon background;
    private ImageIcon leftPanelImage;
    private ImageIcon rightPanelImage;
    private GameFrame gameFrame;
    private SoundPlayer vietnamPlayer;

    public GamePanel(GameFrame gameFrame, ControlPanel controlPanel, SoundPlayer vietnamPlayer) {
        this.gameFrame = gameFrame;
        this.controlPanel = controlPanel;
        this.vietnamPlayer = vietnamPlayer;
        missiles = new ConcurrentLinkedQueue<>();
        enemyMissiles = new ConcurrentLinkedQueue<>();
        random = new Random();

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
                    CURRENT_SCORE++;
                    controlPanel.updateInfo();
                }
            }
        }

        enemyMissileIterator = enemyMissiles.iterator();
        while (enemyMissileIterator.hasNext()) {
            EnemyMissile enemyMissile = enemyMissileIterator.next();
            if (!enemyMissile.isVisible()) {
                enemyMissileIterator.remove();
                // Віднімання життя при досягненні цілі
                LIVES--;
                controlPanel.updateInfo();
                if (LIVES <= 0) {
                    gameOver();
                }
            }
        }

        controlPanel.updateInfo();
        repaint();

        if (random.nextInt(200) < 2 && ENEMY_MISSILES_LEFT > 0) {
            spawnEnemyMissile();
            ENEMY_MISSILES_LEFT--;
        }

        if(enemyMissiles.isEmpty() && ENEMY_MISSILES_LEFT == 0) {
            if(LEVEL == 1) {
                JDialog level2Dialog = new Level2Dialog(gameFrame);
            } else if(LEVEL == 2) {
                JDialog level3Dialog = new Level3Dialog(gameFrame);

                gunImage = loadImage("src/images/gun_night.png");
                background = loadImage("src/images/warbackg_night.png");
                missileImage = loadImage("src/images/missile_night.png");
                enemyMissileImage = loadImage("src/images/enemy_night.png");
            } else if(LEVEL == 3) {
                vietnamPlayer.stop();
                SuccessDialog successDialog = new SuccessDialog(gameFrame);
            }

            LIVES = 3;
            ENEMY_MISSILES_LEFT = ENEMY_MISSILES;
            CURRENT_SCORE = 0;
            LEVEL++;
            controlPanel.updateInfo();
        }
    }

    private void gameOver() {
        timer.stop();
        int option = JOptionPane.showOptionDialog(this, "Game Over. Your score: " + CURRENT_SCORE,
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
        CURRENT_SCORE = 0;
        LIVES = 3;
        LEVEL = 1;
        MISSILE_SPEED = 2;
        ENEMY_MISSILES_LEFT = ENEMY_MISSILES;
        controlPanel.updateInfo();
        timer.start();
    }

    private void goToMainMenu() {
        // Імплементуйте ваш метод для повернення на головне меню
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

        // Малювання ворожих ракет на сканері
        for (EnemyMissile enemyMissile : enemyMissiles) {
            int radarX = getWidth() / 2 - 50 + (enemyMissile.getX() * 100 / getWidth());
            int radarY = getHeight() - 100 + (enemyMissile.getY() * 100 / getHeight());
            g.setColor(Color.RED);
            g.fillOval(radarX, radarY, 5, 5);
        }
    }
}
