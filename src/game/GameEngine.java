package src.game;

import src.structures.*;
import src.model.*;
import java.util.*;
import java.awt.*;
import java.util.List;

public class GameEngine {
    public enum GameMode {
        SINGLE_PLAYER,
        AI_BATTLE,
        MULTIPLAYER
    }
    
    // Core game components
    private PlayerShip player;
    private PlayerShip player2;
    private List<AIShip> aiShips;
    private SinglyLinkedList<Bullet> bullets;
    private SinglyLinkedList<Enemy> enemies;
    private SinglyLinkedList<PowerUp> powerUps;
    private QueueDS<EnemyWave> enemyWaves;
    private StackDS<String> moveHistory;
    private BST scoreBoard;
    private DoublyLinkedList<String> objectives;
    private CircularLinkedList<String> powerUpCycle;
    private Graph levelMap;
    private MinHeap obstacleHeap;
    
    // Levels as linked list
    private LevelLinkedList levels;
    private Level currentLevel;
    
    // Game state
    private int score;
    private int player2Score;
    private int lives;
    private int player2Lives;
    private boolean gameOver;
    private boolean gameWon;
    private long startTime;
    private long levelStartTime;
    private int currentLevelIndex;
    private GameMode gameMode;
    private int difficulty;
    
    // Time constraints
    private static final long LEVEL_TIME_LIMIT = 120000; // 120 seconds per level
    private static final long EMOTION_COOLDOWN = 10000; // 10 seconds for emotion ability
    
    // Emotion system
    private EmotionSystem emotions;
    
    // AI System
    private AIController aiController;
    
    // Constants
    private static final int PLAYER_SPEED = 5;
    private static final int BULLET_SPEED = 7;
    
    // Weapons
    private int currentWeapon = 1; // 1: Normal, 2: Spread, 3: Laser
    
    public GameEngine() {
        this(GameMode.SINGLE_PLAYER, 2, 1);
    }
    
    public GameEngine(GameMode mode, int difficulty, int startLevel) {
        this.gameMode = mode;
        this.difficulty = difficulty;
        
        // Initialize all game components
        initializeGame(startLevel);
    }
    
    private void initializeGame(int startLevel) {
        player = new PlayerShip(400, 500, "Player 1");
        player2 = new PlayerShip(200, 500, "Player 2");
        
        bullets = new SinglyLinkedList<>();
        enemies = new SinglyLinkedList<>();
        powerUps = new SinglyLinkedList<>();
        enemyWaves = new QueueDS<>();
        moveHistory = new StackDS<>();
        scoreBoard = new BST();
        objectives = new DoublyLinkedList<>();
        powerUpCycle = new CircularLinkedList<>();
        levelMap = new Graph();
        obstacleHeap = new MinHeap(50);
        
        // Initialize levels linked list
        levels = new LevelLinkedList();
        initializeLevels();
        
        // Start from specified level
        currentLevel = levels.getFirst();
        for (int i = 1; i < startLevel && currentLevel != null; i++) {
            currentLevel = levels.getNextLevel();
        }
        currentLevelIndex = startLevel - 1;
        
        // Initialize AI based on game mode
        aiShips = new ArrayList<>();
        aiController = new AIController();
        
        switch (gameMode) {
            case SINGLE_PLAYER:
                // Add some enemy AI ships
                for (int i = 0; i < 3; i++) {
                    aiShips.add(new AIShip(100 + i * 200, 100, false, "Enemy AI " + (i + 1)));
                }
                break;
            case AI_BATTLE:
                // Add both enemy and friendly AI
                aiShips.add(new AIShip(600, 500, true, "Friendly AI"));
                for (int i = 0; i < 2; i++) {
                    aiShips.add(new AIShip(100 + i * 300, 100, false, "Enemy AI " + (i + 1)));
                }
                break;
            case MULTIPLAYER:
                // No additional AI for multiplayer
                break;
        }
        
        // Initialize emotion system
        emotions = new EmotionSystem();
        
        score = 0;
        player2Score = 0;
        lives = 3;
        player2Lives = 3;
        gameOver = false;
        gameWon = false;
        startTime = System.currentTimeMillis();
        levelStartTime = startTime;
        
        initializeDataStructures();
        spawnFirstWave();
    }
    
