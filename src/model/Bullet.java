package src.model;

import java.awt.Rectangle;

public class Bullet {
    private int x, y;
    private int speed;
    private int damage;
    private int size;
    private boolean friendly;
    private String owner;
    private int directionX; // For angled shots
    private int directionY;
    private int type; // 1: Normal, 2: Spread, 3: Laser
    private long creationTime;
    private boolean piercing; // Can pierce through multiple enemies
    
    // Constructor for basic bullets
    public Bullet(int x, int y, boolean friendly, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.friendly = friendly;
        this.damage = friendly ? 25 : 15;
        this.size = friendly ? 5 : 4;
        this.owner = friendly ? "Player" : "Enemy";
        this.directionX = 0;
        this.directionY = friendly ? -1 : 1; // Player shoots up, enemies shoot down
        this.type = 1; // Normal bullet
        this.creationTime = System.currentTimeMillis();
        this.piercing = false;
    }
    
    // Update bullet position - FIXED METHOD
    public void update() {
        // Actually move the bullet based on direction
        if (type == 3) { // Laser moves very fast
            y += directionY * speed * 2;
            x += directionX * speed * 2 / 100; // directionX is 0-100 scale
        } else {
            y += directionY * speed;
            x += directionX * speed / 100; // directionX is 0-100 scale
        }
    }
    
    // Check if bullet is off screen
    public boolean isOffScreen(int screenWidth, int screenHeight) {
        return x < -100 || x > screenWidth + 100 ||
               y < -100 || y > screenHeight + 100;
    }
    
    // Check if bullet should be removed (for laser with time limit)
    public boolean shouldRemove() {
        if (type == 3) { // Laser lasts only 1 second
            return System.currentTimeMillis() - creationTime > 1000;
        }
        return false;
    }
    
    // Get bounding rectangle for collision detection
    public Rectangle getBounds() {
        switch (type) {
            case 3: // Laser
                return new Rectangle(x - size/2, y - 15, size, 30);
            default:
                return new Rectangle(x - size/2, y - size/2, size, size);
        }
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public int getDamage() { return damage; }
    public int getSize() { return size; }
    public boolean isFriendly() { return friendly; }
    public boolean isPlayerBullet() { return friendly; }
    public String getOwner() { return owner; }
    public int getType() { return type; }
    public boolean isPiercing() { return piercing; }
    
    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setDamage(int damage) { this.damage = damage; }
    public void setFriendly(boolean friendly) { this.friendly = friendly; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setType(int type) { this.type = type; }
    public void setPiercing(boolean piercing) { this.piercing = piercing; }
    public void setDirection(int dx, int dy) {
        this.directionX = dx;
        this.directionY = dy;
    }
}