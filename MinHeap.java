import java.util.*;

/**
 * Prim algoritması için minimum heap öncelik kuyruğu.
 * Her düğümün heap içindeki konumunu hızlı erişim için tutacak.
 */
public class MinHeap {
    private ArrayList<HeapNode> heap;
    private HashMap<Vertex, Integer> vertexToIndex; // vertex'den heap indeksine cross-reference

    public MinHeap() {
        heap = new ArrayList<>();
        vertexToIndex = new HashMap<>();
    }

    /**
     * Heap'e bir düğüm ekler
     */
    public void insert(Vertex vertex, float key) {
        HeapNode node = new HeapNode(vertex, key);
        heap.add(node);
        int currentIndex = heap.size() - 1;
        vertexToIndex.put(vertex, currentIndex);
        
        // Heap özelliğini korumak için yukarı kaydırma (sift up)
        siftUp(currentIndex);
    }

    /**
     * En küçük anahtara sahip düğümü çıkarır
     */
    public HeapNode extractMin() {
        if (isEmpty()) {
            return null;
        }
        
        HeapNode min = heap.get(0);
        int lastIndex = heap.size() - 1;
        
        // Son elemanı köke taşı
        heap.set(0, heap.get(lastIndex));
        vertexToIndex.put(heap.get(0).vertex, 0);
        
        // Son elemanı kaldır
        heap.remove(lastIndex);
        vertexToIndex.remove(min.vertex);
        
        // Heap boş değilse aşağı kaydır
        if (!isEmpty()) {
            siftDown(0);
        }
        
        return min;
    }

    /**
     * Belirli bir vertex'in key değerini azaltır ve heap'i yeniden düzenler
     */
    public void decreaseKey(Vertex vertex, float newKey) {
        if (!vertexToIndex.containsKey(vertex)) {
            return;
        }
        
        int index = vertexToIndex.get(vertex);
        if (newKey >= heap.get(index).key) {
            return; // Yeni anahtar daha küçük olmalı
        }
        
        heap.get(index).key = newKey;
        siftUp(index);
    }

    /**
     * Heap boş mu kontrol eder
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Heap'teki düğüm sayısını döndürür
     */
    public int size() {
        return heap.size();
    }

    /**
     * Bir vertex'in heap'te olup olmadığını kontrol eder
     */
    public boolean contains(Vertex vertex) {
        return vertexToIndex.containsKey(vertex);
    }

    /**
     * Bir vertex'in anahtar değerini döndürür
     */
    public float getKey(Vertex vertex) {
        if (!vertexToIndex.containsKey(vertex)) {
            return Float.POSITIVE_INFINITY;
        }
        
        int index = vertexToIndex.get(vertex);
        return heap.get(index).key;
    }

    /**
     * Yukarı kaydırma işlemi (Min-Heap sırasını korumak için)
     */
    private void siftUp(int index) {
        int parentIndex = (index - 1) / 2;
        
        while (index > 0 && heap.get(index).key < heap.get(parentIndex).key) {
            // Düğümleri değiştir
            swap(index, parentIndex);
            
            // İndeksleri güncelle
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    /**
     * Aşağı kaydırma işlemi (Min-Heap sırasını korumak için)
     */
    private void siftDown(int index) {
        int smallest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        
        // Sol çocuk daha küçükse
        if (leftChild < heap.size() && heap.get(leftChild).key < heap.get(smallest).key) {
            smallest = leftChild;
        }
        
        // Sağ çocuk daha küçükse
        if (rightChild < heap.size() && heap.get(rightChild).key < heap.get(smallest).key) {
            smallest = rightChild;
        }
        
        // Eğer index en küçük değilse, düğümleri değiştir ve recursively devam et
        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest);
        }
    }

    /**
     * İki düğümün yerini değiştirir
     */
    private void swap(int i, int j) {
        HeapNode temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
        
        // Cross-reference tablosunu güncelle
        vertexToIndex.put(heap.get(i).vertex, i);
        vertexToIndex.put(heap.get(j).vertex, j);
    }

    /**
     * Heap içinde depolanan düğüm sınıfı
     */
    public static class HeapNode {
        public Vertex vertex;
        public float key;
        
        public HeapNode(Vertex vertex, float key) {
            this.vertex = vertex;
            this.key = key;
        }
    }
} 