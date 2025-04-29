package model;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int id;
    private String name;
    private int xp;
    private int level;
    private List<String> badges;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.xp = 0;
        this.level = 1;
        this.badges = new ArrayList<>();
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public List<String> getBadges() {
        return badges;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
    

    // Add XP and Level Up
    public void addXp(int amount) {
        xp += amount;
        if (xp >= 100) {
            level++;
            xp = 0;
            System.out.println( "Congratulations! " + name + " has reached level " + level + "!");
            addBadge("Level " + level);
        }
        System.out.println(name + " has " + xp + " XP.");
    }

    // Add Badge
    public void addBadge(String badge) {
        badges.add(badge);
        System.out.println(name + " has earned a new badge: " + badge);
    }

    // Display Player Info
    public void displayProfile() {
        System.out.println("\n===  Player Profile  ===");
        System.out.println("Name: " + name);
        System.out.println("Level: " + level);
        System.out.println("XP: " + xp + "/100");
        if (badges.isEmpty()) {
            System.out.println("No badges yet. Earn some to show off!");
        } else {
            System.out.println("Badges: " + String.join(", ", badges));
        }
        System.out.println("========================");
    }   
}
