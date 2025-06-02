import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import java.util.ArrayList; // ! НОВИЙ ІМПОРТ !
import java.util.List;     // ! НОВИЙ ІМПОРТ !

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private GamePanel gamePanel;
    private MainMenuPanel mainMenuPanel;

    private List<UserProfile> userProfiles; // ! ЗМІНЕНО: Тепер це список профілів !
    private UserProfile currentlyActiveProfile; // ! НОВЕ: Поточний активний профіль для гри !

    public Main() {
        super("Extreme Road");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(740, 700);
        setResizable(false);
        setLocationRelativeTo(null);

        // ! ЗАВАНТАЖЕННЯ СПИСКУ ПРОФІЛІВ !
        userProfiles = ProfileManager.loadProfiles();
        if (userProfiles.isEmpty()) {
            // Якщо профілів немає, створюємо початковий "Гість" профіль
            UserProfile guestProfile = new UserProfile("Гість", "Гравець", "guest@extreme.road");
            userProfiles.add(guestProfile);
            ProfileManager.saveProfiles(userProfiles);
            System.out.println("Список профілів порожній. Створено початковий профіль: " + guestProfile.getName());
        }
        // За замовчуванням активуємо перший профіль зі списку (або "Гість", якщо він щойно створений)
        currentlyActiveProfile = userProfiles.get(0);
        System.out.println("Активний профіль при старті: " + currentlyActiveProfile.getName());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gamePanel = new GamePanel();
        gamePanel.setCurrentUserProfile(currentlyActiveProfile); // Передаємо вибраний профіль в GamePanel!
        gamePanel.setMainFrame(this);
        mainMenuPanel = new MainMenuPanel(this);

        mainPanel.add(mainMenuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        add(mainPanel);

        // Показуємо меню при старті
        cardLayout.show(mainPanel, "Menu");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Зберігаємо ВЕСЬ СПИСОК профілів перед виходом
                ProfileManager.saveProfiles(userProfiles);
                System.out.println("Профілі користувачів збережено при закритті вікна.");
            }
        });
    }

    // Метод для переходу до гри
    public void startGame() {
        if (currentlyActiveProfile == null) {
            JOptionPane.showMessageDialog(this, "Будь ласка, виберіть профіль перед початком гри.", "Немає вибраного профілю", JOptionPane.WARNING_MESSAGE);
            showProfile(); // Відкриваємо менеджер профілів, щоб користувач міг вибрати
            return;
        }
        // Передаємо саме АКТИВНИЙ профіль в GamePanel перед стартом
        gamePanel.setCurrentUserProfile(currentlyActiveProfile);
        cardLayout.show(mainPanel, "Game");
        gamePanel.resetGame(); // Скидаємо гру та запускаємо її
        gamePanel.requestFocusInWindow(); // Для керування гравцем!
        System.out.println("Гра почалася з профілем: " + currentlyActiveProfile.getName());
    }

    // Метод для відображення вікна менеджера профілів
    public void showProfile() {
        // Зупиняємо гру та музику, якщо вона зараз іде
        gamePanel.getGameTimer().stop();
        if (gamePanel.getBackgroundMusic() != null) {
            gamePanel.getBackgroundMusic().stop();
        }

        // Передаємо ВЕСЬ СПИСОК профілів у менеджер профілів
        ProfileManagerWindow profileWindow = new ProfileManagerWindow(this, userProfiles);
        profileWindow.setVisible(true);

        // Після закриття вікна профілю:
        // Зберігаємо оновлений список профілів (на випадок створення/редагування/видалення)
        ProfileManager.saveProfiles(userProfiles);

        // Поновлюємо гру/музику, якщо повернулися до гри
        if (gamePanel.isVisible() && !gamePanel.isGameOver()) { // Якщо GamePanel зараз відображається
            gamePanel.getGameTimer().start();
            if (gamePanel.getBackgroundMusic() != null) {
                gamePanel.getBackgroundMusic().playLoop();
            }
            gamePanel.requestFocusInWindow();
        } else {
            // Якщо були в меню або гра завершена, повертаємося до меню
            cardLayout.show(mainPanel, "Menu");
        }
    }

    // ! НОВИЙ МЕТОД: Для повернення до головного меню !
    public void showMenu() {
        // Зупиняємо гру, якщо вона йде
        gamePanel.getGameTimer().stop();
        if (gamePanel.getBackgroundMusic() != null) {
            gamePanel.getBackgroundMusic().stop();
        }

        // Переключаємося на панель меню
        cardLayout.show(mainPanel, "Menu");
        System.out.println("Повернення до головного меню.");
    }

    // Метод для виходу з гри
    public void exitGame() {
        // Зберігаємо профіль перед виходом (якщо не було збережено раніше)
        ProfileManager.saveProfiles(userProfiles);
        System.out.println("Вихід з гри.");
        System.exit(0);
    }

    // Геттер для поточного активного профілю (для MainMenuPanel)
    public UserProfile getCurrentUserProfile() {
        return currentlyActiveProfile;
    }

    // Сеттер для встановлення поточного активного профілю (викликається з ProfileManagerWindow)
    public void setCurrentUserProfile(UserProfile profile) {
        this.currentlyActiveProfile = profile;
        // Оновлюємо профіль і в GamePanel, якщо GamePanel вже існує
        if (gamePanel != null) {
            gamePanel.setCurrentUserProfile(currentlyActiveProfile);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main game = new Main();
            game.setVisible(true);
        });
    }
}