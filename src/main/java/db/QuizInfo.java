package db;

public class QuizInfo {
    private int id;
    private String title;
    private String language; // 🔹 Ajouté

    public QuizInfo(int id, String title, String language) {
        this.id = id;
        this.title = title;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }
}
