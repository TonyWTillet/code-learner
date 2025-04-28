import java.util.Scanner;

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
        if (language.equals("Java")) {
            Quiz quiz = new Quiz("Quiz Java Débutant");

            quiz.addQuestion(
                "Que signifie JDK ?",
                java.util.List.of("Java Development Kit", "Java Deployment Key", "Just Download Kit", "Java Default Key"),
                0
            );
            quiz.addQuestion(
                "Mot-clé pour créer un objet en Java ?",
                java.util.List.of("create", "build", "new", "instance"),
                2
            );
            quiz.addQuestion(
                "Comment commence un programme Java ?",
                java.util.List.of("start()", "run()", "main()", "init()"),
                2
            );

            quiz.play(player);
            DatabaseManager.savePlayer(player);
        } else {
            System.out.println("Quiz pour " + language + " pas encore disponible !");
        }
    }
}
