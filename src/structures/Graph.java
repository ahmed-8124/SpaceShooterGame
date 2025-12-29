package src.structures;

import java.util.*;

public class Graph {
    private Map<String, List<String>> adjacencyList;
    
    public Graph() {
        adjacencyList = new HashMap<>();
    }
    
    public void addRoom(String roomName) {
        adjacencyList.putIfAbsent(roomName, new ArrayList<>());
    }
    
    public void connectRooms(String room1, String room2) {
        adjacencyList.putIfAbsent(room1, new ArrayList<>());
        adjacencyList.putIfAbsent(room2, new ArrayList<>());
        
        if (!adjacencyList.get(room1).contains(room2)) {
            adjacencyList.get(room1).add(room2);
        }
        if (!adjacencyList.get(room2).contains(room1)) {
            adjacencyList.get(room2).add(room1);
        }
    }
    
    public List<String> getConnectedRooms(String roomName) {
        return adjacencyList.getOrDefault(roomName, new ArrayList<>());
    }
    
    public List<String> shortestPath(String start, String goal) {
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.add(start);
        visited.add(start);
        parent.put(start, null);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(goal)) {
                return reconstructPath(parent, goal);
            }
            
            for (String neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        return new ArrayList<>(); // No path found
    }
    
    private List<String> reconstructPath(Map<String, String> parent, String goal) {
        List<String> path = new LinkedList<>();
        String current = goal;
        
        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }
        
        return path;
    }
    
    public void printGraph() {
        System.out.println("\n=== SPACE SECTOR MAP ===");
        adjacencyList.forEach((room, neighbors) -> {
            System.out.println(room + " -> " + neighbors);
        });
        System.out.println("=======================");
    }
    
    public Set<String> getAllRooms() {
        return adjacencyList.keySet();
    }
    
    public boolean hasRoom(String roomName) {
        return adjacencyList.containsKey(roomName);
    }
}
