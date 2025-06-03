import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Image; // ! НОВИЙ ІМПОРТ !
import javax.swing.ImageIcon; // ! НОВИЙ ІМПОРТ !

public class Obstacle {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int lane;

    private Image image; // ! НОВА ЗМІННА: Зображення для перешкоди !
    private String type; // ! НОВА ЗМІННА: Тип перешкоди (наприклад, "car", "cone") !

    // Конструктор
    public Obstacle(int startX, int startY, int obstacleWidth, int obstacleHeight, int speed, int lane, String type) {
        this.x = startX;
        this.y = startY;
        this.width = obstacleWidth;
        this.height = obstacleHeight;
        this.speed = speed;
        this.lane = lane;
        this.type = type; // Встановлюємо тип

        // ! НОВИЙ БЛОК: Завантаження зображення відповідно до типу !
        try {
            // Залежно від типу перешкоди, завантажуємо відповідне зображення
            // Ти можеш додати більше типів і відповідних зображень тут
            if (type.equals("car")) {
                // Зміни "car.png" на назву твого файлу зображення для машини
                image = new ImageIcon(getClass().getResource("war1.png")).getImage();
            } else if (type.equals("cone")) {
                // Зміни "cone.png" на назву твого файлу зображення для конуса
                image = new ImageIcon(getClass().getResource("war1.png")).getImage();
                this.width = 180; // Конуси можуть бути меншими
                this.height = 180;
            } else {
                // Якщо тип невідомий або зображення відсутнє, можна залишити червоний квадрат
                // або використати зображення за замовчуванням
                image = null; // Немає зображення, буде малюватися прямокутник
            }
        } catch (Exception e) {
            System.err.println("Error loading obstacle image for type " + type + ": " + e.getMessage());
            image = null; // Якщо зображення не завантажилося, малюємо прямокутник
        }
    }

    // Оновлений метод draw
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null); // Малюємо зображення
        } else {
            // Якщо зображення немає, малюємо червоний прямокутник
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public void move() {
        y += speed;
    }

    public void updatePosition(int laneWidth, int laneOffset) {
        this.x = laneOffset + (lane * laneWidth) + (laneWidth / 2) - (width / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }

    // Геттери
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getSpeed() { return speed; }
    public int getLane() { return lane; }
    public String getType() { return type; } // ! НОВИЙ ГЕТТЕР !
}
