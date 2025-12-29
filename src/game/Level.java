package src.game;

public class Level {
    private int number;
    private String name;
    private int enemyCount;
    private int completionBonus;
    private float difficulty;
    
    public Level(int number, String name, int enemyCount, int completionBonus, float difficulty) {
        this.number = number;
        this.name = name;
        this.enemyCount = enemyCount;
        this.completionBonus = completionBonus;
        this.difficulty = difficulty;
    }
    
    // Getters
    public int getNumber() { return number; }
    public String getName() { return name; }
    public int getEnemyCount() { return enemyCount; }
    public int getCompletionBonus() { return completionBonus; }
    public float getDifficulty() { return difficulty; }
    
    // Helper method to get multiplier for scoring
    public int getMultiplier() {
        return (int)(difficulty * 100);
    }
    
    @Override
    public String toString() {
        return "Level " + number + ": " + name + " (Difficulty: " + difficulty + ")";
    }
}