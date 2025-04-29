package model;
import java.util.List;
import java.util.Scanner;

import db.DatabaseManager;
public class Quiz implements Activity {
    private String title;
    private List<Question> questions;
    private int id; 


    public Quiz(String title, List<Question> questions, int id) {
        this.title = title;
        this.questions = questions;
        this.id = id;
    }

    // Play the quiz
    @Override
    public void play(Player player, Scanner scanner) {
        int correctAnswers = 0;

        System.out.println("\n=== Quiz : " + title + " ===");

        // Parcours des questions
        for (Question question : questions) {
            System.out.println("\n" + question.getText());

            // Afficher les options
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + ". " + options.get(i));
            }

            // Demander à l'utilisateur de choisir une option
            System.out.print("Choisis le numéro de ta réponse : ");
            int userChoice;

            try {
                userChoice = scanner.nextInt() - 1; // Moins 1 car liste commence à 0
            } catch (Exception e) {
                System.out.println("Entrée invalide, réponse considérée comme fausse !");
                scanner.nextLine(); // vider la mauvaise entrée
                userChoice = -1; // réponse invalide
            }
                
            if (question.isCorrect(userChoice)) {
                System.out.println("Correct !");
                correctAnswers++;
            } else {
                System.out.println("Incorrect.");
                System.out.println("Good answer : " + options.get(question.getCorrectOption()));
            }
        }

        // Calculer le score
         // Résultat final
         System.out.println("\n=== Result of the Quiz ===");
         System.out.println("Correct answers : " + correctAnswers + "/" + questions.size());

         

         
         if (correctAnswers == questions.size()) {
            System.out.println("All correct answers ! Added 50 XP");
            player.addXp(50);
            player.addBadge("Quiz " + title);
            
        } else if (correctAnswers >= questions.size() / 2) {
            System.out.println("Well done ! Added 30 XP");
            player.addXp(30);
        } else {
            System.out.println("Continue to practice ! Added 10 XP");
            player.addXp(10);
        }
        
        // Save the player
        saveQuizResults(player, correctAnswers);
    }
    
    /**
     * Sauvegarde les résultats du quiz dans la base de données avec un mécanisme de retry
     * @param player Le joueur qui a complété le quiz
     * @param correctAnswers Le nombre de réponses correctes
     */
    private void saveQuizResults(Player player, int correctAnswers) {
        int maxRetries = 5; // Augmentation du nombre de tentatives
        int baseDelayMs = 100; // Délai de base en millisecondes
        boolean isPerfectScore = correctAnswers == questions.size();
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Utilisation d'une méthode unique pour sauvegarder les données
                DatabaseManager.savePlayerAndCompletedQuiz(player, id, correctAnswers, isPerfectScore);
                return; // Succès, on sort de la méthode
            } catch (Exception e) {
                if (attempt == maxRetries) {
                    // Dernière tentative échouée
                    String error = "Erreur lors de la sauvegarde des résultats du quiz (tentative " + attempt + "/" + maxRetries + ") : " + e.getMessage();
                    System.out.println(error);
                } else {
                    // Délai exponentiel entre les tentatives
                    int delayMs = baseDelayMs * (int)Math.pow(2, attempt - 1);
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    // Add a question to the quiz
    public void addQuestion(String text, List<String> options, int correctOption) {
        Question question = new Question(text, options, correctOption);
        questions.add(question);
    }

    // Get the title of the quiz
    public String getTitle() {
        return title;
    }

    // Get the questions of the quiz
    public List<Question> getQuestions() {
        return questions;
    }

    // Get the number of questions in the quiz
    public int getNumberOfQuestions() {
        return questions.size();
    }

    // Get the score of the quiz
}
