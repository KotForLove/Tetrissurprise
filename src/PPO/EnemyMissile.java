package PPO;

import javax.swing.*;
import java.awt.*;

import static PPO.Livescore.MISSILE_SPEED;

public class EnemyMissile {
    private int x, y;
    private int targetX, targetY;
    private int dx, dy;
    private boolean visible;
    private Image image;

    public EnemyMissile(int x, int y, int targetX, int targetY, ImageIcon enemyMissileImage) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.visible = true;
        this.image = enemyMissileImage.getImage();

        int deltaX = targetX - x;
        int deltaY = targetY - y;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double steps = distance / MISSILE_SPEED;
        this.dx = (int) (deltaX / steps);
        this.dy = (int) (deltaY / steps);
    }

    public void update() {
        x += dx;
        y += dy;
        if (x < 0 || x > 800 || y < 0 || y > 600) {
            visible = false;
        }
    }

    public void draw(Graphics g) {
        if (visible) {
            g.drawImage(image, x, y, 40, 20, null);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 2);
    }

    public boolean isOffScreen() {
        return x < 0 || x > 800 || y < 0 || y > 600;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
