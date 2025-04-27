import java.util.List;

public class Question {
    private String text;
    private List<String> options;
    private int correctOption;

    public Question(String text, List<String> options, int correctOption) {
        this.text = text;
        this.options = options;
        this.correctOption = correctOption;
    }
    
    // Getters and Setters
    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    // Check if the answer is correct
    public boolean isCorrect(int selectedOption) {
        return selectedOption == correctOption;
    }
}

