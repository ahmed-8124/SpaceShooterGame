package src.model;

import java.awt.Rectangle;

public class PowerUp {
    public enum Type {
        HEALTH,
        SHIELD,
        TRIPLE_SHOT,
        SPEED,
        SCORE_BONUS
    }
    
    private Type type;
    private int x, y;
    private int speed;
    private boolean collected;
    
    public PowerUp(Type type) {
        this.type = type;
        this.x = (int)(Math.random() * 750);
        this.y = -20;
        this.speed = 2;
        this.collected = false;
    }
    
    // Constructor for GameEngine
    public PowerUp(int x, int y, String type) {
        try {
            this.type = Type.valueOf(type.replace(" ", "_").toUpperCase());
        } catch (IllegalArgumentException e) {
            this.type = Type.HEALTH; // Default type
        }
        this.x = x;
        this.y = y;
        this.speed = 2;
        this.collected = false;
    }
    
    public void move() {
        y += speed; // Actually move the power-up
    }
    
    public void update() {
        move();
    }
    
    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
    
    public void collect() {
        collected = true;
    }
    
    public String getTypeString() {
        return type.toString();
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x - 10, y - 10, 20, 20);
    }
    
    // Getters
    public Type getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isCollected() { return collected; }
    
    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}