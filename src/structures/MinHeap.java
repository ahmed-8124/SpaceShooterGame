package src.structures;

public class MinHeap {
    private int[][] heap;
    private int size;
    private int capacity;
    
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.heap = new int[capacity][3]; // distance, x, y
        this.size = 0;
    }
    
    public void insert(int[] obstacle) {
        if (size == capacity) return;
        
        heap[size] = obstacle;
        int current = size;
        size++;
        
        while (current > 0 && heap[current][0] < heap[parent(current)][0]) {
            swap(current, parent(current));
            current = parent(current);
        }
    }
    
    public int[] extractMin() {
        if (size == 0) return null;
        
        int[] min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapify(0);
        
        return min;
    }
    
    public int[] peek() {
        return size > 0 ? heap[0] : null;
    }
    
    private void heapify(int i) {
        int left = leftChild(i);
        int right = rightChild(i);
        int smallest = i;
        
        if (left < size && heap[left][0] < heap[smallest][0])
            smallest = left;
        if (right < size && heap[right][0] < heap[smallest][0])
            smallest = right;
            
        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }
    
    private int parent(int i) { return (i - 1) / 2; }
    private int leftChild(int i) { return 2 * i + 1; }
    private int rightChild(int i) { return 2 * i + 2; }
    
    private void swap(int i, int j) {
        int[] temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    public void clear() { size = 0; }
}