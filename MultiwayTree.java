import java.util.*;

public class MultiwayTree {
    // Vertex id'lerini TreeNode olarak saklar.
    HashMap<String, TreeNode> nodeMap;
    Graph graph;
    String originalRoot; // İlk girilen vertex (Prim's icin)

    public MultiwayTree(Graph graph) {
        this.graph = graph;
        this.nodeMap = new HashMap<>();
    }

    // Prim's algoritmasindan elde edilen pred haritasina gore MST'yi olusturur.
    public void buildFromPredecessors(HashMap<Vertex, Vertex> pred, String rootId) {
        nodeMap.clear();
        originalRoot = rootId;
        
        // nodeleri olustur
        for (Vertex v : graph.getAllVertices()) {
            nodeMap.put(v.id, new TreeNode(v));
        }
        
        // Parent-child iliskilerini kur
        for (Vertex v : graph.getAllVertices()) {
            Vertex p = pred.get(v);
            if (p != null) {
                linkChild(p.id, v.id);
            }
        }
    }

    // pId altina, cId nodeunu alfabetik siraya gore ekler.
     
    public void linkChild(String pId, String cId) {
        TreeNode parent = nodeMap.get(pId);
        TreeNode child = nodeMap.get(cId);
        
        if (parent == null || child == null) return;
        
        // Eger child zaten bir parent'a bagliysa, once onu ayir
        if (child.parent != null) {
            removeChild(child.parent, child);
        }
        
        child.parent = parent;
        
        // Parent'in hic cocugu yoksa, ilk cocuk olarak ekle
        if (parent.firstChild == null) {
            parent.firstChild = child;
            child.nextSibling = null;
            child.prevSibling = null;
            return;
        }
        
        // Alfabetik siraya gore ekle
        TreeNode current = parent.firstChild;
        TreeNode prev = null;
        
        while (current != null && current.vertex.id.compareTo(child.vertex.id) < 0) {
            prev = current;
            current = current.nextSibling;
        }
        
        // Basa ekle
        if (prev == null) {
            child.nextSibling = parent.firstChild;
            child.prevSibling = null;
            parent.firstChild.prevSibling = child;
            parent.firstChild = child;
        } 
        // Ortaya veya sona ekle
        else {
            child.nextSibling = current;
            child.prevSibling = prev;
            prev.nextSibling = child;
            if (current != null) {
                current.prevSibling = child;
            }
        }
    }

    // parent'tan child'i cikarir.
     
    public void removeChild(TreeNode parent, TreeNode child) {
        if (parent.firstChild == child) {
            parent.firstChild = child.nextSibling;
            if (child.nextSibling != null) {
                child.nextSibling.prevSibling = null;
            }
        } else {
            if (child.prevSibling != null) {
                child.prevSibling.nextSibling = child.nextSibling;
            }
            if (child.nextSibling != null) {
                child.nextSibling.prevSibling = child.prevSibling;
            }
        }
        
        child.parent = null;
        child.nextSibling = null;
        child.prevSibling = null;
    }

    // İki node arasindaki yolu bulur.
     
    public List<String> findPath(String start, String end) {
        if (start.equals(end)) {
            List<String> path = new ArrayList<>();
            path.add(start);
            return path;
        }
        
        // Agactaki tum kenarlari komsuluk listesi olarak hazirla
        Map<String, List<String>> adjList = new HashMap<>();
        for (String id : nodeMap.keySet()) {
            adjList.put(id, new ArrayList<>());
        }
        
        for (TreeNode node : nodeMap.values()) {
            if (node.parent != null) {
                String nodeId = node.vertex.id;
                String parentId = node.parent.vertex.id;
                adjList.get(nodeId).add(parentId);
                adjList.get(parentId).add(nodeId);
            }
        }
        
        // BFS ile yolu bul
        Map<String, String> parentMap = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(start);
        visited.add(start);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(end)) {
                break;
            }
            
