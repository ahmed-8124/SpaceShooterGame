package src.game;

import java.util.*;
import src.structures.*;
import src.model.*;

public class AIController {
    private long lastDecisionTime;
    private static final long DECISION_INTERVAL = 1000; // Make decisions every second
    private Map<AIShip, String> currentTasks;
    
    public AIController() {
        lastDecisionTime = System.currentTimeMillis();
        currentTasks = new HashMap<>();
    }
    
    public void update(GameEngine engine) {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastDecisionTime > DECISION_INTERVAL) {
            makeDecisions(engine);
            lastDecisionTime = currentTime;
        }
        
        executeTasks(engine);
    }
    
    private void makeDecisions(GameEngine engine) {
        List<AIShip> aiShips = engine.getAIShips();
        if (aiShips.isEmpty()) return;
        
        // Analyze game state
        int enemyCount = engine.getEnemies().size();
        PlayerShip player = engine.getPlayer();
        boolean playerInDanger = isPlayerInDanger(engine, player);
        
        for (AIShip ai : aiShips) {
            String task;
            
            if (ai.isFriendly()) {
                // Friendly AI decisions
                if (playerInDanger && ai.getHealth() > 50) {
                    task = "PROTECT_PLAYER";
                } else if (enemyCount > 5) {
                    task = "CLEAR_ENEMIES";
                } else if (ai.getHealth() < 30) {
                    task = "EVADE";
                } else {
                    task = "PATROL";
                }
            } else {
                // Enemy AI decisions
                if (ai.getHealth() < 50) {
                    task = "EVADE";
                } else if (enemyCount < 3) {
                    task = "AGGRESSIVE";
                } else {
                    task = "FLANK";
                }
            }
            
            currentTasks.put(ai, task);
        }
    }
    
    private boolean isPlayerInDanger(GameEngine engine, PlayerShip player) {
        if (player == null) return false;
        
        // Check if enemies are close to player
        SinglyLinkedList<Enemy> enemies = engine.getEnemies();
        Object[] enemyArray = enemies.toArray();
        
        for (Object obj : enemyArray) {
            if (obj instanceof Enemy) {
                Enemy enemy = (Enemy) obj;
                int distance = (int)Math.sqrt(
                    Math.pow(player.getX() - enemy.getX(), 2) +
                    Math.pow(player.getY() - enemy.getY(), 2)
                );
                if (distance < 150) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void executeTasks(GameEngine engine) {
        for (Map.Entry<AIShip, String> entry : currentTasks.entrySet()) {
            AIShip ai = entry.getKey();
            String task = entry.getValue();
            
            switch (task) {
                case "PROTECT_PLAYER":
                    // Position between player and nearest enemy
                    ai.update(engine);
                    break;
                case "CLEAR_ENEMIES":
                    // Focus on destroying enemies
                    ai.update(engine);
                    break;
                case "EVADE":
                    // Move away from danger
                    ai.update(engine);
                    break;
                case "PATROL":
                    // Standard patrol behavior
                    ai.update(engine);
                    break;
                case "AGGRESSIVE":
                    // Direct attack on player
                    ai.update(engine);
                    break;
                case "FLANK":
                    // Try to flank the player
                    ai.update(engine);
                    break;
            }
        }
    }
}