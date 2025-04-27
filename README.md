# 🎮 Apprends-en-Jouant - Projet Java Gamifié

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

---

## 🛠️ Technologies utilisées

| Technologie | Rôle |
|:---|:---|
| **Java 21** | Langage principal |
| **SQLite (JDBC)** | Sauvegarde et persistance des données |
| **Maven/Gradle** (optionnel futur) | Gestion des dépendances |
| **Gson** (optionnel futur) | Sauvegarde JSON alternative |
| **Terminal** | Interface actuelle (console), futur passage possible en JavaFX |

---

## 🏩 Architecture du projet

```
src/
  Main.java               // Démarrage du jeu
  Player.java              // Gestion du profil joueur
  Question.java            // Modèle de question (QCM)
  Quiz.java                // Modèle de Quiz complet
  Activity.java            // Interface pour les activités
  DatabaseManager.java     // Gestion de la connexion et des opérations SQLite
  TestQuiz.java            // Classe de test du Quiz

apprends_en_jouant.db       // (Fichier SQLite généré automatiquement)
```

---

## 🧱 Design Patterns appliqués

- **Singleton** sur la classe `DatabaseManager` (connexion unique à la base)
- **Factory** (bientôt) pour la génération dynamique d'activités (Quiz, Mini-Projet, etc.)
- **Strategy** (bientôt) pour ajouter des comportements différents aux activités

---

## 🚀 Comment lancer le projet

1. Télécharger le projet.
2. Installer **Java 21** et **SQLite JDBC** ([téléchargement ici](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc)).
3. Compiler :
   ```bash
   javac -cp ".:sqlite-jdbc-<version>.jar" src/*.java
   ```
4. Lancer :
   ```bash
   java -cp ".:sqlite-jdbc-<version>.jar:src" TestQuiz
   ```

---

## 🔥 Objectifs futurs (Roadmap)

- Intégration d'une **interface graphique (JavaFX)**.
- **Progression multi-joueurs** avec scores et classements.
- **Mini-projets** à réaliser pour débloquer de nouveaux badges.
- Notifications et rappels pour encourager l'apprentissage quotidien.
- Génération automatique de quiz basés sur le niveau de compétence.

---

## 📣 Pourquoi ce projet ?

Ce projet est né d'une volonté de **rendre l'apprentissage de la programmation plus motivant** 🎯.  
En combinant des mécanismes de **gamification** et des **concepts fondamentaux de Java**, il permet à tout développeur débutant ou intermédiaire de progresser tout en s'amusant.

---

> Projet développé par [Tillet Tony] avec passion ❤️  
> *(Feel free to fork, contribute or play !)*

