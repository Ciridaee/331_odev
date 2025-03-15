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

        // MinHeap kullanarak Prim algoritmasını uygula
        MinHeap minHeap = new MinHeap();
        
        // Tüm düğümlere sonsuz anahtar değeri ver
        for (Vertex v : allVerts) {
            minHeap.insert(v, Float.POSITIVE_INFINITY);
        }
        
        // Kök düğümün anahtar değerini 0 olarak ayarla
        minHeap.decreaseKey(root, 0f);

        while (!minHeap.isEmpty()) {
            // En küçük anahtar değerine sahip düğümü çıkar
            MinHeap.HeapNode minNode = minHeap.extractMin();
            Vertex u = minNode.vertex;
            
            inMST.put(u, true);
            
            // Komşuları alfabetik sıraya göre işle
            List<Edge> sortedEdges = new ArrayList<>(u.adjList);
            Collections.sort(sortedEdges, (e1, e2) -> e1.dest.id.compareTo(e2.dest.id));
            
            for (Edge e : sortedEdges) {
                Vertex v = e.dest;
                
                // Eğer düğüm MST'de değilse ve kenar ağırlığı mevcut anahtar değerinden küçükse
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