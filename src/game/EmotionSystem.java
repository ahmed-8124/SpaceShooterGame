package src.game;

import java.util.*;

public class EmotionSystem {
    private List<String> emotions;
    private int currentEmotionIndex;
    private long lastEmotionChange;
    private long lastAbilityUse;
    private static final long EMOTION_CHANGE_COOLDOWN = 8000;
    private static final long ABILITY_COOLDOWN = 30000; // 30 seconds
    private Random random;
    
    public EmotionSystem() {
        emotions = Arrays.asList("RAGE", "CALM", "FOCUS", "JOY");
        random = new Random();
        currentEmotionIndex = 0;
        lastEmotionChange = System.currentTimeMillis();
        lastAbilityUse = 0;
    }
    
    public void update() {
        long currentTime = System.currentTimeMillis();
        
        // Randomly change emotion every 8 seconds
        if (currentTime - lastEmotionChange > EMOTION_CHANGE_COOLDOWN) {
            changeEmotion();
            lastEmotionChange = currentTime;
        }
    }
    
    private void changeEmotion() {
        int newIndex;
        do {
            newIndex = random.nextInt(emotions.size());
        } while (newIndex == currentEmotionIndex && emotions.size() > 1);
        
        currentEmotionIndex = newIndex;
        System.out.println("Emotion changed to: " + getCurrentEmotion());
    }
    
    public String getCurrentEmotion() {
        return emotions.get(currentEmotionIndex);
    }
    
    public boolean canUseAbility() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastAbilityUse) > ABILITY_COOLDOWN;
    }
    
    public void useAbility() {
        if (canUseAbility()) {
            lastAbilityUse = System.currentTimeMillis();
            System.out.println("Using " + getCurrentEmotion() + " ability!");
        }
    }
    
    public long getAbilityCooldownRemaining() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastAbilityUse;
        return Math.max(0, ABILITY_COOLDOWN - elapsed);
    }
    
    public String getCooldownText() {
        long remaining = getAbilityCooldownRemaining() / 1000;
        return remaining > 0 ? remaining + "s" : "READY";
    }
}