package app;
import java.util.Scanner;

import db.DatabaseManager;
import model.Player;

public class GameLauncher {
    private static Player currentPlayer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        DatabaseManager.createTableIfNotExists();
        DatabaseManager.createQuizTable();
        DatabaseManager.createQuestionTable();
        DatabaseManager.createCompletedQuizTable();


        boolean running = true;

        while (running) {
            System.out.println("Bienvenue sur Code Learner");
            System.out.println("1. Créer un nouveau joueur");
            System.out.println("2. Se connecter à un joueur existant");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createNewPlayer(scanner);
                    break;
                case 2:
                    loginPlayer(scanner);
                    break;
                case 3:
                    DatabaseManager.shutdown();
                    System.exit(0);
                    running = false;
                break;
                default:
                    System.out.println("Option invalide, réessayez.");
            }

            // Si le joueur est connecté, on enchaine sur le prochain menu
            if (currentPlayer != null) {
                connectedPlayerMenu(scanner);
            }
        }

        scanner.close();
    }

    // Option 1 : Créer un nouveau joueur
    private static void createNewPlayer(Scanner scanner) {
        System.out.print("Entrez votre nom de joueur : ");
        String name = scanner.nextLine();
        
        boolean created = DatabaseManager.createPlayer(name);


        if (created) {
            int id = DatabaseManager.getPlayer(name).getId();
            System.out.println("Joueur créé avec succès !");
            currentPlayer = new Player(id, name);
        } else {
            System.out.println("Nom de joueur déjà utilisé.");
        }
    }

    // Option 2 : Se connecter à un joueur existant
    private static void loginPlayer(Scanner scanner) {
        System.out.print("Entrez votre nom de joueur : ");
        String name = scanner.nextLine();

        currentPlayer = DatabaseManager.getPlayer(name);

        if (currentPlayer != null) {
            System.out.println("Connexion réussie ! Bienvenue " + currentPlayer.getName());
        } else {
            System.out.println("Nom de joueur invalide.");
        }
    }

    // Menu principal pour les joueurs connectés
    private static void connectedPlayerMenu(Scanner scanner) {
        System.out.println("\n Vous êtes connecté en tant que : " + currentPlayer.getName());
        System.out.println("Appuyez sur [Entrée] pour continuer vers votre espace d'apprentissage...");
        scanner.nextLine();
        MenuService.showMainLearningMenu(currentPlayer, scanner);

    }
}