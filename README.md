# 🎮 Apprends-en-Jouant - Projet Java Gamifié

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![SQLite](https://img.shields.io/badge/SQLite-Database-lightgrey?logo=sqlite)

Bienvenue dans **Apprends-en-Jouant**, une application Java gamifiée conçue pour **apprendre la programmation** tout en **accumulant de l'expérience (XP)**, **passer des niveaux** et **débloquer des badges** 🎖️.

L'objectif ? **Motiver** l'apprentissage avec un système de **récompenses** comme dans un jeu vidéo !

---

## 📚 Fonctionnalités principales

- 👤 Gestion de profil joueur (pseudo, niveau, XP, badges)
- 📋 Série de quiz thématiques (ex : Java Débutant)
- 🎯 Calcul automatique de l'expérience gagnée
- 🧠 Quiz sous forme de **QCM** avec validation automatique
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
| **JavaFX** | 21 | Interface graphique (futur) |
| **Terminal** | - | Interface actuelle (console) |

---

## 🏩 Architecture du projet

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

## 🧱 Design Patterns appliqués

- **Singleton** sur la classe `DatabaseManager` (connexion unique à la base)
- **Factory** (bientôt) pour la génération dynamique d'activités (Quiz, Mini-Projet, etc.)
- **Strategy** (bientôt) pour ajouter des comportements différents aux activités
- **Transaction** pour garantir l'intégrité des données

---

## 🔧 Optimisations techniques

- **Gestion robuste des transactions SQLite** avec commit/rollback
- **Mécanisme de retry** avec délai exponentiel pour les opérations de base de données
- **Configuration optimisée de SQLite** (WAL mode, timeout augmenté, synchronisation normale)
- **Gestion explicite des connexions** pour éviter les fuites de ressources
- **Journalisation améliorée** pour faciliter le débogage

---

## 🚀 Comment lancer le projet

1. Télécharger le projet.
2. Installer **Java 21** et **SQLite JDBC** ([téléchargement ici](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)).
3. Compiler :
   ```bash
   javac -cp ".:sqlite-jdbc-<version>.jar" src/main/java/app/*.java src/main/java/core/*.java src/main/java/db/*.java
   ```
4. Lancer un test manuel (exemple) :
   ```bash
   java -cp ".:sqlite-jdbc-<version>.jar:src/main/java:src/test/java" test.java.test.TestQuiz
   ```

---

## 🧪 Tests

- Les tests sont **manuels** : lancez les classes du dossier `src/test/java/test/` pour vérifier les fonctionnalités principales (création de joueur, quiz, etc.).
- Aucun framework de test automatisé (JUnit) n'est utilisé pour l'instant.

---

## 🔥 Objectifs futurs (Roadmap)

- Intégration d'une **interface graphique (JavaFX)**
- **Progression multi-joueurs** avec scores et classements
- **Mini-projets** à réaliser pour débloquer de nouveaux badges
- Notifications et rappels pour encourager l'apprentissage quotidien
- Génération automatique de quiz basés sur le niveau de compétence
- **Pool de connexions** pour améliorer les performances en cas de charge élevée
- Passage à des tests automatisés (JUnit)

---

## 📣 Pourquoi ce projet ?

Ce projet est né d'une volonté de **rendre l'apprentissage de la programmation plus motivant** 🎯.  
En combinant des mécanismes de **gamification** et des **concepts fondamentaux de Java**, il permet à tout développeur débutant ou intermédiaire de progresser tout en s'amusant.

---

> Projet développé par [Tillet Tony] avec passion ❤️  
> *(Feel free to fork, contribute or play !)*

