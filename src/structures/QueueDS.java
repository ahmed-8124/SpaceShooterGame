package src.structures;

public class QueueDS<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
        }
    }
    
    private Node<T> front;
    private Node<T> rear;
    private int size;
    
    public void enqueue(T value) {
        Node<T> newNode = new Node<>(value);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T value = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return value;
    }
    
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }
    
    public boolean isEmpty() {
        return front == null;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        front = rear = null;
        size = 0;
    }
    
    public void printQueue() {
        System.out.print("Queue (front->rear): ");
        Node<T> current = front;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}