# 🎮 Code Learner - Projet Java Gamifié
# 🎮 Code Learner - Projet Java Gamifié

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![SQLite](https://img.shields.io/badge/SQLite-Database-lightgrey?logo=sqlite)

Bienvenue dans **Apprends-en-Jouant**, une application Java gamifiée conçue pour **apprendre la programmation** tout en **accumulant de l'expérience (XP)**, **passant des niveaux** et **débloquant des badges**. L'objectif ? **Motiver** l'apprentissage avec un système de **récompenses** inspiré des jeux vidéo !
Bienvenue dans **Apprends-en-Jouant**, une application Java gamifiée conçue pour **apprendre la programmation** tout en **accumulant de l'expérience (XP)**, **passant des niveaux** et **débloquant des badges**. L'objectif ? **Motiver** l'apprentissage avec un système de **récompenses** inspiré des jeux vidéo !

---

## 🚀 Fonctionnalités principales
## 🚀 Fonctionnalités principales

- 👤 Gestion complète du profil joueur (pseudo, niveau, XP, badges)
- 📋 Quiz thématiques (ex : Java Débutant, Python, C++)
- 🎯 Calcul automatique de l'expérience et progression dynamique
- 🧠 Quiz sous forme de QCM avec correction instantanée
- 👤 Gestion complète du profil joueur (pseudo, niveau, XP, badges)
- 📋 Quiz thématiques (ex : Java Débutant, Python, C++)
- 🎯 Calcul automatique de l'expérience et progression dynamique
- 🧠 Quiz sous forme de QCM avec correction instantanée
- 💾 Sauvegarde et chargement du profil via **base de données SQLite**
- ⚡ Système de montée de niveau et attribution de badges
- 📈 Architecture évolutive pour intégrer de nouveaux types d'activités (Mini-projets, Articles, Challenges)
- 🔄 Mécanisme de retry robuste pour les opérations de base de données

---

## 🛠️ Technologies utilisées

| Technologie | Version | Rôle |
|:---|:---|:---|
| **Java** | 21 | Langage principal |
| **SQLite** | 3.x | Base de données embarquée |
| **JDBC** | 4.x | API de connexion à la base de données |
| **WAL Mode** | - | Mode journal pour améliorer les performances |
| **Gradle** | - | Gestion des dépendances (futur) |
| **Gson** | - | Sérialisation JSON (futur) |
| **JUnit** | 5.x | Tests unitaires (futur) |
| **JavaFX** | 21 | Interface graphique (version courante) |
| **Terminal** | - | Interface actuelle (ancienne version) |
| **JavaFX** | 21 | Interface graphique (version courante) |
| **Terminal** | - | Interface actuelle (ancienne version) |

---

## 🏗️ Architecture du projet
## 🏗️ Architecture du projet

```
src/
  main/
    java/
      app/      // Navigation, menus, lancement du jeu
      core/     // Modèles de données : Player, Quiz, Question, etc.
      db/       // Gestion base de données : DatabaseManager, DatabaseWorker…
      utils/    // (actuellement vide)
    ressources/ // Base SQLite (database.db), logs
  test/
    java/
      test/     // Tests manuels (TestQuiz, TestPlayer, TestCreatePlayer)
```

---

## 🧩 Design Patterns et bonnes pratiques
## 🧩 Design Patterns et bonnes pratiques

- **Singleton** sur la classe `DatabaseManager` (connexion unique à la base)
- **Factory** (bientôt) pour la génération dynamique d'activités (Quiz, Mini-Projet, etc.)
- **Strategy** (bientôt) pour ajouter des comportements différents aux activités
- **Transaction** pour garantir l'intégrité des données

---

## ⚡ Optimisations techniques
## ⚡ Optimisations techniques

- **Gestion robuste des transactions SQLite** avec commit/rollback
- **Mécanisme de réessaie** avec délai exponentiel pour les accès concurrents à la base
- **Configuration optimisée de SQLite** (mode WAL, timeout augmenté, synchronisation normale)
- **Mécanisme de réessaie** avec délai exponentiel pour les accès concurrents à la base
- **Configuration optimisée de SQLite** (mode WAL, timeout augmenté, synchronisation normale)
- **Gestion explicite des connexions** pour éviter les fuites de ressources
- **Journalisation améliorée** pour faciliter le débogage

---

## 🧪 Tests

- Les tests sont **manuels** : lancez les classes du dossier `src/test/java/test/` pour vérifier les fonctionnalités principales (création de joueur, quiz, etc.).
- Passage à des tests automatisés (JUnit) prévu dans la feuille de route.

---

## 🚦 Comment lancer le projet
## 🧪 Tests

- Les tests sont **manuels** : lancez les classes du dossier `src/test/java/test/` pour vérifier les fonctionnalités principales (création de joueur, quiz, etc.).
- Passage à des tests automatisés (JUnit) prévu dans la feuille de route.

---

## 🚦 Comment lancer le projet

1. **Téléchargez** le projet.
2. Installez **Java 21** et **SQLite JDBC** ([téléchargement ici](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)).
3. Compilez :
   ```bash
   ./gradlew clean build
   ```
4. Lancez un test manuel (exemple console) :
   ```bash
   java -cp ".:build/classes/java/main:build/resources/main" test.java.test.TestQuiz
   ```
5. **Pour lancer l'interface graphique JavaFX** :
   ```bash
   ./gradlew run
   ```
   ou, si vous souhaitez lancer manuellement :
   ```bash
   java --module-path /chemin/vers/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml -cp "build/classes/java/main;build/resources/main" gui.MainApp
   ```
   > Remplacez `/chemin/vers/javafx-sdk-21/lib` par le chemin réel de votre SDK JavaFX.

---

## 🔥 Feuille de route (Roadmap)

- **Progression multi-joueurs** avec scores et classements
- **Mini-projets** à réaliser pour débloquer de nouveaux badges
- Notifications et rappels pour encourager l'apprentissage quotidien
- Génération automatique de quiz adaptés au niveau
- Génération automatique de quiz adaptés au niveau
- **Pool de connexions** pour améliorer les performances en cas de charge élevée
- Passage à des tests automatisés (JUnit)

---

## 💡 Pourquoi ce projet ?
## 💡 Pourquoi ce projet ?

Ce projet est né d'une volonté de **rendre l'apprentissage de la programmation plus motivant** 🎯.  
En combinant des mécanismes de **gamification** et des **concepts fondamentaux de Java**, il permet à tout développeur débutant ou intermédiaire de progresser tout en s'amusant.

---

> Projet développé par [Tillet Tony] avec passion ❤️  
> *(Feel free to fork, contribute or play !)*