            for (String neighbor : adjList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                    parentMap.put(neighbor, current);
                }
            }
        }
        
        // Yolu olustur
        List<String> path = new ArrayList<>();
        String current = end;
        
        if (!parentMap.containsKey(end) && !start.equals(end)) {
            return path; // Yol bulunamadi
        }
        
        while (current != null) {
            path.add(0, current);
            current = parentMap.get(current);
        }
        
        return path;
    }

    // İki node arasindaki yolu yazdirir.
     
    public void printPath(String start, String end) {
        if (start.equals(end)) {
            System.out.println(start);
            return;
        }
        
        List<String> path = findPath(start, end);
        
        if (path.isEmpty() || path.size() < 2) {
            // Yol bulunamadi
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1) {
                sb.append(", ");
            }
        }
        
        System.out.println(sb.toString());
    }

    /**
     * MST'ye yeni bir kenar eklenir veya mevcut kenarin agirligi azaltilir.
     * İslem sonucunda MST guncellenebilir.
     */
    public void insertOrDecreaseEdge(String u, String v, float weight) {
        // once kenarin MST'ye eklenip eklenmeyecegini belirle
        if (!shouldReplaceEdge(u, v, weight)) {
            return;
        }
        
        // MST'deki tum kenarlari komsuluk listesi olarak hazirla
        Map<String, List<String>> adj = new HashMap<>();
        for (String id : nodeMap.keySet()) {
            adj.put(id, new ArrayList<>());
        }
        
        for (TreeNode node : nodeMap.values()) {
            if (node.parent != null) {
                String nodeId = node.vertex.id;
                String parentId = node.parent.vertex.id;
                adj.get(nodeId).add(parentId);
                adj.get(parentId).add(nodeId);
            }
        }
        
        // BFS ile u'dan v'ye yolu bul
        Map<String, String> parent = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(u);
        visited.add(u);
        parent.put(u, null);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(v)) {
                break;
            }
            
            for (String neighbor : adj.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        
        // Yol yoksa cik
        if (!parent.containsKey(v)) {
            return;
        }
        
        // Yolu olustur
        List<String> path = new ArrayList<>();
        String current = v;
        
        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }
        
        // Yol uzerindeki en agir kenari bul
        String maxEdgeSource = null;
        String maxEdgeDest = null;
        float maxWeight = -1;
        
        for (int i = 0; i < path.size() - 1; i++) {
            String v1 = path.get(i);
            String v2 = path.get(i+1);
            float edgeWeight = graph.getEdgeWeight(v1, v2);
            
            if (edgeWeight > maxWeight) {
                maxWeight = edgeWeight;
                maxEdgeSource = v1;
                maxEdgeDest = v2;
            }
        }
        
        // Eger yeni kenar daha hafifse, MST'yi guncelle
        if (weight < maxWeight) {
            TreeNode srcNode = nodeMap.get(maxEdgeSource);
            TreeNode destNode = nodeMap.get(maxEdgeDest);
            
            // En agir kenari cikar
            if (srcNode.parent == destNode) {
                removeChild(destNode, srcNode);
            } else if (destNode.parent == srcNode) {
                removeChild(srcNode, destNode);
            }
            
            // Yeni kenari ekle
            linkChild(u, v);
            
            // MST'yi yeniden yapilandir
            rebuildMST();
        }
    }

    // MST'yi yeniden yapilandirir, baglantilari duzeltir.
     
    private void rebuildMST() {
        // Kok nodeu bul
        String rootId = null;
        for (TreeNode node : nodeMap.values()) {
            if (node.parent == null) {
                rootId = node.vertex.id;
                break;
            }
        }
        
        if (rootId == null) {
            // Kok bulunamadiysa, orijinal koku kullan
            rootId = originalRoot;
        }
        
        // MST'yi Prim algoritmasi ile yeniden olustur
        MultiwayTree newMST = PrimMST.buildMST(graph, rootId);
        
        // Yeni MST'den nodeleri ve baglantilari al
        this.nodeMap = newMST.nodeMap;
    }
    
    // Verilen kenarin MST'ye eklenip eklenmeyecegini belirler.
     
    private boolean shouldReplaceEdge(String u, String v, float weight) {
        // Agactaki tum kenarlari komsuluk listesi olarak hazirla
        Map<String, List<String>> adj = new HashMap<>();
        for (String id : nodeMap.keySet()) {
            adj.put(id, new ArrayList<>());
        }
        
        for (TreeNode node : nodeMap.values()) {
            if (node.parent != null) {
                String nodeId = node.vertex.id;
                String parentId = node.parent.vertex.id;
                adj.get(nodeId).add(parentId);
                adj.get(parentId).add(nodeId);
            }
        }
        
        // BFS ile yolu bul
        Map<String, String> parent = new HashMap<>();
        Map<String, Float> maxEdgeWeight = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        
        queue.offer(u);
        parent.put(u, null);
        maxEdgeWeight.put(u, 0.0f);
        
        while (!queue.isEmpty() && !parent.containsKey(v)) {
            String current = queue.poll();
            
            for (String neighbor : adj.get(current)) {
                if (!parent.containsKey(neighbor)) {
                    float edgeW = graph.getEdgeWeight(current, neighbor);
                    float pathMaxW = Math.max(maxEdgeWeight.get(current), edgeW);
                    
                    parent.put(neighbor, current);
                    maxEdgeWeight.put(neighbor, pathMaxW);
                    queue.offer(neighbor);
                }
            }
        }
        
        // Eger yol bulunduysa ve yol uzerindeki max agirlik yeni kenardan buyukse
        if (parent.containsKey(v) && maxEdgeWeight.get(v) > weight) {
            return true;
        }
        
        return false;
    }

    // graphta yeni bir kenar eklenir veya mevcut kenarin agirligi guncellenir.
     
    public void updateInsertEdge(String u, String v, float w) {
        // once graphi guncelle
        graph.insertOrDecreaseEdge(u, v, w);
        
        // Eger MST'yi etkileyecekse, MST'yi guncelle
        insertOrDecreaseEdge(u, v, w);
    }

    // Verilen nodeu agacin koku yapar.
     
    public void evert(String id) {
        TreeNode node = nodeMap.get(id);
        if (node == null || node.parent == null) {
            return; // Zaten kok veya node yok
        }
        
        // MST'yi Prim algoritmasi ile yeniden olustur
        MultiwayTree newMST = PrimMST.buildMST(graph, id);
        
        // Yeni MST'den nodeleri ve baglantilari al
        this.nodeMap = newMST.nodeMap;
    }

    // MST'yi verilen kokten baslayarak yazdirir.
     
    public void printMST(String rootId) {
        // once verilen nodeu kok olarak ayarla
        evert(rootId);
        
        // Koku yazdir
        System.out.println(rootId);
        
        // Komsuluk listesi olustur
        Map<String, List<String>> adjList = new HashMap<>();
        for (String id : nodeMap.keySet()) {
            adjList.put(id, new ArrayList<>());
        }
        
        for (TreeNode node : nodeMap.values()) {
            if (node.parent != null) {
                String nodeId = node.vertex.id;
                String parentId = node.parent.vertex.id;
                adjList.get(parentId).add(nodeId);
            }
        }
        
        // Komsulari alfabetik siraya gore sirala
        for (List<String> neighbors : adjList.values()) {
            Collections.sort(neighbors);
        }
        
        // DFS ile agaci yazdir
        Set<String> visited = new HashSet<>();
        visited.add(rootId);
        printMSTDFS(rootId, adjList, visited, 1);
    }
    
    // DFS ile MST'yi yazdirir.
     
    private void printMSTDFS(String current, Map<String, List<String>> adjList, Set<String> visited, int depth) {
        List<String> neighbors = adjList.get(current);
        
        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                
                // Girinti ekle
                for (int i = 0; i < depth; i++) {
                    System.out.print(". ");
                }
                
                // nodeu yazdir
                System.out.println(neighbor);
                
                // Alt nodeleri yazdir
                printMSTDFS(neighbor, adjList, visited, depth + 1);
            }
        }
    }
} 