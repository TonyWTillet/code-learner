public class TestPlayer {
    public static void main(String[] args) {
        Player player = new Player("Tony");        

        player.displayProfile();

        // Test adding XP
        System.out.println("\nTesting adding XP...");
        player.addXp(5);

        // Test adding XP to level up
        System.out.println("\nTesting adding XP to level up...");
        player.addXp(95);

        // Test adding badges
        System.out.println("\nTesting adding badges...");
        player.addBadge("First Badge");

        // Test adding XP after level up
        System.out.println("\nTesting adding XP after level up...");
        player.addXp(10);

        // Display final profile
        System.out.println("\nFinal Profile:");
        player.displayProfile();
        // Profile response should be:
        System.out.println("""
            ===  Player Profile  ===
            Name: Tony
            Level: 2
            XP: 10/100
            Badges: Level 2, First Badge
            ========================
        """);
        
    }
}

