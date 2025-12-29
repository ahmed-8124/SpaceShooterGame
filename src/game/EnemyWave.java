package src.game;

public class EnemyWave {
    private int count;
    private float speed;
    
    public EnemyWave(int count, float speed) {
        this.count = count;
        this.speed = speed;
    }
    
    public int getCount() { return count; }
    public float getSpeed() { return speed; }
    
    @Override
    public String toString() {
        return "Wave[" + count + " enemies, speed=" + speed + "]";
    }
}