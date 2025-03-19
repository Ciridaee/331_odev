import java.util.*;

/**
 * Prim algoritmasi icin minimum heap oncelik kuyrugu.
 * Her nodeun heap icindeki konumunu hizli erisim icin tutacak.
 */
public class MinHeap {
    private ArrayList<HeapNode> heap;
    private HashMap<Vertex, Integer> vertexToIndex; // vertex'den heap indeksine cross-reference

    public MinHeap() {
        heap = new ArrayList<>();
        vertexToIndex = new HashMap<>();
    }

    /**
     * Heap'e bir node ekler
     */
    public void insert(Vertex vertex, float key) {
        HeapNode node = new HeapNode(vertex, key);
        heap.add(node);
        int currentIndex = heap.size() - 1;
        vertexToIndex.put(vertex, currentIndex);
        
        // Heap ozelligini korumak icin yukari kaydirma (sift up)
        siftUp(currentIndex);
    }

    /**
     * En kucuk keye sahip nodeu cikarir
     */
    public HeapNode extractMin() {
        if (isEmpty()) {
            return null;
        }
        
        HeapNode min = heap.get(0);
        int lastIndex = heap.size() - 1;
        
        // Son elemani koke tasi
        heap.set(0, heap.get(lastIndex));
        vertexToIndex.put(heap.get(0).vertex, 0);
        
        // Son elemani kaldir
        heap.remove(lastIndex);
        vertexToIndex.remove(min.vertex);
        
        // Heap bos degilse asagi kaydir
        if (!isEmpty()) {
            siftDown(0);
        }
        
        return min;
    }

    /**
     * Belirli bir vertex'in key degerini azaltir ve heap'i yeniden duzenler
     */
    public void decreaseKey(Vertex vertex, float newKey) {
        if (!vertexToIndex.containsKey(vertex)) {
            return;
        }
        
        int index = vertexToIndex.get(vertex);
        if (newKey >= heap.get(index).key) {
            return; // Yeni key daha kucuk olmali
        }
        
        heap.get(index).key = newKey;
        siftUp(index);
    }

    
     // Heap bos mu kontrol eder
     
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Heap'teki node sayisini dondurur
     
    public int size() {
        return heap.size();
    }

    // Bir vertex'in heap'te olup olmadigini kontrol eder
     
    public boolean contains(Vertex vertex) {
        return vertexToIndex.containsKey(vertex);
    }

    // Bir vertex'in key degerini dondurur
     
    public float getKey(Vertex vertex) {
        if (!vertexToIndex.containsKey(vertex)) {
            return Float.POSITIVE_INFINITY;
        }
        
        int index = vertexToIndex.get(vertex);
        return heap.get(index).key;
    }

    // Yukari kaydirma islemi (Min-Heap sirasini korumak icin)
     
    private void siftUp(int index) {
        int parentIndex = (index - 1) / 2;
        
        while (index > 0 && heap.get(index).key < heap.get(parentIndex).key) {
            // nodeleri degistir
            swap(index, parentIndex);
            
            // İndeksleri guncelle
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    // Asagi kaydirma islemi (Min-Heap sirasini korumak icin)
     
    private void siftDown(int index) {
        int smallest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        
        // Sol cocuk daha kucukse
        if (leftChild < heap.size() && heap.get(leftChild).key < heap.get(smallest).key) {
            smallest = leftChild;
        }
        
        // Sag cocuk daha kucukse
        if (rightChild < heap.size() && heap.get(rightChild).key < heap.get(smallest).key) {
            smallest = rightChild;
        }
        
        // Eger index en kucuk degilse, nodeleri degistir ve recursively devam et
        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest);
        }
    }

    // İki nodeun yerini degistirir
     
    private void swap(int i, int j) {
        HeapNode temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
        
        // Cross-reference tablosunu guncelle
        vertexToIndex.put(heap.get(i).vertex, i);
        vertexToIndex.put(heap.get(j).vertex, j);
    }

    // Heap icinde depolanan node sinifi
     
    public static class HeapNode {
        public Vertex vertex;
        public float key;
        
        public HeapNode(Vertex vertex, float key) {
            this.vertex = vertex;
            this.key = key;
        }
    }
} 