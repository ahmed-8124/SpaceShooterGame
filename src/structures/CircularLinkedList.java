package src.structures;


public class CircularLinkedList<T> {
    class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
        }
    }
    
    private Node head;
    private Node current;
    private int size;
    
    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            head.next = head; // Points to itself
            current = head;
        } else {
            Node temp = head;
            while (temp.next != head) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.next = head;
        }
        size++;
    }
    
    public T getNext() {
        if (current == null) return null;
        T data = current.data;
        current = current.next;
        return data;
    }
    
    public void reset() {
        current = head;
    }
    
    public void showCycle() {
        if (head == null) {
            System.out.println("Cycle is empty");
            return;
        }
        
        System.out.println("\n=== POWER-UP CYCLE ===");
        Node temp = head;
        int count = 1;
        do {
            System.out.println(count + ". " + temp.data);
            temp = temp.next;
            count++;
        } while (temp != head);
        System.out.println("Next: " + current.data);
    }
    
    public int size() {
        return size;
    }
    
    public T peekNext() {
        return current != null ? current.data : null;
    }
}
