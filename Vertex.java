import java.util.*;

public class Vertex {
    public String id;
    public List<Edge> adjList;
    
    public Vertex(String id) {
        this.id = id;
        this.adjList = new ArrayList<>();
    }
} 