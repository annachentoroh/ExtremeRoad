import java.io.Serializable;

public class UserProfile implements Serializable {
    private static final long serialVersionUID = 2L; // ! ЗМІНЕНО: Збільшуємо serialVersionUID !

    private String name;
    private String surname;
    private String email;
    private int highScore;
    private long totalCoinsCollected;
    private String photoPath; // ! НОВЕ ПОЛЕ: шлях до фотографії !

    public UserProfile(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.highScore = 0;
        this.totalCoinsCollected = 0;
        this.photoPath = null; // За замовчуванням фото немає
    }

    // --- Геттери ---
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getEmail() { return email; }
    public int getHighScore() { return highScore; }
    public long getTotalCoinsCollected() { return totalCoinsCollected; }
    public String getPhotoPath() { return photoPath; } // ! НОВИЙ ГЕТТЕР !

    // --- Сеттери ---
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEmail(String email) { this.email = email; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; } // ! НОВИЙ СЕТТЕР !

    public void updateHighScore(int newScore) {
        if (newScore > this.highScore) {
            this.highScore = newScore;
        }
    }

    public void addCoins(int coins) {
        this.totalCoinsCollected += coins;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Surname: " + surname + "\n" +
                "Email: " + email + "\n" +
                "High Score: " + highScore + "\n" +
                "Total Coins: " + totalCoinsCollected + "\n" +
                "Photo Path: " + (photoPath != null ? photoPath : "N/A");
    }
}
