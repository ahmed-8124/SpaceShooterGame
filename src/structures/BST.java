package src.structures;

public class BST {
    class ScoreNode {
        int score;
        String playerName;
        ScoreNode left, right;
        
        ScoreNode(int score, String playerName) {
            this.score = score;
            this.playerName = playerName;
        }
        
        @Override
        public String toString() {
            return playerName + ": " + score + " points";
        }
    }
    
    private ScoreNode root;
    
    public void insert(int score, String playerName) {
        root = insertRec(root, score, playerName);
    }
    
    private ScoreNode insertRec(ScoreNode root, int score, String playerName) {
        if (root == null) {
            return new ScoreNode(score, playerName);
        }
        
        // Higher scores go to left (for descending order)
        if (score > root.score) {
            root.left = insertRec(root.left, score, playerName);
        } else {
            root.right = insertRec(root.right, score, playerName);
        }
        
        return root;
    }
    
    public void inorder() {
        System.out.println("\n=== HIGH SCORES ===");
        inorderRec(root);
        System.out.println("===================");
    }
    
    private void inorderRec(ScoreNode root) {
        if (root != null) {
            inorderRec(root.left);  // Higher scores first
            System.out.println(root);
            inorderRec(root.right); // Lower scores last
        }
    }
    
    public void reverseInorder() {
        System.out.println("\n=== HIGH SCORES (Top 10) ===");
        reverseInorderRec(root, new int[]{0});
        System.out.println("============================");
    }
    
    private void reverseInorderRec(ScoreNode root, int[] count) {
        if (root != null && count[0] < 10) {
            reverseInorderRec(root.right, count); // Start from right for ascending
            if (count[0] < 10) {
                System.out.println((count[0] + 1) + ". " + root);
                count[0]++;
            }
            reverseInorderRec(root.left, count);
        }
    }
    
    public int getHighestScore() {
        if (root == null) return 0;
        ScoreNode current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.score;
    }
}
