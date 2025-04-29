package app;
import java.util.List;
import java.util.Scanner;

import db.DatabaseManager;
import db.QuizInfo;
import model.Player;
import model.Question;
import model.Quiz;

public class MenuService {

    public static void showMainLearningMenu(Player player, Scanner scanner) {
        boolean navigating = true;

        while (navigating) {
            navigating = showLanguageMenu(player, scanner);
        }
    }

    private static boolean showLanguageMenu(Player player, Scanner scanner) {
        while (true) {
            System.out.println("\nChoisissez votre langage :");
            System.out.println("1. Java");
            System.out.println("2. Python");
            System.out.println("3. C++");
            System.out.println("4. Retour au menu principal");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consomme la fin de ligne

            switch (choice) {
                case 1:
                    showActivityMenu(player, "Java", scanner);
                    break;
                case 2:
                    showActivityMenu(player, "Python", scanner);
                    break;
                case 3:
                    showActivityMenu(player, "C++", scanner);
                    break;
                case 4:
                    System.out.println("Retour au menu principal...");
                    return false; // <- Demander de quitter
                default:
                    System.out.println("Option invalide. Réessayez.");
            }
        }
    }

    private static void showActivityMenu(Player player, String language, Scanner scanner) {
        boolean selectingActivity = true;
        while (selectingActivity) {
            System.out.println("\nChoisissez votre type d'activité pour " + language + " :");
            System.out.println("1. Quiz");
            System.out.println("2. Mini-projet (non disponible)");
            System.out.println("3. Lire un article (non disponible)");
            System.out.println("4. Retour au choix du langage");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consommer la fin de ligne

            switch (choice) {
                case 1:
                    launchQuiz(player, language, scanner);
                    break;
                case 2:
                    System.out.println("Mini-projets en cours de développement...");
                    break;
                case 3:
                    System.out.println("Articles en cours de développement...");
                    break;
                case 4:
                    System.out.println("Retour au choix du langage...");
                    selectingActivity = false; // <- Retourner vers le menu du langage
                    break;
                default:
                    System.out.println("Option invalide. Réessayez.");
            }
        }
    }

    private static void launchQuiz(Player player, String language, Scanner scanner) {
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
        Quiz quiz = new Quiz(selectedQuiz.getTitle(), questions, selectedQuiz.getId());

        quiz.play(player, scanner);

        
        System.out.println("Appuyez sur Entrée pour revenir à la liste des quiz...");
        scanner.nextLine(); 
    }
}
