public class TreeNode {
    public Vertex vertex;
    public TreeNode parent;
    public TreeNode firstChild;
    public TreeNode nextSibling;
    public TreeNode prevSibling;
    
    public TreeNode(Vertex vertex) {
        this.vertex = vertex;
        this.parent = null;
        this.firstChild = null;
        this.nextSibling = null;
        this.prevSibling = null;
    }
} 