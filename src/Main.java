import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Program for generating kanji component dependency order via topological sort.
 * 
 * @author Edgar Hakobyan, Acuna
 * @version 1.5
 */
public class Main {
    
    private static final String KANJI_SYMBOLS = "src/data-kanji.txt";
    private static final String DATA_COMPONENTS = "src/data-components.txt";
    private static BetterDiGraph diGraph;
    
    /**
     * Entry point for testing.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new Main().run();
    }
    private void run() {
        diGraph = new BetterDiGraph();
        try {
            BufferedReader reader = getBufferReader(KANJI_SYMBOLS);
            HashMap<Integer, Integer> kanjiMap = kanjiLoader(reader);
            reader.close();

            System.out.println("Original:");
            for (Integer x : diGraph.vertices()) {
                printFormatted(kanjiMap, x);
            }

            reader = getBufferReader(DATA_COMPONENTS);
            componentLoader(reader);
            reader.close();


            /*
             * READ ME
             * Create a .dot type file in the src folder.
             * name it graph (or rename from the method generateDotFile)
             * uncomment section below to write the contents of a graph to .dot format
             * for visualization purposes
             */
            /////////////// UNCOMMENT ME 👇 ////////////////////

            // generateDotFile(kanjiMap, diGraph);

            /////////////// UNCOMMENT ME ☝ ////////////////////

            TopologicalSort topologicalSort = new IntuitiveTopological(diGraph);
            System.out.println("\nSorted:");
            for (Integer i : topologicalSort.order()) {
                printFormatted(kanjiMap, i);
            }

        } catch (IOException e) {
            System.out.println("File not found");
        }



    }

    /**
     * Method for loading kanji symbols and keys into a hash map
     * @param reader reader object
     * @return hashMapSet
     * @throws IOException thrown when cannot read from file
     */
    private static HashMap<Integer, Integer> kanjiLoader(BufferedReader reader) throws IOException {
        HashMap<Integer, Integer> hashMapSet = new HashMap<>();

        String currentLine;
        reader.readLine();
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.substring(0, 1).equals("#")) {
                String[] splitString = currentLine.split("\t");

                int id = Integer.parseInt(splitString[0]);
                int kanjiId = Character.codePointAt(splitString[1], 0);
                hashMapSet.put(id, kanjiId);
                diGraph.addVertex(id);
            }
        }
        return hashMapSet;
    }

    /**
     * Method for loading components into a hash map of adjacency pairs
     * @param reader reader object
     * @throws IOException thrown when cannot read from file
     */
    private static void componentLoader(BufferedReader reader) throws IOException {
        String line;
        reader.readLine(); // skips the top line
        while ((line = reader.readLine()) != null) {
            if (line.charAt(0) != '#') {
                String[] str = line.split("\t");

                int v = Integer.parseInt(str[0]); // converts string to int
                int w = Integer.parseInt(str[1]);

                diGraph.addEdge(v, w);
            }
        }
    }

    /**
     * BufferReader object for reading inputs in UTF_8 format
     * @param filename text file name
     * @return BufferReader object
     * @throws IOException thrown when file not found
     */
    private static BufferedReader getBufferReader(String filename) throws IOException {
        return new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(filename)), StandardCharsets.UTF_8));
    }

    /**
     * Method for converting unicode Kanji symbols into actual Kanji symbols
     * @param keySet a hashmap of keys and Kanji unicode values
     * @param v hashmap key
     */
    private static void printFormatted(HashMap<Integer, Integer> keySet, int v) {
        System.out.print(String.valueOf(Character.toChars(keySet.get(v))));
    }

    /**
     * Generates a .dot type file for graph visualization
     * @param v key index
     * @param w key index
     * @throws IOException thrown when file not found
     */
    public void generateDotFile( HashMap<Integer, Integer> v, BetterDiGraph w ) throws IOException {

        FileWriter writer = new FileWriter("src/graph.dot", true);
        BufferedWriter buffer = new BufferedWriter(writer);
        buffer.write("digraph graphVisualization { ");
        buffer.newLine();

        for (Integer x: w.vertices()) {
            for(Integer y : w.getAdj(x)) {

                // converts unicode to Kanji characters
                String q1 = String.valueOf(Character.toChars(v.get(x)));
                String q2 = String.valueOf(Character.toChars(v.get(y)));
                // < > used to insert HTML style string into dot file
                buffer.write("<" + q1 + ">");
                buffer.write("->");
                buffer.write("<" + q2 + ">");
                buffer.newLine();
            }
        }
        buffer.write("}");
        buffer.close();
    }
}