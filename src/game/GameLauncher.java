package src.game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameLauncher extends JFrame {
    
    public GameLauncher() {
        setTitle("Space Shooter - Main Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        initUI();
        setVisible(true);
    }
    
    private void initUI() {
        JPanel menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                g2d.setColor(new Color(10, 10, 30));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(Color.WHITE);
                for (int i = 0; i < 100; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    g2d.fillOval(x, y, 1, 1);
                }
                
                g2d.setColor(Color.CYAN);
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                g2d.drawString("GALAXY DEFENDER", 180, 100);
            }
        };
        menuPanel.setLayout(new GridBagLayout());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 20));
        buttonPanel.setOpaque(false);
        
        JButton singlePlayerBtn = createMenuButton("Single Player");
        JButton multiplayerBtn = createMenuButton("Multiplayer");
        JButton aiBattleBtn = createMenuButton("AI Battle");
        JButton exitBtn = createMenuButton("Exit");
        
        singlePlayerBtn.addActionListener(e -> startGame(GameEngine.GameMode.SINGLE_PLAYER, 2, 1));
        multiplayerBtn.addActionListener(e -> startGame(GameEngine.GameMode.MULTIPLAYER, 2, 1));
        aiBattleBtn.addActionListener(e -> startGame(GameEngine.GameMode.AI_BATTLE, 2, 1));
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(singlePlayerBtn);
        buttonPanel.add(multiplayerBtn);
        buttonPanel.add(aiBattleBtn);
        buttonPanel.add(exitBtn);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(buttonPanel, gbc);
        
        setContentPane(menuPanel);
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(70, 70, 150));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        button.setPreferredSize(new Dimension(250, 50));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(90, 90, 200));
                button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            }
            
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 70, 150));
                button.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
            }
        });
        
        return button;
    }
    
    private void startGame(GameEngine.GameMode mode, int difficulty, int startLevel) {
        dispose();
        
        SwingUtilities.invokeLater(() -> {
            try {
                GameEngine gameEngine = new GameEngine(mode, difficulty, startLevel);
                
                if (mode == GameEngine.GameMode.MULTIPLAYER) {
                    MultiplayerWindow gameWindow = new MultiplayerWindow(gameEngine);
                    gameWindow.setVisible(true);
                } else {
                    GameWindow gameWindow = new GameWindow(gameEngine);
                    gameWindow.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting game: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                new GameLauncher().setVisible(true);
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameLauncher();
        });
    }
}