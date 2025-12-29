package src.model;

public class Player {
    private String name;
    private int x, y;
    private int health;
    private int score;
    private int bullets;
    private boolean shieldActive;
    private boolean tripleShotActive;
    private int speed;
    
    public Player(String name, int startX, int startY) {
        this.name = name;
        this.x = startX;
        this.y = startY;
        this.health = 100;
        this.score = 0;
        this.bullets = 50;
        this.shieldActive = false;
        this.tripleShotActive = false;
        this.speed = 5;
    }
    
    // Movement methods
    public void moveLeft() {
        if (x - speed >= 0) {
            x -= speed;
        }
    }
    
    public void moveRight() {
        if (x + speed <= 780) {
            x += speed;
        }
    }
    
    public void moveUp() {
        if (y - speed >= 0) {
            y -= speed;
        }
    }
    
    public void moveDown() {
        if (y + speed <= 580) {
            y += speed;
        }
    }
    
    public boolean shoot() {
        if (bullets > 0) {
            bullets--;
            return true;
        }
        return false;
    }
    
    public void reload() {
        bullets = 50;
    }
    
    public void takeDamage(int damage) {
        if (!shieldActive) {
            health -= damage;
            if (health < 0) health = 0;
        }
    }
    
    public void heal(int amount) {
        health += amount;
        if (health > 100) health = 100;
    }
    
    public void addScore(int points) {
        score += points;
    }
    
    public void activateShield() {
        shieldActive = true;
        // Shield would typically last for a duration
        // In a full implementation, you'd use a timer
    }
    
    public void activateTripleShot() {
        tripleShotActive = true;
        // Triple shot would typically last for a duration
    }
    
    // Getters
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public int getScore() { return score; }
    public int getBullets() { return bullets; }
    public boolean hasShield() { return shieldActive; }
    public boolean hasTripleShot() { return tripleShotActive; }
    
    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setHealth(int health) { this.health = health; }
    public void setScore(int score) { this.score = score; }
}