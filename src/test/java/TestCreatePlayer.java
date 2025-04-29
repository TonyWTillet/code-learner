package test;
import java.util.Scanner;

import db.DatabaseManager;

public class TestCreatePlayer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DatabaseManager.createTableIfNotExists(); // Toujours préparer la table

        System.out.println("Bienvenue sur Code Learner");
        System.out.print("Entrez votre pseudo de joueur : ");
        String name = scanner.nextLine().trim();
        
        // Validation du pseudo
        if (name.isEmpty() || name.length() > 50 || !name.matches("^[a-zA-Z0-9_-]+$")) {
            System.out.println("Pseudo invalide. Utilisez uniquement des lettres, chiffres, tirets et underscores.");
            scanner.close();
            return;
        }

        boolean created = DatabaseManager.createPlayer(name);

        if (created) {
            System.out.println("Joueur créé avec succès !");
        } else {
            System.out.println("Ce pseudo est déjà pris. Veuillez en choisir un autre.");
        }
        
        scanner.close();
    }
}
