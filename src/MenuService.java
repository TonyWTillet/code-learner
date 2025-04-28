import java.util.Scanner;
import java.util.List;
public class MenuService {

    public static void showLanguageMenu(Player player) {
        Scanner scanner = new Scanner(System.in);

        boolean selectingLanguage = true;
        while (selectingLanguage) {
            System.out.println("\n Choisissez votre langage :");
            System.out.println("1. Java");
            System.out.println("2. Python");
            System.out.println("3. C++");
            System.out.println("4. Retour au menu principal");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            String language = "";

            switch (choice) {
                case 1:
                    language = "Java";
                    selectingLanguage = false;
                    break;
                case 2:
                    language = "Python";
                    selectingLanguage = false;
                    break;
                case 3:
                    language = "C++";
                    selectingLanguage = false;
                    break;
                case 4:
                    System.out.println("Retour au menu principal...");
                    return; // retourne sans continuer
                default:
                    System.out.println("Option invalide. Réessayez.");
            }
            if (!language.isEmpty()) {
                showActivityMenu(player, language);
            }
        }

        scanner.close();
    }

    private static void showActivityMenu(Player player, String language) {
        Scanner scanner = new Scanner(System.in);

        boolean selectingActivity = true;
        while (selectingActivity) {
            System.out.println("\nChoisissez votre type d'activité pour " + language + " :");
            System.out.println("1. Quiz");
            System.out.println("2. Mini-projet (non disponible pour l'instant)");
            System.out.println("3. Lire un article (non disponible pour l'instant)");
            System.out.println("4. Retour au choix du langage");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consomme la fin de ligne

            switch (choice) {
                case 1:
                    launchQuiz(player, language);
                    break;
                case 2:
                    System.out.println("Mini-projets en cours de développement...");
                    break;
                case 3:
                    System.out.println("Articles en cours de développement...");
                    break;
                case 4:
                    System.out.println("Retour au choix du langage...");
                    selectingActivity = false;
                    break;
                default:
                    System.out.println("Option invalide. Réessayez.");
            }
        }

        scanner.close();
    }

    private static void launchQuiz(Player player, String language) {
        Scanner scanner = new Scanner(System.in);
    
        List<QuizInfo> availableQuizzes = DatabaseManager.loadQuizzesByLanguage(language);
    
        if (availableQuizzes.isEmpty()) {
            System.out.println("Aucun quiz disponible pour " + language + ".");
            return;
        }
    
        System.out.println("\nChoisissez votre quiz :");
        for (int i = 0; i < availableQuizzes.size(); i++) {
            System.out.println((i + 1) + ". " + availableQuizzes.get(i).getTitle());
        }
        System.out.println((availableQuizzes.size() + 1) + ". Retour");
    
        System.out.print("Votre choix : ");
        int choice = scanner.nextInt();
        scanner.nextLine();
    
        if (choice == availableQuizzes.size() + 1) {
            System.out.println("Retour au menu précédent...");
            return;
        }
    
        if (choice < 1 || choice > availableQuizzes.size()) {
            System.out.println("Option invalide.");
            return;
        }
    
        QuizInfo selectedQuiz = availableQuizzes.get(choice - 1);
        List<Question> questions = DatabaseManager.loadQuestionsForQuiz(selectedQuiz.getId());
        Quiz quiz = new Quiz(selectedQuiz.getTitle(), questions);
        
        for (Question q : questions) {
            quiz.addQuestion(q.getText(), q.getOptions(), q.getCorrectOption());
        }
    
        quiz.play(player);
        DatabaseManager.savePlayer(player);
    }
}
