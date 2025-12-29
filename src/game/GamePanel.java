package src.game;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import src.structures.*;
import src.model.*;

public class GamePanel extends JPanel {
    private GameEngine engine;
    
    public GamePanel(GameEngine engine) {
        this.engine = engine;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(800, 600));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        drawBackground(g2d);
        
        // Draw game state
        if (engine.isGameOver()) {
            drawGameOver(g2d);
        } else if (engine.isGameWon()) {
            drawGameWon(g2d);
        } else {
            drawGame(g2d);
        }
    }
    
    private void drawBackground(Graphics2D g2d) {
        // Dark space background
        g2d.setColor(new Color(10, 10, 30));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw stars
        g2d.setColor(Color.WHITE);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(getWidth());
            int y = rand.nextInt(getHeight());
            int size = rand.nextInt(3) + 1;
            g2d.fillOval(x, y, size, size);
        }
    }
    
    private void drawGame(Graphics2D g2d) {
        // Draw player ships
        drawPlayer(g2d, engine.getPlayer(), Color.GREEN);
        
        if (engine.isMultiplayerMode()) {
            drawPlayer(g2d, engine.getPlayer2(), Color.BLUE);
        }
        
        // Draw AI ships
        drawAIShips(g2d);
        
        // Draw bullets
        drawBullets(g2d);
        
        // Draw enemies
        drawEnemies(g2d);
        
        // Draw power-ups
        drawPowerUps(g2d);
        
        // Draw UI
        drawUI(g2d);
    }
    
    private void drawPlayer(Graphics2D g2d, PlayerShip player, Color color) {
        if (player == null) return;
        
        // Draw ship body
        g2d.setColor(player.isInvulnerable() ? Color.CYAN : color);
        int[] xPoints = {player.getX(), player.getX() - 20, player.getX() + 20};
        int[] yPoints = {player.getY() - 20, player.getY() + 20, player.getY() + 20};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Draw shield if active
        if (player.isShieldActive()) {
            g2d.setColor(new Color(0, 255, 255, 100));
            g2d.drawOval(player.getX() - 25, player.getY() - 25, 50, 50);
        }
        
        // Draw health bar
        drawHealthBar(g2d, player.getX() - 20, player.getY() - 40, player.getHealth(), 100, color);
    }
    
    private void drawAIShips(Graphics2D g2d) {
        for (AIShip ai : engine.getAIShips()) {
            if (ai == null) continue;
            
            Color color = ai.isFriendly() ? Color.CYAN : Color.RED;
            g2d.setColor(color);
            g2d.fillOval(ai.getX() - 15, ai.getY() - 15, 30, 30);
            
            // Draw health bar
            drawHealthBar(g2d, ai.getX() - 15, ai.getY() - 25, ai.getHealth(),
                         ai.isFriendly() ? 150 : 200, color);
        }
    }
    
    private void drawBullets(Graphics2D g2d) {
        SinglyLinkedList<Bullet> bulletList = engine.getBullets();
        if (bulletList != null) {
            Object[] bullets = bulletList.toArray();
            for (Object obj : bullets) {
                if (obj instanceof Bullet) {
                    Bullet bullet = (Bullet) obj;
                    g2d.setColor(bullet.isFriendly() ? Color.YELLOW : Color.RED);
                    g2d.fillRect(bullet.getX() - 2, bullet.getY() - 5, 4, 10);
                }
            }
        }
    }
    
    private void drawEnemies(Graphics2D g2d) {
        SinglyLinkedList<Enemy> enemyList = engine.getEnemies();
        if (enemyList != null) {
            Object[] enemies = enemyList.toArray();
            for (Object obj : enemies) {
                if (obj instanceof Enemy) {
                    Enemy enemy = (Enemy) obj;
                    if (!enemy.isDestroyed()) {
                        // Draw enemy shape
                        g2d.setColor(Color.RED);
                        g2d.fillRect(enemy.getX() - 15, enemy.getY() - 15, 30, 30);
                        
                        // Draw enemy health
                        drawHealthBar(g2d, enemy.getX() - 15, enemy.getY() - 25,
                                    enemy.getHealth(), 150, Color.RED);
                    }
                }
            }
        }
    }
    
    private void drawPowerUps(Graphics2D g2d) {
        SinglyLinkedList<PowerUp> powerUpList = engine.getPowerUps();
        if (powerUpList != null) {
            Object[] powerUps = powerUpList.toArray();
            for (Object obj : powerUps) {
                if (obj instanceof PowerUp) {
                    PowerUp powerUp = (PowerUp) obj;
                    if (!powerUp.isCollected()) {
                        Color color = getPowerUpColor(powerUp.getTypeString());
                        g2d.setColor(color);
                        g2d.fillOval(powerUp.getX() - 12, powerUp.getY() - 12, 24, 24);
                    }
                }
            }
        }
    }
    
    private Color getPowerUpColor(String type) {
        switch (type) {
            case "TRIPLE_SHOT": return Color.YELLOW;
            case "SHIELD": return Color.BLUE;
            case "SPEED_BOOST": return Color.GREEN;
            case "TIME_FREEZE": return Color.CYAN;
            case "MEGA_BLAST": return Color.ORANGE;
            case "HEALTH": return Color.RED;
            case "SCORE_BONUS": return Color.MAGENTA;
            default: return Color.WHITE;
        }
    }
    
    private void drawHealthBar(Graphics2D g2d, int x, int y, int health, int maxHealth, Color color) {
        int barWidth = 30;
        int barHeight = 5;
        int currentWidth = (int)((float)health / maxHealth * barWidth);
        
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x, y, barWidth, barHeight);
        g2d.setColor(color);
        g2d.fillRect(x, y, currentWidth, barHeight);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, barWidth, barHeight);
    }
    
    private void drawUI(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Draw scores
        g2d.drawString("P1 Score: " + engine.getScore(), 10, 20);
        
        if (engine.isMultiplayerMode()) {
            g2d.setColor(Color.BLUE);
            g2d.drawString("P2 Score: " + engine.getPlayer2Score(), 10, 40);
            g2d.setColor(Color.WHITE);
        }
        
        // Draw lives
        g2d.drawString("P1 Lives: " + engine.getLives(), 150, 20);
        if (engine.isMultiplayerMode()) {
            g2d.setColor(Color.BLUE);
            g2d.drawString("P2 Lives: " + engine.getPlayer2Lives(), 150, 40);
            g2d.setColor(Color.WHITE);
        }
        
        // Draw level info
        Level currentLevel = engine.getCurrentLevel();
        if (currentLevel != null) {
            g2d.drawString("Level: " + currentLevel.getName(), 300, 20);
            g2d.drawString("Difficulty: " + String.format("%.1f", currentLevel.getDifficulty()), 300, 40);
        }
        
        // Draw time remaining
        long timeRemaining = engine.getLevelTimeRemaining() / 1000;
        g2d.setColor(timeRemaining < 30 ? Color.RED : Color.GREEN);
        g2d.drawString("Time: " + timeRemaining + "s", 500, 20);
        
        // Draw enemy count
        g2d.setColor(Color.YELLOW);
        int enemyCount = engine.getEnemies().size();
        g2d.drawString("Enemies: " + enemyCount, 500, 40);
        
        // Draw game mode
        g2d.setColor(Color.CYAN);
        String modeText = "";
        switch (engine.getGameMode()) {
            case SINGLE_PLAYER: modeText = "Single Player"; break;
            case AI_BATTLE: modeText = "AI Battle"; break;
            case MULTIPLAYER: modeText = "Multiplayer"; break;
        }
        g2d.drawString("Mode: " + modeText, 650, 20);
    }
    
    private void drawGameOver(Graphics2D g2d) {
        // Semi-transparent overlay
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Game Over text
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 64));
        g2d.drawString("GAME OVER", 230, 250);
        
        // Final scores
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Player 1 Score: " + engine.getScore(), 300, 300);
        
        // Instructions
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString("Press R to Restart | Press M for Main Menu", 220, 450);
    }
    
    private void drawGameWon(Graphics2D g2d) {
        // Celebration background
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // You Win text
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 72));
        g2d.drawString("YOU WIN!", 230, 250);
        
        // Final scores
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.drawString("Final Score: " + engine.getScore(), 320, 320);
        
        // Instructions
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString("Press R to Play Again | Press M for Main Menu", 200, 480);
    }
}