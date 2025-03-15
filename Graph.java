import java.util.*;

class Graph {
    private HashMap<String, Vertex> vertices;

    public Graph() {
        vertices = new HashMap<>();
    }

    public void addVertex(String id) {
        if (!vertices.containsKey(id)) {
            vertices.put(id, new Vertex(id));
        }
    }

    public void addEdge(String u, String v, float w) {
        Vertex vu = vertices.get(u);
        Vertex vv = vertices.get(v);
        if (vu == null || vv == null) return;
        vu.adjList.add(new Edge(vv, w));
        vv.adjList.add(new Edge(vu, w));
    }

    public boolean insertEdge(String u, String v, float w) {
        Vertex vu = vertices.get(u);
        Vertex vv = vertices.get(v);
        if (vu == null || vv == null) return false;
        if (edgeExists(vu, vv)) return false;
        vu.adjList.add(new Edge(vv, w));
        vv.adjList.add(new Edge(vu, w));
        return true;
    }

    public boolean decreaseWeight(String u, String v, float dw) {
        Vertex vu = vertices.get(u);
        Vertex vv = vertices.get(v);
        if (vu == null || vv == null) return false;
        boolean found = false;
        for (Edge e : vu.adjList) {
            if (e.dest == vv) {
                e.weight -= dw;
                if (e.weight <= 0) e.weight = 0.000001f;
                found = true;
                break;
            }
        }
        if (found) {
            for (Edge e : vv.adjList) {
                if (e.dest == vu) {
                    e.weight -= dw;
                    if (e.weight <= 0) e.weight = 0.000001f;
                    break;
                }
            }
        }
        return found;
    }

    public float getEdgeWeight(String u, String v) {
        Vertex vu = vertices.get(u);
        Vertex vv = vertices.get(v);
        if (vu == null || vv == null) return Float.POSITIVE_INFINITY;
        for (Edge e : vu.adjList) {
            if (e.dest == vv) return e.weight;
        }
        return Float.POSITIVE_INFINITY;
    }

    private boolean edgeExists(Vertex u, Vertex v) {
        for (Edge e : u.adjList) {
            if (e.dest == v) return true;
        }
        return false;
    }

    public Vertex getVertex(String id) {
        return vertices.get(id);
    }

    public Collection<Vertex> getAllVertices() {
        return vertices.values();
    }

    /**
     * Grafta yeni bir kenar ekler veya mevcut kenarın ağırlığını günceller.
     * Kenar yoksa ekler, varsa ağırlığı verilen değerle günceller.
     */
    public void insertOrDecreaseEdge(String u, String v, float w) {
        Vertex vu = vertices.get(u);
        Vertex vv = vertices.get(v);
        if (vu == null || vv == null) return;
        
        boolean edgeExists = false;
        
        // Kenarın varlığını kontrol et ve ağırlığını güncelle
        for (Edge e : vu.adjList) {
            if (e.dest == vv) {
                if (e.weight > w) {
                    e.weight = w;
                    // Çift yönlü olduğu için diğer tarafı da güncelle
                    for (Edge e2 : vv.adjList) {
                        if (e2.dest == vu) {
                            e2.weight = w;
                            break;
                        }
                    }
                }
                edgeExists = true;
                break;
            }
        }
        
        // Kenar yoksa ekle
        if (!edgeExists) {
            vu.adjList.add(new Edge(vv, w));
            vv.adjList.add(new Edge(vu, w));
        }
    }
} 