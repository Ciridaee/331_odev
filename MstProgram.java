import java.io.*;
import java.util.*;

public class MstProgram {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: java MstProgram <inputfile>");
            return;
        }
        String filename = args[0];
        Scanner in = new Scanner(new File(filename));

        // Read graph input: vertices and edges.
        int numVertices = in.nextInt();
        in.nextLine();
        Graph graph = new Graph();
        ArrayList<String> vertexOrder = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            String vid = in.nextLine().trim();
            graph.addVertex(vid);
            vertexOrder.add(vid);
        }
        int numEdges = in.nextInt();
        in.nextLine();
        for (int i = 0; i < numEdges; i++) {
            String line = in.nextLine().trim();
            String[] parts = line.split("\\s+");
            String u = parts[0];
            String v = parts[1];
            float w = Float.parseFloat(parts[2]);
            graph.addEdge(u, v, w);
        }

        // Build the initial MST once using Prim's algorithm.
        String rootId = vertexOrder.get(0);
        MultiwayTree mst = PrimMST.buildMST(graph, rootId);

        // Process directives incrementally.
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.isEmpty())
                continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0];
            if (cmd.equals("quit"))
                break;
            System.out.println("Directive-----------------> " + line);
            if (cmd.equals("print-mst")) {
                if (parts.length < 2)
                    continue;
                String r = parts[1];
                mst.printMST(r);
            } else if (cmd.equals("path")) {
                if (parts.length < 3)
                    continue;
                String u = parts[1];
                String v = parts[2];
                mst.printPath(u, v);
            } else if (cmd.equals("insert-edge")) {
                if (parts.length < 4)
                    continue;
                String u = parts[1];
                String v = parts[2];
                float w = Float.parseFloat(parts[3]);
                
                boolean ok = graph.insertEdge(u, v, w);
                if (!ok) {
                    System.out.println("Invalid Operation");
                } else {
                    // Update MST incrementally.
                    mst.updateInsertEdge(u, v, w);
                }
            } else if (cmd.equals("decrease-weight")) {
                if (parts.length < 4)
                    continue;
                String u = parts[1];
                String v = parts[2];
                float dw = Float.parseFloat(parts[3]);
                boolean ok = graph.decreaseWeight(u, v, dw);
                if (!ok) {
                    System.out.println("Invalid Operation");
                } else {
                    float newWeight = graph.getEdgeWeight(u, v);
                    mst.updateInsertEdge(u, v, newWeight);
                }
            }
        }
        in.close();
    }
}
