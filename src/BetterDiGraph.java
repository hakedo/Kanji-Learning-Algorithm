import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Implementation of Graph Data Structure using HashMaps and generics
 * @author Edgar Hakobyan
 * @version 1.2
 */
public class BetterDiGraph implements EditableDiGraph {

    /** Number of vertices in a graph */
    private int V;

    /** Number of edges in a graph */
    private int E;

    /** Key and Adjacency list pair for vertices */
    private HashMap<Integer, LinkedList<Integer>> adj;

    /**
     * Default constructor for BetterDiGraph that
     * creates an empty graph
     */
    public BetterDiGraph() {
        this.V = 0;
        this.E = 0;
        this.adj = new HashMap<>();
    }

    @Override
    public void addEdge(int v, int w) {
        // checks graph for already existing vertex
        if (!adj.containsKey(v)) {
            addVertex(v);
        }
        // checks graph for already existing vertex
        if (!adj.containsKey(w)) {
            addVertex(w);
        }
        adj.get(v).push(w);
        E++;
    }

    @Override
    public void addVertex(int v) {
        // checks to see if vertex is already in the graph.
        // if not, then adds a new vertex with adjacency LinkedList
        if (!adj.containsKey(v)) {
            adj.put(v, new LinkedList<>());
            V++;
        }
    }

    @Override
    public Iterable<Integer> getAdj(int v) { return this.adj.get(v); }

    @Override
    public int getEdgeCount() { return E; }

    @Override
    public int getVertexCount() { return V; }

    @Override
    public int getIndegree(int v) throws NoSuchElementException {
        if (!adj.containsKey(v)) {
            throw new NoSuchElementException();
        }
        int degree = 0;
        /*
         * Iterate over a list of vertices
         * Count how many times vertex V appears
         * in other vertices' adjacency list.
         * (see how many pointers exist to V)
         */
        for (Integer x: vertices()) {
            if (x != v) {
                if (adj.get(x).contains(v)) {
                    degree++;
                }
            }
        }
        return degree;
    }

    @Override
    public void removeEdge(int v, int w) {
        if (!adj.containsKey(v)) {
            return;
        }
        if (adj.get(v).contains(w)) {
            adj.get(v).remove(w);
            E--;
        }
    }

    @Override
    public void removeVertex(int v) {
        if (!adj.containsKey(v)) {
            return;
        }
        // Step 1: Remove all edge references of v from all other vertices
        // Step 2: Remove vertex v itself
        for (Integer x: vertices()) {
            if (x != v) {
                if (adj.get(x).contains(v)) {
                    removeEdge(x, v);
                }
            }
        }
        adj.remove(v);
        V--;
    }

    @Override
    public Iterable<Integer> vertices() { return adj.keySet(); }

    @Override
    public boolean isEmpty() { return V == 0; }

    @Override
    public boolean containsVertex(int v) { return this.adj.containsKey(v); }
}
