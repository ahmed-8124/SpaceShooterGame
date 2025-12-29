package src.model;

import java.awt.Rectangle;

public class Enemy {
    private String name;
    private int health;
    private int speed;
    private int damage;
    private int x, y;
    private boolean destroyed;
    private boolean frozen;
    private long freezeEndTime;
    
    public Enemy(String name, int health, int speed, int damage) {
        this.name = name;
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.x = 0;
        this.y = 0;
        this.destroyed = false;
        this.frozen = false;
        this.freezeEndTime = 0;
    }
    
    // Constructor for SpaceShooterGame
    public Enemy(String name, int health, int speed, int damage, int x, int y) {
        this(name, health, speed, damage);
        this.x = x;
        this.y = y;
    }
    
    public void move() {
        if (!frozen || System.currentTimeMillis() > freezeEndTime) {
            y += speed; // Actually move the enemy
        }
    }
    
    public void update(float difficultyMultiplier) {
        if (!frozen || System.currentTimeMillis() > freezeEndTime) {
            if (frozen && System.currentTimeMillis() > freezeEndTime) {
                frozen = false;
            }
            move();
        }
    }
    
    public void update() {
        update(1.0f);
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            destroyed = true;
        }
    }
    
    public void freeze(long duration) {
        this.frozen = true;
        this.freezeEndTime = System.currentTimeMillis() + duration;
    }
    
    public boolean isFrozen() {
        return frozen && System.currentTimeMillis() < freezeEndTime;
    }
    
    public boolean isFriendly() {
        return false;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x - 15, y - 15, 30, 30);
    }
    
    // Getters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getSpeed() { return speed; }
    public int getDamage() { return damage; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isDestroyed() { return destroyed; }
    
    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setHealth(int health) { this.health = health; }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
}