package src.game;

import src.structures.SinglyLinkedList;
import src.model.*;
import java.awt.Rectangle;
import java.util.List;

public class AIShip {
    private int x, y;
    private int speed;
    private boolean friendly;
    private boolean rescued;
    private int health;
    private long lastShotTime;
    private static final long SHOT_COOLDOWN = 1500;
    private String name;
    private int behaviorPattern;
    private long lastPatternChange;
    private int targetX, targetY;
    private boolean avoiding;
    private int avoidDirection;
    
    public AIShip(int x, int y, boolean friendly, String name) {
        this.x = x;
        this.y = y;
        this.friendly = friendly;
        this.rescued = false;
        this.health = friendly ? 150 : 200;
        this.speed = friendly ? 4 : 3;
        this.name = name;
        this.behaviorPattern = (int)(Math.random() * 3);
        this.lastPatternChange = System.currentTimeMillis();
        this.targetX = x;
        this.targetY = y;
        this.avoiding = false;
        this.avoidDirection = 0;
        this.lastShotTime = 0;
    }
    
    public void update(GameEngine engine) {
        long currentTime = System.currentTimeMillis();
        
        // Change behavior pattern every 5-10 seconds
        if (currentTime - lastPatternChange > 5000 + Math.random() * 5000) {
            behaviorPattern = (int)(Math.random() * 4);
            lastPatternChange = currentTime;
            avoiding = false;
        }
        
        // Update movement based on behavior pattern
        switch (behaviorPattern) {
            case 0: // Patrol pattern
                patrolMovement();
                break;
            case 1: // Chase pattern
                if (!friendly && engine.getPlayer() != null) {
                    chaseMovement(engine.getPlayer());
                } else {
                    patrolMovement();
                }
                break;
            case 2: // Evasive pattern
                evasiveMovement();
                break;
            case 3: // Support pattern (friendly AI only)
                if (friendly && engine.getPlayer() != null) {
                    supportMovement(engine.getPlayer());
                } else {
                    patrolMovement();
                }
                break;
        }
        
        // Apply avoidance if needed
        if (avoiding) {
            x += speed * avoidDirection;
        }
        
        // Keep within bounds
        x = Math.max(50, Math.min(750, x));
        y = Math.max(50, Math.min(550, y));
    }
    
    private void patrolMovement() {
        // Move towards target position
        if (x < targetX) {
            x += speed;
        } else if (x > targetX) {
            x -= speed;
        }
        
        if (y < targetY) {
            y += speed;
        } else if (y > targetY) {
            y -= speed;
        }
        
        // Change target if close enough or randomly
        if (Math.abs(x - targetX) < 20 && Math.abs(y - targetY) < 20 || Math.random() < 0.05) {
            targetX = 50 + (int)(Math.random() * 700);
            targetY = 50 + (int)(Math.random() * 500);
        }
    }
    
    private void chaseMovement(PlayerShip player) {
        if (player == null) return;
        
        // Move towards player
        if (x < player.getX()) {
            x += speed + 1;
        } else if (x > player.getX()) {
            x -= speed + 1;
        }
        
        if (y < player.getY()) {
            y += speed;
        } else if (y > player.getY()) {
            y -= speed;
        }
        
        // Keep some distance
        int distance = (int)Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2));
        if (distance < 100) {
            if (x < player.getX()) {
                x -= speed;
            } else {
                x += speed;
            }
        }
    }
    
    private void evasiveMovement() {
        // Random zig-zag movement
        if (Math.random() < 0.3) {
            x += (Math.random() < 0.5) ? -speed * 2 : speed * 2;
        }
        if (Math.random() < 0.3) {
            y += (Math.random() < 0.5) ? -speed : speed;
        }
    }
    
    private void supportMovement(PlayerShip player) {
        if (player == null) return;
        
        // Stay near player but not too close
        int desiredDistance = 150;
        int distance = (int)Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2));
        
        if (distance > desiredDistance + 50) {
            // Move closer to player
            if (x < player.getX()) {
                x += speed;
            } else {
                x -= speed;
            }
            
            if (y < player.getY()) {
                y += speed;
            } else {
                y -= speed;
            }
        } else if (distance < desiredDistance - 50) {
            // Move away from player
            if (x < player.getX()) {
                x -= speed;
            } else {
                x += speed;
            }
            
            if (y < player.getY()) {
                y -= speed;
            } else {
                y += speed;
            }
        } else {
            // Patrol near player
            patrolMovement();
        }
    }
    
    public void shoot(SinglyLinkedList<Bullet> bullets, PlayerShip target) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime > SHOT_COOLDOWN) {
            int bulletX = x;
            int bulletY = y + (friendly ? 30 : -20);
            
            // Calculate direction to target
            if (target != null && !friendly) {
                // Enemy AI aims at player
                int targetDirectionX = target.getX() - x;
                int targetDirectionY = target.getY() - y;
                double distance = Math.sqrt(targetDirectionX * targetDirectionX + targetDirectionY * targetDirectionY);
                
                if (distance > 0) {
                    bulletX += (int)((targetDirectionX / distance) * 20);
                }
            }
            
            Bullet bullet = new Bullet(bulletX, bulletY, friendly, friendly ? 5 : 8);
            bullet.setOwner(name);
            bullets.add(bullet);
            lastShotTime = currentTime;
        }
    }
    
    public void rescue() {
        if (friendly) {
            rescued = true;
            health = 150;
        }
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
        }
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }
    public boolean isFriendly() { return friendly; }
    public boolean isRescued() { return rescued; }
    public int getHealth() { return health; }
    public String getName() { return name; }
    
    public Rectangle getBounds() {
        return new Rectangle(x - 15, y - 15, 30, 30);
    }
}