    private void initializeLevels() {
        levels.addLevel(new Level(1, "Training Ground", 8, 1000, 1.0f));
        levels.addLevel(new Level(2, "Asteroid Field", 12, 2000, 1.5f));
        levels.addLevel(new Level(3, "Enemy Base", 18, 3000, 2.0f));
        levels.addLevel(new Level(4, "Boss Arena", 25, 5000, 2.5f));
        levels.addLevel(new Level(5, "Final Showdown", 35, 10000, 3.0f));
        levels.addLevel(new Level(6, "Cosmic Chaos", 50, 15000, 3.5f));
        levels.addLevel(new Level(7, "Galaxy's Edge", 70, 20000, 4.0f));
    }
    
    private void initializeDataStructures() {
        // Add power-ups to cycle
        powerUpCycle.add("TRIPLE_SHOT");
        powerUpCycle.add("SHIELD");
        powerUpCycle.add("SPEED_BOOST");
        powerUpCycle.add("TIME_FREEZE");
        powerUpCycle.add("MEGA_BLAST");
        powerUpCycle.add("HEALTH");
        powerUpCycle.add("SCORE_BONUS");
        
        // Add objectives based on game mode
        objectives.add("Destroy " + currentLevel.getEnemyCount() + " enemies");
        objectives.add("Collect 3 power-ups");
        objectives.add("Complete level within time limit");
        
        if (gameMode == GameMode.AI_BATTLE) {
            objectives.add("Protect friendly AI");
        }
        if (gameMode == GameMode.MULTIPLAYER) {
            objectives.add("Beat opponent score");
        }
        
        // Create level map
        levelMap.addRoom("Start");
        for (int i = 1; i <= 7; i++) {
            levelMap.addRoom("Level " + i);
            if (i > 1) {
                levelMap.connectRooms("Level " + (i - 1), "Level " + i);
            }
        }
        levelMap.connectRooms("Start", "Level 1");
        
        // Add initial scores
        scoreBoard.insert(15000, "Galaxy Master");
        scoreBoard.insert(12000, "Space Warrior");
        scoreBoard.insert(10000, "Star Captain");
        scoreBoard.insert(8000, "Cosmic Pilot");
        scoreBoard.insert(5000, "Rookie");
    }
    
    private void spawnFirstWave() {
        int baseCount = currentLevel.getEnemyCount();
        float baseSpeed = currentLevel.getDifficulty();
        
        enemyWaves.enqueue(new EnemyWave(baseCount / 3, baseSpeed));
        enemyWaves.enqueue(new EnemyWave(baseCount / 2, baseSpeed * 1.2f));
        enemyWaves.enqueue(new EnemyWave(baseCount, baseSpeed * 1.5f));
        
        // Additional wave for higher difficulty
        if (difficulty >= 3) {
            enemyWaves.enqueue(new EnemyWave(baseCount, baseSpeed * 2.0f));
        }
    }
    
    public void update() {
        if (gameOver || gameWon) return;
        
        checkTimeLimit();
        updateAgents();
        updateBullets();
        updateEnemies();
        updatePowerUps();
        updateAI();
        checkCollisions();
        checkLevelCompletion();
        
        // Spawn new waves if needed
        if (enemies.isEmpty() && !enemyWaves.isEmpty()) {
            spawnWave(enemyWaves.dequeue());
        }
        
        // Spawn random power-up
        if (Math.random() < 0.005) {
            spawnRandomPowerUp();
        }
    }
    
    private void updateAI() {
        // Update AI controller
        aiController.update(this);
        
        // Update all AI ships
        for (AIShip ai : aiShips) {
            ai.update(this);
            
            // AI shooting logic
            if (ai.isFriendly()) {
                // Friendly AI shoots at enemies
                if (Math.random() < 0.01 * difficulty) {
                    ai.shoot(bullets, player);
                }
            } else {
                // Enemy AI shoots at player
                if (Math.random() < 0.015 * difficulty) {
                    ai.shoot(bullets, player);
                }
            }
        }
    }
    
