package src.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements KeyListener {
    public GameEngine engine;
    private GamePanel gamePanel;
    private Timer gameTimer;
    private boolean paused = false;
    
    public GameWindow(GameEngine engine) {
        this.engine = engine;
        
        setTitle("Space Shooter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        gamePanel = new GamePanel(engine);
        add(gamePanel);
        
        // Initialize game timer
        gameTimer = new Timer(16, e -> { // ~60 FPS
            if (!paused && !engine.isGameOver() && !engine.isGameWon()) {
                engine.update();
                gamePanel.repaint();
            }
        });
        
        addKeyListener(this);
        setFocusable(true);
        gameTimer.start();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (engine.isGameOver() || engine.isGameWon()) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            } else if (e.getKeyCode() == KeyEvent.VK_M) {
                returnToMenu();
            }
            return;
        }
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                engine.moveShipLeft();
                break;
            case KeyEvent.VK_RIGHT:
                engine.moveShipRight();
                break;
            case KeyEvent.VK_UP:
                engine.moveShipUp();
                break;
            case KeyEvent.VK_DOWN:
                engine.moveShipDown();
                break;
            case KeyEvent.VK_SPACE:
                engine.shoot();
                break;
            case KeyEvent.VK_U:
                engine.undoMove();
                break;
            case KeyEvent.VK_R:
                restartGame();
                break;
            case KeyEvent.VK_M:
                returnToMenu();
                break;
            case KeyEvent.VK_P:
                togglePause();
                break;
            case KeyEvent.VK_E:
                engine.useEmotionAbility();
                break;
            case KeyEvent.VK_1:
                engine.switchWeapon(1);
                break;
            case KeyEvent.VK_2:
                engine.switchWeapon(2);
                break;
            case KeyEvent.VK_3:
                engine.switchWeapon(3);
                break;
        }
    }
    
    private void togglePause() {
        paused = !paused;
        if (paused) {
            gameTimer.stop();
            showPauseMenu();
        } else {
            gameTimer.start();
        }
    }
    
    private void showPauseMenu() {
        String[] options = {"Resume", "Restart", "Main Menu", "Exit"};
        int choice = JOptionPane.showOptionDialog(this,
            "Game Paused",
            "Pause Menu",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]);
        
        switch (choice) {
            case 0: // Resume
                paused = false;
                gameTimer.start();
                break;
            case 1: // Restart
                restartGame();
                break;
            case 2: // Main Menu
                returnToMenu();
                break;
            case 3: // Exit
                System.exit(0);
                break;
        }
    }
    
    private void restartGame() {
        dispose();
        GameEngine newEngine = new GameEngine(engine.getGameMode(),
                                            engine.getDifficulty(),
                                            engine.getCurrentLevelIndex() + 1);
        new GameWindow(newEngine).setVisible(true);
    }
    
    private void returnToMenu() {
    dispose(); // Close current window
    
    SwingUtilities.invokeLater(() -> {
        try {
            // Create the main menu launcher
            new GameLauncher().setVisible(true);
        } catch (Exception e) {
            System.err.println("Error returning to main menu: " + e.getMessage());
            e.printStackTrace();
            
            // Alternative: Just exit the game
            int choice = JOptionPane.showConfirmDialog(this,
                "Could not return to main menu. Exit game?",
                "Error",
                JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    });
}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
}