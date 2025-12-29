package src.game;

public class LevelLinkedList {
    class LevelNode {
        Level level;
        LevelNode next;
        
        LevelNode(Level level) {
            this.level = level;
        }
    }
    
    private LevelNode head;
    private LevelNode current;
    
    public void addLevel(Level level) {
        LevelNode newNode = new LevelNode(level);
        if (head == null) {
            head = newNode;
            current = head;
        } else {
            LevelNode temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }
    
    public Level getFirst() {
        current = head;
        return current != null ? current.level : null;
    }
    
    public Level getNextLevel() {
        if (current != null && current.next != null) {
            current = current.next;
            return current.level;
        }
        return null;
    }
    
    public Level getCurrentLevel() {
        return current != null ? current.level : null;
    }
    
    public void reset() {
        current = head;
    }
    
    public int getCount() {
        int count = 0;
        LevelNode temp = head;
        while (temp != null) {
            count++;
            temp = temp.next;
        }
        return count;
    }
}