    private void updateAgents() {
        emotions.update();
    }
    
private void updateBullets() {
    Object[] bulletArray = bullets.toArray();
    List<Bullet> toRemove = new ArrayList<>();
    
    for (Object obj : bulletArray) {
        Bullet bullet = (Bullet) obj;
        bullet.update(); // This line must be here!
        
        // Remove bullets that are off-screen
        if (bullet.getY() < -50 || bullet.getY() > 650 ||
            bullet.getX() < -50 || bullet.getX() > 850) {
            toRemove.add(bullet);
        }
    }
    
    for (Bullet bullet : toRemove) {
        bullets.remove(bullet);
    }
}
    private void updateEnemies() {
        Object[] enemyArray = enemies.toArray();
        List<Enemy> toRemove = new ArrayList<>();
        
        for (Object obj : enemyArray) {
            Enemy enemy = (Enemy) obj;
            enemy.update(currentLevel.getDifficulty() * (difficulty * 0.5f));
            
            // Remove enemies that are off-screen
            if (enemy.getY() > 650) {
                toRemove.add(enemy);
            }
        }
        
        for (Enemy enemy : toRemove) {
            enemies.remove(enemy);
        }
    }
    
    private void updatePowerUps() {
        Object[] powerUpArray = powerUps.toArray();
        List<PowerUp> toRemove = new ArrayList<>();
        
        for (Object obj : powerUpArray) {
            PowerUp powerUp = (PowerUp) obj;
            powerUp.update();
            
            if (powerUp.getY() > 650) {
                toRemove.add(powerUp);
            }
        }
        
        for (PowerUp powerUp : toRemove) {
            powerUps.remove(powerUp);
        }
    }
    
    private void checkTimeLimit() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - levelStartTime;
        
