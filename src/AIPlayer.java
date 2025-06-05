import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Клас AIPlayer представляє ігрового бота, який автоматично ухиляється від перешкод на доріжці.
 */
public class AIPlayer {
    private int x;
    private int y;
    private int width;
    private int height;
    private int lane;

    /**
     * Створює нового бота на заданій позиції з вказаними розмірами.
     *
     * @param startX        початкова координата X
     * @param startY        початкова координата Y
     * @param playerWidth   ширина бота
     * @param playerHeight  висота бота
     */
    public AIPlayer(int startX, int startY, int playerWidth, int playerHeight) {
        this.x = startX;
        this.y = startY;
        this.width = playerWidth;
        this.height = playerHeight;
        this.lane = 1; // Центральна доріжка
    }

    /**
     * Малює бота на екрані.
     *
     * @param g графічний контекст
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }

    /**
     * Оновлює позицію бота по X залежно від доріжки.
     *
     * @param laneWidth   ширина доріжки
     * @param laneOffset  зсув початку доріжок
     */
    public void updatePosition(int laneWidth, int laneOffset) {
        this.x = laneOffset + (lane * laneWidth) + (laneWidth / 2) - (width / 2);
    }

    /**
     * Приймає рішення щодо руху, щоб уникати перешкод.
     *
     * @param obstacles     список поточних перешкод
     * @param laneWidth     ширина доріжки
     * @param laneOffset    зсув початку доріжок
     * @param panelHeight   висота панелі (для визначення меж видимості)
     */
    public void makeDecision(ArrayList<Obstacle> obstacles, int laneWidth, int laneOffset, int panelHeight) {
        Obstacle obstacleInMyLane = null;
        Obstacle obstacleToMyLeft = null;
        Obstacle obstacleToMyRight = null;

        for (Obstacle obs : obstacles) {
            if (obs.getY() > this.y - 300 && obs.getY() < this.y + this.height) {
                if (obs.getLane() == this.lane) {
                    obstacleInMyLane = obs;
                } else if (obs.getLane() == this.lane - 1 && this.lane > 0) {
                    obstacleToMyLeft = obs;
                } else if (obs.getLane() == this.lane + 1 && this.lane < 2) {
                    obstacleToMyRight = obs;
                }
            }
        }

        if (obstacleInMyLane != null) {
            if (this.lane == 1) {
                if (obstacleToMyLeft == null) {
                    moveLeft();
                } else if (obstacleToMyRight == null) {
                    moveRight();
                }
            } else if (this.lane == 0 && obstacleToMyRight == null) {
                moveRight();
            } else if (this.lane == 2 && obstacleToMyLeft == null) {
                moveLeft();
            }
        }
    }

    /** Зміщує бота на одну доріжку вліво, якщо це можливо. */
    public void moveLeft() {
        if (lane > 0) {
            lane--;
        }
    }

    /** Зміщує бота на одну доріжку вправо, якщо це можливо. */
    public void moveRight() {
        if (lane < 2) {
            lane++;
        }
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public int getLane() { return lane; }

    /**
     * Повертає прямокутник, що представляє хітбокс бота.
     *
     * @return Rectangle, що описує розміри і позицію бота
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}