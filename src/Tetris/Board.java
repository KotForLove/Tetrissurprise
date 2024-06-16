package Tetris;

import Common.SoundPlayer;
import PPO.PPOMainMenu;
import PPO.TransitDialog;
import Tetris.Tetris;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 20;
    private final int CELL_SIZE = 30;  // Зменшено розмір клітинок для кращого відображення
    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int curX = 0;
    private int curY = 0;
    private Piece curPiece;
    private Piece.Tetrominoes[] board;
    private Tetris parent;
    private int PIECES_COUNT = 0;
    private SoundPlayer rotateSoundPlayer;
    private SoundPlayer moveSoundPlayer;
    private SoundPlayer dropSoundPlayer;
    private SoundPlayer linefallSoundPlayer;

    public Board(Tetris parent) {
        this.parent = parent;
        setFocusable(true);
        curPiece = new Piece();
        timer = new Timer(100, this);
        board = new Piece.Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
        rotateSoundPlayer = new SoundPlayer("src/sounds/rotate.wav");
        moveSoundPlayer = new SoundPlayer("src/sounds/move.wav");
        dropSoundPlayer = new SoundPlayer("src/sounds/softdrop.wav");
        linefallSoundPlayer = new SoundPlayer("src/sounds/linefall.wav");
        addKeyListener(new TAdapter());
        clearBoard();
    }

    public void start() {
        if (isPaused)
            return;
        isStarted = true;
        isFallingFinished = false;
        clearBoard();
        newPiece();
        timer.start();
    }

    private void pause() {
        if (!isStarted)
            return;
        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * CELL_SIZE;

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Piece.Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Piece.Tetrominoes.NoShape)
                    drawSquare(g, 0 + j * CELL_SIZE, boardTop + i * CELL_SIZE, shape);
            }
        }

        if (curPiece.getShape() != Piece.Tetrominoes.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, 0 + x * CELL_SIZE, boardTop + (BOARD_HEIGHT - y - 1) * CELL_SIZE, curPiece.getShape());
            }
        }
    }

    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1))
                break;
            --newY;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1))
            pieceDropped();
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++)
            board[i] = Piece.Tetrominoes.NoShape;
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }

        PIECES_COUNT++;

        if(PIECES_COUNT == 12) {
            parent.setVisible(false);
            parent.stopMusic();
            parent.dispose();
            new TransitDialog(parent);
            PPOMainMenu mainMenu = new PPOMainMenu();
        }

        removeFullLines();

        if (!isFallingFinished)
            newPiece();
    }

    private void newPiece() {
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Piece.Tetrominoes.NoShape);
            timer.stop();
            isStarted = false;
        }
    }

    private boolean tryMove(Piece newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;
            if (shapeAt(x, y) != Piece.Tetrominoes.NoShape)
                return false;
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Piece.Tetrominoes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++)
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            parent.updateScore(numFullLines * 100); // Update score
            isFallingFinished = true;
            curPiece.setShape(Piece.Tetrominoes.NoShape);
        }
    }

    private void drawSquare(Graphics g, int x, int y, Piece.Tetrominoes shape) {
        Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
                new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0) };

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + CELL_SIZE - 1, x, y);
        g.drawLine(x, y, x + CELL_SIZE - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + CELL_SIZE - 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1);
        g.drawLine(x + CELL_SIZE - 1, y + CELL_SIZE - 1, x + CELL_SIZE - 1, y + 1);
    }

    private Piece.Tetrominoes shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!isStarted || curPiece.getShape() == Piece.Tetrominoes.NoShape)
                return;

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P') {
                pause();
                return;
            }

            if (isPaused)
                return;

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    tryMove(curPiece, curX - 1, curY);
                    moveSoundPlayer.play();
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(curPiece, curX + 1, curY);
                    moveSoundPlayer.play();
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(curPiece.rotateRight(), curX, curY);
                    rotateSoundPlayer.play();
                    break;
                case KeyEvent.VK_UP:
                    tryMove(curPiece.rotateLeft(), curX, curY);
                    rotateSoundPlayer.play();
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    dropSoundPlayer.play();
                    break;
                case 'd':
                case 'D':
                    oneLineDown();
                    linefallSoundPlayer.play();
                    break;
            }
        }
    }
}
