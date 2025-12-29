package src.structures;

public class SinglyLinkedList<T> {
    class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
        }
    }
    
    private Node head;
    private int size;
    
    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }
    
    public void remove(T data) {
        if (head == null) return;
        
        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return;
        }
        
        Node current = head;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }
        
        if (current.next != null) {
            current.next = current.next.next;
            size--;
        }
    }
    
    public void clear() {
        head = null;
        size = 0;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
    
    // For game - get all bullets as array
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }
}