        if (elapsed > LEVEL_TIME_LIMIT) {
            loseLife();
            levelStartTime = currentTime;
        }
    }
    
    private void checkCollisions() {
        checkBulletCollisions();
        checkPlayerCollisions();
        checkPowerUpCollisions();
        checkAICollisions();
        
        // Check for combos
        if (moveHistory.containsCombo()) {
            applyComboEffect();
        }
    }
    
    private void checkBulletCollisions() {
    Object[] bulletArray = bullets.toArray();
    Object[] enemyArray = enemies.toArray();
    
    for (Object bulletObj : bulletArray) {
        Bullet bullet = (Bullet) bulletObj;
        Rectangle bulletRect = bullet.getBounds(); // Use bullet's getBounds()
        
        // Check collision with enemies
        for (Object enemyObj : enemyArray) {
            Enemy enemy = (Enemy) enemyObj;
            if (!enemy.isDestroyed()) {
                Rectangle enemyRect = enemy.getBounds();
                
                if (bulletRect.intersects(enemyRect) && bullet.isFriendly()) {
                    enemy.takeDamage(bullet.getDamage());
                    
                    if (enemy.isDestroyed()) {
                        int points = 100 * currentLevel.getMultiplier();
                        if (moveHistory.containsCombo()) {
                            points *= 2;
                        }
                        
                        // Award points to appropriate player
                        if (bullet.getOwner().equals("Player 1")) {
                            score += points;
                        } else if (bullet.getOwner().equals("Player 2")) {
                            player2Score += points;
                        } else {
                            score += points; // Default to player 1
                        }
                        
                        // Random power-up drop
                        if (Math.random() < 0.3) {
                            spawnPowerUp(enemy.getX(), enemy.getY());
                        }
                    }
                    bullets.remove(bullet);
                    break;
                }
            }
        }
    }
}
    
    private void checkPlayerCollisions() {
        Rectangle playerRect = new Rectangle(player.getX() - 20, player.getY() - 20, 40, 40);
        Object[] enemyArray = enemies.toArray();
        
        // Check player 1 collisions
        for (Object enemyObj : enemyArray) {
            Enemy enemy = (Enemy) enemyObj;
            Rectangle enemyRect = new Rectangle(enemy.getX() - 15, enemy.getY() - 15, 30, 30);
            
            if (playerRect.intersects(enemyRect) && !player.isInvulnerable()) {
                loseLife();
                enemy.takeDamage(50);
                break;
            }
        }
        
        // Check player 2 collisions in multiplayer
        if (gameMode == GameMode.MULTIPLAYER) {
            Rectangle player2Rect = new Rectangle(player2.getX() - 20, player2.getY() - 20, 40, 40);
            for (Object enemyObj : enemyArray) {
                Enemy enemy = (Enemy) enemyObj;
                Rectangle enemyRect = new Rectangle(enemy.getX() - 15, enemy.getY() - 15, 30, 30);
                
                if (player2Rect.intersects(enemyRect) && !player2.isInvulnerable()) {
                    player2Lives--;
                    enemy.takeDamage(50);
                    break;
                }
            }
        }
    }
    
    private void checkPowerUpCollisions() {
        Rectangle playerRect = new Rectangle(player.getX() - 20, player.getY() - 20, 40, 40);
        Object[] powerUpArray = powerUps.toArray();
        
        for (Object obj : powerUpArray) {
            PowerUp powerUp = (PowerUp) obj;
            Rectangle powerUpRect = new Rectangle(powerUp.getX() - 10, powerUp.getY() - 10, 20, 20);
            
            if (playerRect.intersects(powerUpRect)) {
                applyPowerUp(powerUp, player);
                powerUps.remove(powerUp);
                break;
            }
        }
        
        // Check player 2 power-up collisions
        if (gameMode == GameMode.MULTIPLAYER) {
            Rectangle player2Rect = new Rectangle(player2.getX() - 20, player2.getY() - 20, 40, 40);
            for (Object obj : powerUpArray) {
                PowerUp powerUp = (PowerUp) obj;
                Rectangle powerUpRect = new Rectangle(powerUp.getX() - 10, powerUp.getY() - 10, 20, 20);
                
                if (player2Rect.intersects(powerUpRect)) {
                    applyPowerUp(powerUp, player2);
                    powerUps.remove(powerUp);
                    break;
                }
            }
        }
    }
    
    private void checkAICollisions() {
        // Check collisions between AI ships and enemies/bullets
        for (AIShip ai : aiShips) {
            Rectangle aiRect = new Rectangle(ai.getX() - 15, ai.getY() - 15, 30, 30);
            
            // Check collision with enemies
            Object[] enemyArray = enemies.toArray();
            for (Object enemyObj : enemyArray) {
                Enemy enemy = (Enemy) enemyObj;
                Rectangle enemyRect = new Rectangle(enemy.getX() - 15, enemy.getY() - 15, 30, 30);
                
                if (aiRect.intersects(enemyRect)) {
                    if (ai.isFriendly()) {
                        ai.takeDamage(20);
                        enemy.takeDamage(50);
                    } else {
                        ai.takeDamage(10);
                        enemy.takeDamage(30);
                    }
                }
            }
        }
    }
    
    private void applyPowerUp(PowerUp powerUp, PlayerShip targetPlayer) {
        String type = powerUp.getTypeString();
        switch (type) {
            case "TRIPLE_SHOT":
                targetPlayer.activateTripleShot(8000);
                break;
            case "SHIELD":
                targetPlayer.activateShield(10000);
                break;
            case "SPEED_BOOST":
                targetPlayer.activateSpeedBoost(6000);
                break;
            case "TIME_FREEZE":
                freezeEnemies(4000);
                break;
            case "MEGA_BLAST":
                activateMegaBlast();
                break;
            case "HEALTH":
                targetPlayer.heal(50);
                break;
            case "SCORE_BONUS":
                if (targetPlayer == player) {
                    score += 500;
                } else {
                    player2Score += 500;
                }
                break;
        }
    }
    
    private void freezeEnemies(long duration) {
        Object[] enemyArray = enemies.toArray();
        for (Object enemyObj : enemyArray) {
            Enemy enemy = (Enemy) enemyObj;
            enemy.freeze(duration);
        }
    }
    
    private void activateMegaBlast() {
        // Destroy all enemies on screen
        Object[] enemyArray = enemies.toArray();
        for (Object enemyObj : enemyArray) {
            Enemy enemy = (Enemy) enemyObj;
            enemy.takeDamage(1000);
            score += 100;
        }
    }
    
    private void applyComboEffect() {
        System.out.println("COMBO ACHIEVED!");
        score += 500;
        player.activateDoublePoints(5000);
        moveHistory.clear();
    }
    
    private void spawnWave(EnemyWave wave) {
        int waveSize = wave.getCount();
        int screenWidth = 800;
        int spacing = screenWidth / (waveSize + 1);
        
        for (int i = 0; i < waveSize; i++) {
            int x = spacing * (i + 1);
            int y = -50 - (i * 20); // Staggered formation
            
            String enemyType;
            if (Math.random() < 0.3) {
                enemyType = "Fast Drone";
            } else if (Math.random() < 0.6) {
                enemyType = "Heavy Fighter";
            } else {
                enemyType = "Standard Enemy";
            }
            
            int health = enemyType.equals("Heavy Fighter") ? 150 : 100;
            int speed = enemyType.equals("Fast Drone") ? 4 : 2;
            int damage = enemyType.equals("Heavy Fighter") ? 30 : 15;
            
            enemies.add(new Enemy(enemyType, health, (int)(speed * wave.getSpeed()), damage, x, y));
        }
    }
    
    private void spawnPowerUp(int x, int y) {
        String type = powerUpCycle.getNext();
        powerUps.add(new PowerUp(x, y, type));
    }
    
    private void spawnRandomPowerUp() {
        int x = 50 + (int)(Math.random() * 700);
        int y = -20;
        String type = powerUpCycle.getNext();
        powerUps.add(new PowerUp(x, y, type));
    }
    
    private void checkLevelCompletion() {
        if (enemies.isEmpty() && enemyWaves.isEmpty()) {
            completeLevel();
        }
    }
    
    private void completeLevel() {
        // Calculate level score bonus
        long timeRemaining = LEVEL_TIME_LIMIT - (System.currentTimeMillis() - levelStartTime);
        int timeBonus = (int)(timeRemaining / 1000) * 20;
        score += timeBonus + currentLevel.getCompletionBonus();
        
        // Move to next level
        currentLevel = levels.getNextLevel();
        currentLevelIndex++;
        
        if (currentLevel == null) {
            gameWon = true;
            scoreBoard.insert(score, "Player 1");
            if (gameMode == GameMode.MULTIPLAYER) {
                scoreBoard.insert(player2Score, "Player 2");
            }
            return;
        }
        
        // Reset for next level
        levelStartTime = System.currentTimeMillis();
        spawnFirstWave();
        
        // Increase difficulty for next level
        difficulty += 0.1;
        
        System.out.println("Level " + currentLevelIndex + " complete! Moving to next level.");
    }
    
    private void loseLife() {
        lives--;
        player.setInvulnerable(2000);
        
        if (lives <= 0) {
            gameOver = true;
            scoreBoard.insert(score, "Player 1");
            if (gameMode == GameMode.MULTIPLAYER) {
                scoreBoard.insert(player2Score, "Player 2");
            }
        }
    }
    
    // Player controls
    public void moveShipLeft() {
        player.move(-PLAYER_SPEED, 0);
        moveHistory.push("LEFT");
    }
    
    public void moveShipRight() {
        player.move(PLAYER_SPEED, 0);
        moveHistory.push("RIGHT");
    }
    
    public void moveShipUp() {
        player.move(0, -PLAYER_SPEED);
        moveHistory.push("UP");
    }
    
    public void moveShipDown() {
        player.move(0, PLAYER_SPEED);
        moveHistory.push("DOWN");
    }
    
    // Player 2 controls (for multiplayer)
    public void movePlayer2Left() {
        if (gameMode == GameMode.MULTIPLAYER) {
            player2.move(-PLAYER_SPEED, 0);
        }
    }
    
    public void movePlayer2Right() {
        if (gameMode == GameMode.MULTIPLAYER) {
            player2.move(PLAYER_SPEED, 0);
        }
    }
    
    public void movePlayer2Up() {
        if (gameMode == GameMode.MULTIPLAYER) {
            player2.move(0, -PLAYER_SPEED);
        }
    }
    
    public void movePlayer2Down() {
        if (gameMode == GameMode.MULTIPLAYER) {
            player2.move(0, PLAYER_SPEED);
        }
    }
    
    public void shoot() {
        switch (currentWeapon) {
            case 1: // Normal shot
                player.shoot(bullets, "Player 1");
                break;
            case 2: // Spread shot
                player.shootSpread(bullets, "Player 1");
                break;
            case 3: // Laser
                player.shootLaser(bullets, "Player 1");
                break;
        }
    }
    
    public void player2Shoot() {
        if (gameMode == GameMode.MULTIPLAYER) {
            player2.shoot(bullets, "Player 2");
        }
    }
    
    public void switchWeapon(int weapon) {
        if (weapon >= 1 && weapon <= 3) {
            currentWeapon = weapon;
            System.out.println("Switched to weapon: " + weapon);
        }
    }
    
    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            String lastMove = moveHistory.pop();
            switch (lastMove) {
                case "LEFT":
                    player.move(PLAYER_SPEED, 0);
                    break;
                case "RIGHT":
                    player.move(-PLAYER_SPEED, 0);
                    break;
                case "UP":
                    player.move(0, PLAYER_SPEED);
                    break;
                case "DOWN":
                    player.move(0, -PLAYER_SPEED);
                    break;
            }
        }
    }
    
    
    public void useEmotionAbility() {
        if (emotions.canUseAbility()) {
            emotions.useAbility();
            String currentEmotion = emotions.getCurrentEmotion();
            applyEmotionEffect(currentEmotion);
        }
    }
    
    private void applyEmotionEffect(String emotion) {
        switch (emotion) {
            case "RAGE":
                player.activateTripleShot(8000);
                freezeEnemies(2000);
                break;
            case "CALM":
                player.activateShield(10000);
                player.heal(30);
                break;
            case "FOCUS":
                freezeEnemies(5000);
                player.activateDoublePoints(10000);
                break;
            case "JOY":
                score += 1000;
                spawnRandomPowerUp();
                spawnRandomPowerUp();
                break;
        }
    }
    
    public void restart() {
        initializeGame(1);
    }
    
    // Getters
    public PlayerShip getPlayer() { return player; }
    public PlayerShip getPlayer2() { return player2; }
    public List<AIShip> getAIShips() { return aiShips; }
    public SinglyLinkedList<Bullet> getBullets() { return bullets; }
    public SinglyLinkedList<Enemy> getEnemies() { return enemies; }
    public SinglyLinkedList<PowerUp> getPowerUps() { return powerUps; }
    public int getScore() { return score; }
    public int getPlayer2Score() { return player2Score; }
    public int getLives() { return lives; }
    public int getPlayer2Lives() { return player2Lives; }
    public boolean isGameOver() { return gameOver; }
    public boolean isGameWon() { return gameWon; }
    public Level getCurrentLevel() { return currentLevel; }
    public int getCurrentLevelIndex() { return currentLevelIndex; }
    public GameMode getGameMode() { return gameMode; }
    public int getDifficulty() { return difficulty; }
    public boolean isMultiplayerMode() { return gameMode == GameMode.MULTIPLAYER; }
    public EmotionSystem getEmotions() { return emotions; }
    public long getLevelTimeRemaining() {
        return Math.max(0, LEVEL_TIME_LIMIT - (System.currentTimeMillis() - levelStartTime));
    }
}