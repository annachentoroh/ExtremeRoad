import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

public class Player {
    private int x, y, width, height;
    private int currentLane; // 0 for left, 1 for middle, 2 for right
    private Image playerImage;

    private boolean invulnerable;
    private long invulnerabilityStartTime;
    private long invulnerabilityDuration;

    private int laneWidth;
    private int laneOffset;

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentLane = 1; // Start in middle lane
        this.invulnerable = false;
        this.invulnerabilityStartTime = 0;
        this.invulnerabilityDuration = 0;

        try {
            playerImage = new ImageIcon(getClass().getResource("person.png")).getImage();
        } catch (Exception e) {
            System.err.println("Error loading player image: " + e.getMessage());
        }
    }

    // --- ЗМІНЕНІ МЕТОДИ РУХУ (ПРОСТА ЛОГІКА: КРОК ЗА КРОКОМ) ---
    public void moveLeft() {
        if (currentLane > 0) { // Переміщуємо на одну смугу вліво, якщо не на крайній лівій
            currentLane--;
        }
        // Після зміни смуги, оновлюємо x-координату
        updatePosition(laneWidth, laneOffset);
    }

    public void moveRight() {
        if (currentLane < 2) { // Переміщуємо на одну смугу вправо, якщо не на крайній правій
            currentLane++;
        }
        // Після зміни смуги, оновлюємо x-координату
        updatePosition(laneWidth, laneOffset);
    }

    // --- МЕТОД updatePosition БЕЗ ЗМІН, але тепер викликається з moveLeft/moveRight ---
    public void updatePosition(int laneWidth, int laneOffset) {
        this.laneWidth = laneWidth;
        this.laneOffset = laneOffset;
        this.x = laneOffset + (currentLane * laneWidth) + (laneWidth - width) / 2;
    }

    // --- МЕТОД moveLane(int direction) ВИДАЛЕНО З Player.java ---
    // Це було джерелом плутанини.

    public void draw(Graphics g) {
        if (playerImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            if (invulnerable) {
                float alpha = (float) (Math.sin(System.currentTimeMillis() / 100.0) * 0.25 + 0.75);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }
            g2d.drawImage(playerImage, x, y, width, height, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void activateInvulnerability(long duration) {
        this.invulnerable = true;
        this.invulnerabilityStartTime = System.currentTimeMillis();
        this.invulnerabilityDuration = duration;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void updateInvulnerability() {
        if (invulnerable) {
            if (System.currentTimeMillis() - invulnerabilityStartTime >= invulnerabilityDuration) {
                invulnerable = false;
                System.out.println("Invulnerability ended.");
            }
        }
    }

    public long getInvulnerabilityRemainingTime() {
        if (invulnerable) {
            long elapsedTime = System.currentTimeMillis() - invulnerabilityStartTime;
            long remainingTime = invulnerabilityDuration - elapsedTime;
            return Math.max(0, remainingTime);
        }
        return 0;
    }

    // Геттер для поточної смуги
    public int getCurrentLane() {
        return currentLane;
    }

    // Сеттер для поточної смуги (будемо використовувати в GamePanel)
    public void setCurrentLane(int newLane) {
        this.currentLane = newLane;
    }
}




/*import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.AlphaComposite; // Для прозорості, якщо хочемо мигання
import java.awt.Graphics2D;

public class Player {
    private int x, y, width, height;
    private int currentLane; // 0 for left, 1 for middle, 2 for right
    private Image playerImage;

    // ! НОВІ ЗМІННІ ДЛЯ НЕВРАЗЛИВОСТІ !
    private boolean invulnerable;
    private long invulnerabilityStartTime;
    private long invulnerabilityDuration; // Тривалість невразливості в мс

    private int laneWidth; // Ширина однієї смуги
    private int laneOffset; // Відступ від лівого краю до першої смуги

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.currentLane = 1; // Start in middle lane
        this.invulnerable = false; // Початково не невразливий
        this.invulnerabilityStartTime = 0;
        this.invulnerabilityDuration = 0;

        try {
            playerImage = new ImageIcon(getClass().getResource("person.png")).getImage();
        } catch (Exception e) {
            System.err.println("Error loading player image: " + e.getMessage());
        }
    }

    public void moveLeft() {
        if (currentLane > 0) {
            currentLane--;
        }
    }

    public void moveRight() {
        if (currentLane < 2) {
            currentLane++;
        }
    }

    //public void updatePosition(int laneWidth, int laneOffset) {
    //    this.x = laneOffset + (currentLane * laneWidth) + (laneWidth / 2) - (width / 2);
    //}

    public void updatePosition(int laneWidth, int laneOffset) {
        this.laneWidth = laneWidth;
        this.laneOffset = laneOffset;
        // Перераховуємо x-координату гравця на основі поточної смуги
        this.x = laneOffset + (currentLane * laneWidth) + (laneWidth - width) / 2;
    }

    // ! НОВИЙ МЕТОД: для ЗМІНИ СМУГИ гравця ззовні (з GamePanel) !
    public void moveLane(int direction) { // direction: -1 for left, 1 for right
        int originalLane = currentLane;
        if (direction == -1 && currentLane > 0) {
            currentLane--;
        } else if (direction == 1 && currentLane < 2) {
            currentLane++;
        }
        // Після зміни смуги, оновлюємо x-координату
        if (currentLane != originalLane) {
            this.x = laneOffset + (currentLane * laneWidth) + (laneWidth - width) / 2;
        }
    }

    public void draw(Graphics g) {
        if (playerImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            if (invulnerable) {
                // ! Мигання гравця під час невразливості !
                float alpha = (float) (Math.sin(System.currentTimeMillis() / 100.0) * 0.25 + 0.75); // Від 0.5 до 1.0
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }
            g2d.drawImage(playerImage, x, y, width, height, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Скидаємо прозорість
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // ! НОВІ МЕТОДИ ДЛЯ НЕВРАЗЛИВОСТІ !
    public void activateInvulnerability(long duration) {
        this.invulnerable = true;
        this.invulnerabilityStartTime = System.currentTimeMillis();
        this.invulnerabilityDuration = duration;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void updateInvulnerability() {
        if (invulnerable) {
            if (System.currentTimeMillis() - invulnerabilityStartTime >= invulnerabilityDuration) {
                invulnerable = false; // Невразливість закінчилася
                System.out.println("Invulnerability ended.");
            }
        }
    }
    // ! НОВИЙ МЕТОД: Повернути час, що залишився до закінчення невразливості !
    public long getInvulnerabilityRemainingTime() {
        if (invulnerable) {
            long elapsedTime = System.currentTimeMillis() - invulnerabilityStartTime;
            long remainingTime = invulnerabilityDuration - elapsedTime;
            return Math.max(0, remainingTime); // Повертаємо 0, якщо час вже вийшов
        }
        return 0; // Не невразливий, повертаємо 0
    }
}*/
