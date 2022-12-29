import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Implementation of intuitive topological sort.
 * Produces a topological sorted graph with elimination instead of DFS
 * @author Edgar Hakobyan
 * @version 1.6
 */
public class IntuitiveTopological implements TopologicalSort {

    HashMap<Integer, Boolean> visitedNodes;
    HashMap<Integer, Boolean> onStack;
    HashMap<Integer, Integer> adjEdges;

    private Iterable<Integer> topologicalOrder;
    private boolean isCyclic;
    DirectedCycle diCycle;
    Stack<Integer> cycle;

    /**
     * Class constructor for producing a topologica order of a graph
     * @param diGraph graph object
     */
    public IntuitiveTopological(BetterDiGraph diGraph) {
        diCycle = new DirectedCycle(diGraph);
        if(!isCyclic) {
            topologyOrder(diGraph);
        }
    }

    /**
     * Algorithm for producing a topological order of a graph
     * @param diGraph graph object
     */
    private void topologyOrder(BetterDiGraph diGraph) {
        LinkedList<Integer> order = new LinkedList<>();

        while (diGraph.getVertexCount() > 0) {
            HashMap<Integer, Integer> indegrees = getInDegrees(diGraph);
            for (Integer i : indegrees.keySet()) {
                if (indegrees.get(i) == 0) {
                    order.add(i);
                    diGraph.removeVertex(i);
                }
            }
        }
        topologicalOrder = order;
    }

    /**
     * DirectedCycle class. Contains methods for checking a cycle in a graph
     */
    public class DirectedCycle {

        /**
         * Constructor for detecting a cycle in a graph
         * @param diGraph graph object
         */
        public DirectedCycle(BetterDiGraph diGraph) {
            visitedNodes = new HashMap<>();
            for (Integer x: diGraph.vertices()) {
                visitedNodes.put(x, false);
            }
            onStack = new HashMap<>();
            adjEdges = new HashMap<>();
            isCyclic = false;

            for (Integer v: diGraph.vertices()) {
                if (!visitedNodes.get(v)) {
                    dfs(diGraph, v);
                }
            }
        }

        /**
         * Depth first search algorithm for checking if graph has a cycle
         * @param diGraph graph object
         * @param v vertex
         */
        private void dfs(BetterDiGraph diGraph, int v) {
            onStack.put(v, true);
            visitedNodes.put(v, true);
            for (Integer w : diGraph.getAdj(v))
                if (this.hasCycle())  {isCyclic = true; return;}
                else if (!visitedNodes.get(w)) {
                    adjEdges.put(w,v);
                    dfs(diGraph, w);
                }
                else if (onStack.get(w)) {
                    cycle = new Stack<Integer>();
                    for (Integer x = v; x != w; x = adjEdges.get(x)) {
                        cycle.push(x);
                    }
                    cycle.push(w);
                    cycle.push(v);
                    isCyclic = true;
                }
            onStack.put(v, false);
        }

        public boolean hasCycle(){ return cycle!=null;}
        public Iterable<Integer> cycle(){ return cycle;}
    }

    /**
     * Method to return a hash map of keys and in degrees
     * @param diGraph graph object
     * @return a hashMap of Key and In Degree Pair
     */
    private HashMap<Integer, Integer> getInDegrees(BetterDiGraph diGraph) {
        HashMap<Integer, Integer> inDegrees = new HashMap<>();
        for (Integer i : diGraph.vertices()) {
            inDegrees.put(i, diGraph.getIndegree(i));
        }
        return inDegrees;
    }

    @Override
    public Iterable<Integer> order() {
        return topologicalOrder;
    }

    @Override
    public boolean isDAG() {
        return !isCyclic;
    }
}