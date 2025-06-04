import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Color; // Додано для резервного кольору

public class Collectable {
    private int x, y, width, height;
    private int speed;
    private int lane; // Доріжка, на якій знаходиться об'єкт
    private String type; // "coin", "shield", "heart", "double_coins" (новий тип)
    private Image image;

    public Collectable(int x, int y, int width, int height, int speed, int lane, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.lane = lane;
        this.type = type;

        // Завантажуємо відповідне зображення в залежності від типу
        try {
            if (type.equals("coin")) {
                image = new ImageIcon(getClass().getResource("coin.png")).getImage();
            } else if (type.equals("shield")) {
                image = new ImageIcon(getClass().getResource("shield.png")).getImage();
            } else if (type.equals("heart")) { // Якщо у тебе є серце як collectable
                image = new ImageIcon(getClass().getResource("heart.png")).getImage();
            } else if (type.equals("double_coins")) { // --- НОВЕ: Для X2 бонусу ---
                image = new ImageIcon(getClass().getResource("double_coins.png")).getImage(); // Зображення для X2 бонусу
            } else {
                System.err.println("Unknown collectable type: " + type);
                image = null; // Встановити null, якщо тип невідомий
            }
        } catch (Exception e) {
            System.err.println("Error loading image for " + type + ": " + e.getMessage());
            image = null; // Встановити null, якщо зображення не завантажилося
        }
    }

    public void move() {
        y += speed; // Об'єкт рухається вниз екраном
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            // Резервний варіант, якщо зображення не завантажилося.
            // Малюємо прямокутник з кольором та можливим текстом.
            switch (type) {
                case "coin":
                    g.setColor(Color.YELLOW);
                    break;
                case "shield":
                    g.setColor(Color.BLUE);
                    break;
                case "heart":
                    g.setColor(Color.RED);
                    break;
                case "double_coins": // --- НОВЕ: Резервний колір та текст для X2 бонусу ---
                    g.setColor(Color.MAGENTA); // Яскравий колір для X2
                    break;
                default:
                    g.setColor(Color.GRAY); // За замовчуванням
            }
            g.fillRect(x, y, width, height);

            if (type.equals("double_coins")) {
                g.setColor(Color.WHITE); // Колір тексту
                g.drawString("X2", x + width / 4, y + height / 2 + 5); // Додаємо "X2" текст
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isOffScreen(int panelHeight) {
        return y > panelHeight;
    }

    public void updatePosition(int laneWidth, int laneOffset) {
        // Перераховуємо X-координату на основі доріжки
        this.x = laneOffset + (lane * laneWidth) + (laneWidth / 2) - (width / 2);
    }

    public String getType() {
        return type;
    }
}