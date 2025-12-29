package src.structures;

import java.util.EmptyStackException;

public class StackDS<T> {
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
        }
    }
    
    private Node<T> top;
    private int size;
    
    public void push(T value) {
        Node<T> newNode = new Node<>(value);
        newNode.next = top;
        top = newNode;
        size++;
    }
    
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T value = top.data;
        top = top.next;
        size--;
        return value;
    }
    
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return top.data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        top = null;
        size = 0;
    }
    
    public void printStack() {
        System.out.print("Combo Stack (top->bottom): ");
        Node<T> current = top;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
    
    public boolean containsCombo() {
        if (size < 3) return false;
        
        // Check for three consecutive identical moves
        Node<T> current = top;
        T firstMove = current.data;
        int count = 0;
        
        while (current != null && count < 3) {
            if (!current.data.equals(firstMove)) {
                return false;
            }
            current = current.next;
            count++;
        }
        return count == 3;
    }
}