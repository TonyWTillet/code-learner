import java.util.List;
import java.util.ArrayList;
public class TestQuiz {
    public static void main(String[] args) {
        // Créer un joueur
        Player player = new Player("Tony");


        List<Question> questions = new ArrayList<>();

        // Créer un quiz
        Quiz quiz = new Quiz("Quiz Java Débutant", questions);

        // Ajouter des questions
        quiz.addQuestion(
            "Que signifie 'JDK' en Java ?",
            List.of("Java Development Kit", "Java Deployment Key", "Just Download Kit", "Java Default Key"),
            0 // La bonne réponse est la première
        );

        quiz.addQuestion(
            "Quel mot-clé sert à créer une nouvelle instance d'une classe ?",
            List.of("create", "build", "new", "instance"),
            2 // Le bon choix est "new"
        );

        quiz.addQuestion(
            "Comment commence le point d'entrée d'un programme Java ?",
            List.of("start()", "run()", "main()", "init()"),
            2 // Le bon choix est "main()"
        );

        // Lancer le quiz
        quiz.play(player);

        // Afficher le profil du joueur à la fin
        player.displayProfile();
    }
}
