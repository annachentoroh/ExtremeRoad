
/**
 * Клас ProfileManager відповідає за збереження та завантаження користувацьких профілів у файл.
 * 
 * Профілі серіалізуються у файл user_profiles.dat через стандартні Java-потоки.
 * Якщо файл не знайдено або пошкоджений, повертається порожній список профілів.
 *
 * Основні методи:
 * - saveProfiles(List<UserProfile>): зберігає список у файл
 * - loadProfiles(): завантажує список з файлу
 */
import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    // ! ЗМІНЕНО: Ім'я файлу для збереження списку профілів !
    private static final String PROFILE_FILE = "user_profiles.dat";

    // Зберегти список профілів у файл
    public static void saveProfiles(List<UserProfile> profiles) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROFILE_FILE))) {
            oos.writeObject(profiles);
            System.out.println("User profiles saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving user profiles: " + e.getMessage());
        }
    }

    // Завантажити список профілів з файлу
    // Повертає порожній список, якщо файл не знайдено або є помилка
    @SuppressWarnings("unchecked") // Приховуємо попередження про unchecked cast
    public static List<UserProfile> loadProfiles() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PROFILE_FILE))) {
            List<UserProfile> profiles = (List<UserProfile>) ois.readObject();
            System.out.println("User profiles loaded successfully.");
            return profiles;
        } catch (FileNotFoundException e) {
            System.out.println("User profiles file not found. Creating a new empty list.");
            return new ArrayList<>(); // Повертаємо порожній список, якщо файл не знайдено
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading user profiles: " + e.getMessage());
            // Якщо є помилка при десеріалізації (наприклад, через зміну serialVersionUID)
            // можна видалити старий файл і повернути порожній список, щоб уникнути циклу помилок.
            // new File(PROFILE_FILE).delete(); // Розкоментуй, якщо хочеш автоматично видаляти пошкоджені файли
            return new ArrayList<>();
        }
    }
}
