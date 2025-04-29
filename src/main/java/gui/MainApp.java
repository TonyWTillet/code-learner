package gui;

import db.DatabaseManager;
import db.QuizInfo;
import model.Player;
import model.Question;


import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {

    private Player currentPlayer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Code Learner");
        showLoginScene(primaryStage);
    }


    private void showLoginScene(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
    
        Label welcome = new Label("Bienvenue sur Code Learner 🎓");
        TextField nameField = new TextField();
        nameField.setPromptText("Entrez votre nom");
    
        Button createBtn = new Button("Créer un joueur");
        Button connectBtn = new Button("Se connecter");
    
        Label statusLabel = new Label();
    
        createBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                statusLabel.setText("⛔ Veuillez entrer un nom.");
                return;
            }
            boolean success = DatabaseManager.createPlayer(name);
            if (success) {
                currentPlayer = DatabaseManager.getPlayer(name);
                statusLabel.setText("✅ Joueur créé et connecté !");
                showLanguageScene(stage); // va au menu suivant
            } else {
                statusLabel.setText("❌ Ce nom existe déjà.");
            }
        });
    
        connectBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                statusLabel.setText("⛔ Veuillez entrer un nom.");
                return;
            }
            Player player = DatabaseManager.getPlayer(name);
            if (player != null) {
                currentPlayer = player;
                statusLabel.setText("✅ Connexion réussie !");
                showLanguageScene(stage);
            } else {
                statusLabel.setText("❌ Joueur introuvable.");
            }
        });
    
        layout.getChildren().addAll(welcome, nameField, createBtn, connectBtn, statusLabel);
        stage.setScene(new Scene(layout, 400, 250));
    }
    

    public static void main(String[] args) {
        launch(args);
    }

    private void showLanguageScene(Stage stage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
    
        Label title = new Label("🌍 Choisissez votre langage");
        Button javaBtn = new Button("Java");
        Button pythonBtn = new Button("Python");
        Button cppBtn = new Button("C++");
        Button backBtn = new Button("⬅ Retour");
    
        javaBtn.setOnAction(e -> showActivityScene(stage, "Java"));
        pythonBtn.setOnAction(e -> showActivityScene(stage, "Python"));
        cppBtn.setOnAction(e -> showActivityScene(stage, "C++"));
        backBtn.setOnAction(e -> showLoginScene(stage));
    
        layout.getChildren().addAll(title, javaBtn, pythonBtn, cppBtn, backBtn);
        stage.setScene(new Scene(layout, 400, 250));
    }

    private void showActivityScene(Stage stage, String language) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
    
        Label title = new Label("🧠 Choisissez une activité pour " + language);
    
        Button quizBtn = new Button("🎯 Quiz");
        Button articleBtn = new Button("📖 Lire un article");
        Button projectBtn = new Button("💻 Mini-projet");
        Button backBtn = new Button("⬅ Retour");
    
        quizBtn.setOnAction(e -> showQuizListScene(stage, language));
        articleBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Non disponible");
            alert.setHeaderText(null);
            alert.setContentText("Les articles ne sont pas encore disponibles !");
            alert.showAndWait();
        });
    
        projectBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Non disponible");
            alert.setHeaderText(null);
            alert.setContentText("Les mini-projets arrivent bientôt !");
            alert.showAndWait();
        });
    
        backBtn.setOnAction(e -> showLanguageScene(stage));
    
        layout.getChildren().addAll(title, quizBtn, articleBtn, projectBtn, backBtn);
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
    }
    
    private void showQuizListScene(Stage stage, String language) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
    
        Label title = new Label("📋 Quiz disponibles pour : " + language);
    
        ListView<String> quizListView = new ListView<>();
        List<QuizInfo> quizzes = DatabaseManager.loadQuizzesByLanguage(language);
        List<QuizInfo> availableQuizzes = new ArrayList<>(quizzes); // pour retrouver l'ID
    
        for (QuizInfo quiz : quizzes) {
            quizListView.getItems().add(quiz.getTitle());
        }
    
        Button startBtn = new Button("▶️ Démarrer le quiz");
        Button backBtn = new Button("⬅ Retour");
    
        startBtn.setOnAction(e -> {
            int selectedIndex = quizListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                QuizInfo selectedQuiz = availableQuizzes.get(selectedIndex);
                startQuizScene(stage, selectedQuiz); // méthode à créer après
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sélection requise");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un quiz.");
                alert.showAndWait();
            }
        });
    
        backBtn.setOnAction(e -> showActivityScene(stage, language));
    
        layout.getChildren().addAll(title, quizListView, startBtn, backBtn);
        stage.setScene(new Scene(layout, 400, 350));
    }
    
    private void startQuizScene(Stage stage, QuizInfo selectedQuiz) {
        List<Question> questions = DatabaseManager.loadQuestionsForQuiz(selectedQuiz.getId());
        if (questions.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Quiz vide");
            alert.setHeaderText(null);
            alert.setContentText("Ce quiz ne contient aucune question !");
            alert.showAndWait();
            showQuizListScene(stage, selectedQuiz.getLanguage());
            return;
        }
    
        showQuestion(stage, selectedQuiz, questions, 0, 0);
    }

    private void showQuestion(Stage stage, QuizInfo quiz, List<Question> questions, int index, int score) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
    
        Question current = questions.get(index);
        Label questionLabel = new Label((index + 1) + ". " + current.getText());
    
        ToggleGroup group = new ToggleGroup();
        VBox optionsBox = new VBox(10);
        List<RadioButton> buttons = new ArrayList<>();
    
        List<String> options = current.getOptions();
        for (int i = 0; i < options.size(); i++) {
            RadioButton rb = new RadioButton(options.get(i));
            rb.setToggleGroup(group);
            rb.setUserData(i); // stocker l’index comme info
            buttons.add(rb);
            optionsBox.getChildren().add(rb);
        }
    
        Button submitBtn = new Button(index == questions.size() - 1 ? "Terminer" : "Suivant");
    
        submitBtn.setOnAction(e -> {
            Toggle selected = group.getSelectedToggle();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune réponse");
                alert.setHeaderText(null);
                alert.setContentText("Merci de sélectionner une réponse.");
                alert.showAndWait();
                return;
            }
    
            int chosenIndex = (int) selected.getUserData();
            boolean correct = current.isCorrect(chosenIndex);
            int updatedScore = correct ? score + 1 : score;
    
            if (index == questions.size() - 1) {
                showQuizResultScene(stage, quiz, updatedScore, questions.size());
            } else {
                showQuestion(stage, quiz, questions, index + 1, updatedScore);
            }
        });
    
        layout.getChildren().addAll(questionLabel, optionsBox, submitBtn);
        stage.setScene(new Scene(layout, 500, 300));
    }

    private void showQuizResultScene(Stage stage, QuizInfo quiz, int score, int total) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
    
        Label result = new Label("✅ Résultat du quiz : " + score + "/" + total);
        Button backBtn = new Button("Retour aux quiz");
    
        backBtn.setOnAction(e -> showQuizListScene(stage, quiz.getLanguage()));
    
        layout.getChildren().addAll(result, backBtn);
        stage.setScene(new Scene(layout, 400, 200));
    }
    
    
    
}
