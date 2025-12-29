package src.structures;


public class DoublyLinkedList<T> {
    class Node {
        T data;
        Node next, prev;
        
        Node(T data) {
            this.data = data;
        }
    }
    
    private Node head;
    private Node tail;
    private int size;
    
    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }
    
    public void printForward() {
        Node current = head;
        System.out.println("\n=== OBJECTIVES ===");
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current.data);
            current = current.next;
            count++;
        }
        System.out.println("==================");
    }
    
    public void printBackward() {
        Node current = tail;
        System.out.println("\n=== COMPLETED OBJECTIVES (Recent First) ===");
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current.data);
            current = current.prev;
            count++;
        }
    }
    
    public int size() {
        return size;
    }
    
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }
    
    public void completeObjective(int index) {
        if (index >= 0 && index < size) {
            Node current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            current.data = (T)("[COMPLETED] " + current.data.toString());
        }
    }
}
