package src.game;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MultiplayerWindow extends GameWindow {
    
    public MultiplayerWindow(GameEngine engine) {
        super(engine);
        setTitle("Space Shooter - Multiplayer");
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        
        if (engine.isGameOver() || engine.isGameWon()) return;
        
        // Player 2 controls (WASD + Q for shoot)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                engine.movePlayer2Left();
                break;
            case KeyEvent.VK_D:
                engine.movePlayer2Right();
                break;
            case KeyEvent.VK_W:
                engine.movePlayer2Up();
                break;
            case KeyEvent.VK_S:
                engine.movePlayer2Down();
                break;
            case KeyEvent.VK_Q:
                engine.player2Shoot();
                break;
        }
    }
}