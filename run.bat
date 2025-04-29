@echo off
chcp 65001 > nul
echo.
echo ========================================
echo 🚀 Compilation du projet...
echo ========================================
call .\gradlew classes

echo.
echo ========================================
echo 🏁 Lancement de l'application...
echo ========================================
java -cp build\classes\java\main main.java.app.GameLauncher

echo.
pause
