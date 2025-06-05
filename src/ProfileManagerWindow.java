
/**
 * ProfileManagerWindow — це модальне вікно для перегляду, створення, редагування, вибору та видалення профілів користувачів.
 *
 * Основні функції:
 * - Виводить список профілів ліворуч і детальну інформацію праворуч.
 * - Дозволяє додавати фото до профілю.
 * - Підтримує редагування та видалення з підтвердженням.
 * - Зберігає зміни через ProfileManager.
 *
 * Стилізоване за допомогою кольорів, шрифтів і власного градієнтного фону.
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ProfileManagerWindow extends JDialog {
    private Main mainFrame;
    private List<UserProfile> profiles;
    private DefaultListModel<String> profileListModel;
    private JList<String> profilesJList;
    private JLabel selectedProfileInfoLabel;
    private JLabel selectedProfilePhotoLabel;
    private UserProfile currentlySelectedProfile;

    public ProfileManagerWindow(Main mainFrame, List<UserProfile> profiles) {
        super(mainFrame, "Manage Profiles", true);
        this.mainFrame = mainFrame;
        this.profiles = profiles;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(mainFrame);

        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(10, 10, 30),
                        getWidth(), getHeight(), new Color(20, 20, 60)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        });
        getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        leftPanel.setOpaque(false);

        profileListModel = new DefaultListModel<>();
        profilesJList = new JList<>(profileListModel);
        profilesJList.setFont(new Font("Consolas", Font.BOLD, 16));
        profilesJList.setBackground(new Color(20, 20, 30));
        profilesJList.setForeground(new Color(0, 255, 255));
        profilesJList.setSelectionBackground(new Color(0, 100, 200));
        profilesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        profilesJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedProfileDisplay();
            }
        });

        JScrollPane scrollPane = new JScrollPane(profilesJList);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionButtonsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        actionButtonsPanel.setOpaque(false);
        JButton createButton = createActionButton("Create New");
        JButton selectButton = createActionButton("Select Profile");
        JButton editButton = createActionButton("Edit Profile");
        JButton deleteButton = createActionButton("Delete Profile");
        createButton.addActionListener(e -> createNewProfile());
        selectButton.addActionListener(e -> selectProfile());
        editButton.addActionListener(e -> editProfile());
        deleteButton.addActionListener(e -> deleteProfile());
        actionButtonsPanel.add(createButton);
        actionButtonsPanel.add(selectButton);
        actionButtonsPanel.add(editButton);
        actionButtonsPanel.add(deleteButton);
        leftPanel.add(actionButtonsPanel, BorderLayout.SOUTH);
        getContentPane().add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        rightPanel.setOpaque(false);

        selectedProfilePhotoLabel = new JLabel();
        selectedProfilePhotoLabel.setPreferredSize(new Dimension(150, 150));
        selectedProfilePhotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectedProfilePhotoLabel.setVerticalAlignment(SwingConstants.CENTER);
        selectedProfilePhotoLabel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
        selectedProfilePhotoLabel.setBackground(new Color(50, 0, 70));
        selectedProfilePhotoLabel.setOpaque(true);

        selectedProfileInfoLabel = new JLabel("Select a profile to view details.");
        selectedProfileInfoLabel.setFont(new Font("Consolas", Font.PLAIN, 16));
        selectedProfileInfoLabel.setForeground(new Color(173, 216, 230));
        selectedProfileInfoLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel photoAndInfoPanel = new JPanel(new BorderLayout(10, 10));
        photoAndInfoPanel.setOpaque(false);
        photoAndInfoPanel.add(selectedProfilePhotoLabel, BorderLayout.NORTH);
        photoAndInfoPanel.add(selectedProfileInfoLabel, BorderLayout.CENTER);

        rightPanel.add(photoAndInfoPanel, BorderLayout.CENTER);

        JButton closeButton = createActionButton("Close");
        closeButton.addActionListener(e -> dispose());
        rightPanel.add(closeButton, BorderLayout.SOUTH);

        getContentPane().add(rightPanel, BorderLayout.CENTER);

        updateProfileList();
        if (!profiles.isEmpty()) {
            profilesJList.setSelectedIndex(0);
        }
        updateSelectedProfileDisplay();
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Consolas", Font.BOLD, 16));
        button.setForeground(Color.CYAN);
        button.setBackground(new Color(30, 30, 60));
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void updateProfileList() {
        profileListModel.clear();
        for (UserProfile profile : profiles) {
            profileListModel.addElement(profile.getName() + " " + profile.getSurname());
        }
    }

    private void updateSelectedProfileDisplay() {
        int selectedIndex = profilesJList.getSelectedIndex();
        if (selectedIndex != -1) {
            currentlySelectedProfile = profiles.get(selectedIndex);
            selectedProfileInfoLabel.setText("<html>" +
                    "<b>Name:</b> " + currentlySelectedProfile.getName() + "<br>" +
                    "<b>Surname:</b> " + currentlySelectedProfile.getSurname() + "<br>" +
                    "<b>Email:</b> " + currentlySelectedProfile.getEmail() + "<br>" +
                    "<b>High Score:</b> <font color='yellow'>" + currentlySelectedProfile.getHighScore() + "</font><br>" +
                    "<b>Total Coins:</b> <font color='orange'>" + currentlySelectedProfile.getTotalCoinsCollected() + "</font>" +
                    "</html>");

            if (currentlySelectedProfile.getPhotoPath() != null && !currentlySelectedProfile.getPhotoPath().isEmpty()) {
                ImageIcon icon = new ImageIcon(currentlySelectedProfile.getPhotoPath());
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    Image image = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                    selectedProfilePhotoLabel.setIcon(new ImageIcon(image));
                } else {
                    selectedProfilePhotoLabel.setIcon(null);
                    selectedProfilePhotoLabel.setText("No Photo");
                }
            } else {
                selectedProfilePhotoLabel.setIcon(null);
                selectedProfilePhotoLabel.setText("No Photo");
            }
        } else {
            currentlySelectedProfile = null;
            selectedProfileInfoLabel.setText("Select a profile to view details.");
            selectedProfilePhotoLabel.setIcon(null);
            selectedProfilePhotoLabel.setText("");
        }
    }

    // Стилізований метод створення кнопок для внутрішніх діалогів
    private JButton createStyledInnerButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        button.setForeground(Color.CYAN);
        button.setBackground(new Color(55, 55, 113));
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        button.setFocusPainted(false);
        return button;
    }

    // Оновлений метод створення профілю
    private void createNewProfile() {
        JTextField nameField = new JTextField(15);
        JTextField surnameField = new JTextField(15);
        JTextField emailField = new JTextField(15);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBackground(new Color(25, 25, 45));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Surname:"));
        inputPanel.add(surnameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        String photoPath = null;
        JButton choosePhotoButton = createStyledInnerButton("Choose Photo...");
        JLabel photoPreviewLabel = new JLabel("No photo selected");
        photoPreviewLabel.setPreferredSize(new Dimension(80, 80));
        photoPreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoPreviewLabel.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        choosePhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Profile Photo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String chosenPhotoPath = selectedFile.getAbsolutePath();
                ImageIcon tempIcon = new ImageIcon(chosenPhotoPath);
                if (tempIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    Image img = tempIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    photoPreviewLabel.setIcon(new ImageIcon(img));
                    photoPreviewLabel.setText("");
                } else {
                    photoPreviewLabel.setIcon(null);
                    photoPreviewLabel.setText("Invalid Image");
                }
                inputPanel.putClientProperty("photoPath", chosenPhotoPath);
            }
        });

        inputPanel.add(choosePhotoButton);
        inputPanel.add(photoPreviewLabel);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Create New Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();
            String chosenPhotoPath = (String) inputPanel.getClientProperty("photoPath");

            if (!name.isEmpty()) {
                UserProfile newProfile = new UserProfile(name, surname, email);
                newProfile.setPhotoPath(chosenPhotoPath);
                profiles.add(newProfile);
                mainFrame.setCurrentUserProfile(newProfile);
                ProfileManager.saveProfiles(profiles);
                updateProfileList();
                profilesJList.setSelectedValue(newProfile.getName() + " " + newProfile.getSurname(), true);
                JOptionPane.showMessageDialog(this, "Profile '" + name + "' created and selected!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Оновлений метод редагування профілю
    private void editProfile() {
        if (currentlySelectedProfile == null) {
            JOptionPane.showMessageDialog(this, "Please select a profile to edit.", "No Profile Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField(currentlySelectedProfile.getName(), 15);
        JTextField surnameField = new JTextField(currentlySelectedProfile.getSurname(), 15);
        JTextField emailField = new JTextField(currentlySelectedProfile.getEmail(), 15);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBackground(new Color(25, 25, 45));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Surname:"));
        inputPanel.add(surnameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);

        String currentPhotoPath = currentlySelectedProfile.getPhotoPath();
        JButton choosePhotoButton = createStyledInnerButton("Change Photo...");
        JLabel photoPreviewLabel = new JLabel();
        photoPreviewLabel.setPreferredSize(new Dimension(80, 80));
        photoPreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoPreviewLabel.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        if (currentPhotoPath != null && !currentPhotoPath.isEmpty()) {
            ImageIcon tempIcon = new ImageIcon(currentPhotoPath);
            if (tempIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = tempIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                photoPreviewLabel.setIcon(new ImageIcon(img));
                photoPreviewLabel.setText("");
            } else {
                photoPreviewLabel.setIcon(null);
                photoPreviewLabel.setText("Invalid Photo");
            }
        } else {
            photoPreviewLabel.setIcon(null);
            photoPreviewLabel.setText("No Photo");
        }

        choosePhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select New Profile Photo");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String chosenPhotoPath = selectedFile.getAbsolutePath();
                ImageIcon tempIcon = new ImageIcon(chosenPhotoPath);
                if (tempIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    Image img = tempIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    photoPreviewLabel.setIcon(new ImageIcon(img));
                    photoPreviewLabel.setText("");
                } else {
                    photoPreviewLabel.setIcon(null);
                    photoPreviewLabel.setText("Invalid Image");
                }
                inputPanel.putClientProperty("photoPath", chosenPhotoPath);
            }
        });

        inputPanel.add(choosePhotoButton);
        inputPanel.add(photoPreviewLabel);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Edit Profile: " + currentlySelectedProfile.getName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newSurname = surnameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPhotoPath = (String) inputPanel.getClientProperty("photoPath");
            if (newPhotoPath == null) {
                newPhotoPath = currentlySelectedProfile.getPhotoPath();
            }

            if (!newName.isEmpty()) {
                currentlySelectedProfile.setName(newName);
                currentlySelectedProfile.setSurname(newSurname);
                currentlySelectedProfile.setEmail(newEmail);
                currentlySelectedProfile.setPhotoPath(newPhotoPath);
                ProfileManager.saveProfiles(profiles);
                updateProfileList();
                updateSelectedProfileDisplay();
                JOptionPane.showMessageDialog(this, "Profile '" + newName + "' updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Оновлений метод вибору профілю
    private void selectProfile() {
        if (currentlySelectedProfile != null) {
            mainFrame.setCurrentUserProfile(currentlySelectedProfile);

            JPanel msgPanel = new JPanel();
            msgPanel.setBackground(new Color(25, 25, 45));
            JLabel msgLabel = new JLabel("Profile '" + currentlySelectedProfile.getName() + "' selected for game!");
            msgLabel.setForeground(Color.CYAN);
            msgLabel.setFont(new Font("Consolas", Font.BOLD, 14));
            msgPanel.add(msgLabel);

            JOptionPane.showMessageDialog(this, msgPanel, "Profile Selected", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JPanel errorPanel = new JPanel();
            errorPanel.setBackground(new Color(45, 20, 20));
            JLabel errorLabel = new JLabel("Please select a profile first.");
            errorLabel.setForeground(Color.ORANGE);
            errorLabel.setFont(new Font("Consolas", Font.BOLD, 14));
            errorPanel.add(errorLabel);

            JOptionPane.showMessageDialog(this, errorPanel, "No Profile Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Оновлений метод видалення профілю
    private void deleteProfile() {
        if (currentlySelectedProfile == null) {
            JPanel errorPanel = new JPanel();
            errorPanel.setBackground(new Color(45, 20, 20));
            JLabel errorLabel = new JLabel("Please select a profile to delete.");
            errorLabel.setForeground(Color.ORANGE);
            errorLabel.setFont(new Font("Consolas", Font.BOLD, 14));
            errorPanel.add(errorLabel);

            JOptionPane.showMessageDialog(this, errorPanel, "No Profile Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel confirmPanel = new JPanel();
        confirmPanel.setBackground(new Color(25, 25, 45));
        JLabel confirmLabel = new JLabel("Are you sure you want to delete profile '" + currentlySelectedProfile.getName() + "'?");
        confirmLabel.setForeground(Color.CYAN);
        confirmLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        confirmPanel.add(confirmLabel);

        int confirm = JOptionPane.showConfirmDialog(this, confirmPanel,
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            profiles.remove(currentlySelectedProfile);
            ProfileManager.saveProfiles(profiles);
            updateProfileList();
            if (mainFrame.getCurrentUserProfile() == currentlySelectedProfile) {
                mainFrame.setCurrentUserProfile(null);
            }
            currentlySelectedProfile = null;
            selectedProfileInfoLabel.setText("Select a profile to view details.");
            selectedProfilePhotoLabel.setIcon(null);
            selectedProfilePhotoLabel.setText("");

            JPanel successPanel = new JPanel();
            successPanel.setBackground(new Color(25, 25, 45));
            JLabel successLabel = new JLabel("Profile deleted successfully.");
            successLabel.setForeground(Color.GREEN);
            successLabel.setFont(new Font("Consolas", Font.BOLD, 14));
            successPanel.add(successLabel);

            JOptionPane.showMessageDialog(this, successPanel, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
