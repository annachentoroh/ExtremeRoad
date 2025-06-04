import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenuPanel extends JPanel {
    private Main mainFrame; // Посилання на головний фрейм (Main)
    private JLabel gameTitleLabel;
    private JButton startGameButton;
    private JButton profileButton;
    private JButton instructionsButton; // New button for instructions
    private JButton exitButton;

    public MainMenuPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 🔷 Градієнтний фон
        setOpaque(false);

        // 🔹 Панель кнопок по центру
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(150, 150, 150, 150));

        // 🔹 Назва гри
        gameTitleLabel = new JLabel("EXTREME ROAD", SwingConstants.CENTER);
        gameTitleLabel.setFont(new Font("Consolas", Font.BOLD, 48));
        gameTitleLabel.setForeground(Color.CYAN);
        gameTitleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(gameTitleLabel, BorderLayout.NORTH);

        // 🔹 Кнопки
        startGameButton = createMenuButton("Start Game");
        profileButton = createMenuButton("Profile");
        instructionsButton = createMenuButton("Instructions");
        exitButton = createMenuButton("Exit");

        startGameButton.addActionListener(e -> mainFrame.startGame());
        profileButton.addActionListener(e -> mainFrame.showProfile());
        instructionsButton.addActionListener(e -> showInstructions());
        exitButton.addActionListener(e -> mainFrame.exitGame());

        buttonPanel.add(startGameButton);
        buttonPanel.add(profileButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(10, 10, 30),
                getWidth(), getHeight(), new Color(82, 82, 181)
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Consolas", Font.BOLD, 26));
        button.setForeground(Color.CYAN);
        button.setBackground(new Color(20, 20, 40));
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Method to display game instructions
    private void showInstructions() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Game Instructions", true);
        dialog.setSize(800, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0, 51, 102)); // Насичений синій фон
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("<html><body style='color: white; font-family: sans-serif;'>"
                + "<h2 style='text-align: center;'>Game Instructions</h2>"
                + "<p>Welcome to <strong>EXTREME ROAD</strong>! Navigate your player through endless lanes, avoid obstacles, and collect coins and power-ups.</p>"
                + "<h3>Controls:</h3>"
                + "<ul>"
                + "<li><strong>Left Arrow:</strong> Move player left.</li>"
                + "<li><strong>Right Arrow:</strong> Move player right.</li>"
                + "<li><strong>R (Game Over Screen):</strong> Restart game.</li>"
                + "</ul>"
                + "<h3>Gameplay:</h3>"
                + "<ul>"
                + "<li>Avoid hitting obstacles like cars and cones. Colliding with them ends the game.</li>"
                + "<li>Collect <strong>Coins</strong> to increase your score.</li>"
                + "<li>Pick up <strong>Shields</strong> to gain temporary invulnerability, allowing you to pass through obstacles safely.</li>"
                + "<li>The game speed and obstacle frequency will gradually increase as your score gets higher, making it more challenging!</li>"
                + "</ul>"
                + "<p style='text-align: center; font-style: italic;'>Good luck and have fun!</p>"
                + "</body></html>");
        label.setForeground(Color.WHITE);
        label.setVerticalAlignment(SwingConstants.TOP);

        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(0, 51, 102)); // Той самий фон для скролу
        scrollPane.setOpaque(false);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Consolas", Font.BOLD, 18));
        okButton.setBackground(new Color(0, 102, 204));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 51, 102));
        buttonPanel.add(okButton);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

}




