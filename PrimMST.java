import java.util.*;

class PrimMST {

    public static MultiwayTree buildMST(Graph graph, String rootId) {
        ArrayList<Vertex> allVerts = new ArrayList<>(graph.getAllVertices());
        HashMap<Vertex, Vertex> pred = new HashMap<>();
        HashMap<Vertex, Boolean> inMST = new HashMap<>();

        for (Vertex v : allVerts) {
            pred.put(v, null);
            inMST.put(v, false);
        }

        Vertex root = graph.getVertex(rootId);
        if (root == null) return new MultiwayTree(graph);

        // MinHeap kullanarak Primi uygula
        MinHeap minHeap = new MinHeap();
        
        // Tum nodelere sonsuz key degeri ver
        for (Vertex v : allVerts) {
            minHeap.insert(v, Float.POSITIVE_INFINITY);
        }
        
        // Kok nodeun key degerini 0 olarak ayarla
        minHeap.decreaseKey(root, 0f);

        while (!minHeap.isEmpty()) {
            // En kucuk key degerine sahip nodeu cikar
            MinHeap.HeapNode minNode = minHeap.extractMin();
            Vertex u = minNode.vertex;
            
            inMST.put(u, true);
            
            // Komsulari alfabetik siraya gore isle
            List<Edge> sortedEdges = new ArrayList<>(u.adjList);
            Collections.sort(sortedEdges, (e1, e2) -> e1.dest.id.compareTo(e2.dest.id));
            
            for (Edge e : sortedEdges) {
                Vertex v = e.dest;
                
                // Eger node MST'de degilse ve kenar agirligi mevcut key degerinden kucukse
                if (!inMST.get(v) && e.weight < minHeap.getKey(v)) {
                    pred.put(v, u);
                    minHeap.decreaseKey(v, e.weight);
                }
            }
        }
        
        MultiwayTree mst = new MultiwayTree(graph);
        mst.buildFromPredecessors(pred, rootId);
        return mst;
    }
} 