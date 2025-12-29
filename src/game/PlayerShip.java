package src.game;

import src.structures.SinglyLinkedList;
import src.model.Bullet;

public class PlayerShip {
    private int x, y;
    private boolean invulnerable;
    private long invulnerableEndTime;
    private boolean tripleShotActive;
    private long tripleShotEndTime;
    private boolean shieldActive;
    private long shieldEndTime;
    private boolean speedBoostActive;
    private long speedBoostEndTime;
    private boolean doublePointsActive;
    private long doublePointsEndTime;
    private String name;
    private int health;
    
    public PlayerShip(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.invulnerable = false;
        this.health = 100;
    }
    
    public void move(int dx, int dy) {
        int speed = speedBoostActive ? 8 : 5;
        x += dx * speed;
        y += dy * speed;
        
        // Keep within bounds
        x = Math.max(20, Math.min(780, x));
        y = Math.max(20, Math.min(580, y));
        
        // Update active power-ups
        updatePowerUps();
    }
    
    public void shoot(SinglyLinkedList<Bullet> bullets, String owner) {
        Bullet bullet = new Bullet(x, y - 20, true, 7);
        bullet.setOwner(owner);
        bullets.add(bullet);
    }
    
    public void shootSpread(SinglyLinkedList<Bullet> bullets, String owner) {
        // Shoot 3 bullets in a spread pattern
        for (int i = -1; i <= 1; i++) {
            Bullet bullet = new Bullet(x + (i * 10), y - 20, true, 6);
            bullet.setOwner(owner);
            bullets.add(bullet);
        }
    }
    
    public void shootLaser(SinglyLinkedList<Bullet> bullets, String owner) {
        // Shoot a powerful laser
        Bullet bullet = new Bullet(x, y - 20, true, 10);
        bullet.setDamage(50);
        bullet.setOwner(owner);
        bullets.add(bullet);
    }
    
    private void updatePowerUps() {
        long currentTime = System.currentTimeMillis();
        
        if (invulnerable && currentTime > invulnerableEndTime) {
            invulnerable = false;
        }
        if (tripleShotActive && currentTime > tripleShotEndTime) {
            tripleShotActive = false;
        }
        if (shieldActive && currentTime > shieldEndTime) {
            shieldActive = false;
        }
        if (speedBoostActive && currentTime > speedBoostEndTime) {
            speedBoostActive = false;
        }
        if (doublePointsActive && currentTime > doublePointsEndTime) {
            doublePointsActive = false;
        }
    }
    
    public void activateTripleShot(long duration) {
        tripleShotActive = true;
        tripleShotEndTime = System.currentTimeMillis() + duration;
    }
    
    public void activateShield(long duration) {
        shieldActive = true;
        shieldEndTime = System.currentTimeMillis() + duration;
        setInvulnerable(duration);
    }
    
    public void activateSpeedBoost(long duration) {
        speedBoostActive = true;
        speedBoostEndTime = System.currentTimeMillis() + duration;
    }
    
    public void activateDoublePoints(long duration) {
        doublePointsActive = true;
        doublePointsEndTime = System.currentTimeMillis() + duration;
    }
    
    public void setInvulnerable(long duration) {
        invulnerable = true;
        invulnerableEndTime = System.currentTimeMillis() + duration;
    }
    
    public void heal(int amount) {
        health = Math.min(100, health + amount);
    }
    
    public void takeDamage(int damage) {
        if (!invulnerable) {
            health -= damage;
            if (health < 0) health = 0;
            setInvulnerable(1000); // Brief invulnerability after hit
        }
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isInvulnerable() { return invulnerable; }
    public boolean isShieldActive() { return shieldActive; }
    public boolean isSpeedBoostActive() { return speedBoostActive; }
    public boolean isDoublePointsActive() { return doublePointsActive; }
    public boolean isTripleShotActive() { return tripleShotActive; }
    public int getHealth() { return health; }
    public String getName() { return name; }
}