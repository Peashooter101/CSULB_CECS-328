import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PA4_Dominos {

    /**
     * Solve the problem obeying the following rules:
     * - A domino will have a number on the top side and bottom side.
     * -- You will not flip the domino.
     * - You are creating a stack of dominos such that...
     * -- The bottom number on the domino must match the top number of the domino above it in the stack.
     * - You are guaranteed at least one valid solution.
     * @param dominos The given set of dominos (not Set like the Data Structure).
     * @return The stack of dominos that abides by those rules.
     */
    public static ArrayList<int[]> solve(ArrayList<int[]> dominos){

        // Debug
        boolean debug = false;

        // Assemble the Graph
        Graph g = assembleGraph(dominos);
        if (debug) { g.printGraph(); }

        // Find Cycles in the Graph, until all connections are broken.
        ArrayList<ArrayList<Integer>> cycles = new ArrayList<>();
        ArrayList<Integer> odds = new ArrayList<>();
        if (debug) { System.out.println("Starting Traversal\n"); }

        // While there are still nodes in the graph...
        while (g.hasNodes()) {
            // Choose a point N and its destination D (2)
            Node n = g.pullExistingNode();
            Node d = n.connections.get(0);
            boolean oddFound = false;

            // Search for Cycle
            ArrayList<Integer> currentPath = new ArrayList<>();

            // If N/D is not in path.
            while (!currentPath.contains(d.nodeID)) {
                // See if Node N has degree 1 (A)
                if (n.getDegree() == 1) {
                    if (debug) { System.out.println("Found nodes with 1 degree: " + n.nodeID + "(" + n.connections.size() + ")"); }
                    odds.add(n.nodeID);
                    oddFound = true;
                    break;
                }
                // See if Node D has degree 1 (B)
                if (d.getDegree() == 1) {
                    if (debug) { System.out.println("Found nodes with 1 degree: " + n.nodeID + "(" + n.connections.size() + ")"); }
                    odds.add(d.nodeID);
                    oddFound = true;
                    break;
                }
                // Add N as path.
                currentPath.add(n.nodeID);
                // Move to the destination node.
                Node prev = n;
                n = d;
                // Get a new destination: To prevent backtracking to itself,
                if (n.connections.get(0) == prev) { d = n.connections.get(1); }
                else { d = n.connections.get(0); }
                // Add our current node to the path we took.
            }
            // If an odd node was found, pathfind to the other odd node.
            if (oddFound) {
                odds = findOddPath(g, g.nodesById.get(odds.get(0)));
                for (int i = 0; i < odds.size()-1; i++) {
                    g.breakConnection(g.nodesById.get(odds.get(i)), g.nodesById.get(odds.get(i+1)));
                }
                continue;
            }

            // Node d represents the node that got cycled at.
            currentPath.add(n.nodeID);
            List<Integer> currentCycle = currentPath.subList(currentPath.indexOf(d.nodeID), currentPath.size());
            if (debug) { System.out.println(currentCycle); }

            // Disconnect all nodes in the cycle from each other and add them to cycles.
            cycles.add(new ArrayList<>());
            while (currentCycle.size() > 1) {
                cycles.get(cycles.size()-1).add(currentCycle.get(0));
                g.breakConnection(g.nodesById.get(currentCycle.get(0)), g.nodesById.get(currentCycle.get(1)));
                currentCycle.remove(0);
            }
            cycles.get(cycles.size()-1).add(currentCycle.get(0));
            g.breakConnection(g.nodesById.get(currentCycle.get(0)), d);
            if (debug) {
                System.out.println("Cycles Found: ");
                for (ArrayList<Integer> a : cycles) {
                    System.out.println("   " + a);
                }
                System.out.println();
            }
        }

        System.out.println("Odd Path: " + odds);
        if (!odds.isEmpty()) { cycles.add(0, odds); }


        // Find a Eulerian Cycle: Undirected Graph
        ArrayList<Integer> path = eulerian(cycles, !odds.isEmpty());

        System.out.println("Eulerian Cycle: " + path);

        // Assemble Answer
        ArrayList<int[]> answer = new ArrayList<>();

        for (int i = 0; i < path.size()-1; i++) {
            answer.add(new int[]{path.get(i), path.get(i+1)});
        }

        return answer;
    }

    public static ArrayList<Integer> findOddPath(Graph graph, Node start) {
        // Start at the given node.
        ArrayList<Integer> returnPath = new ArrayList<>();
        returnPath.add(start.nodeID);

        Node current = start;
        current.visited = true;
        while (true) {
            // Error Case
            if (returnPath.isEmpty()) { return null; }
            current = graph.nodesById.get(returnPath.get(returnPath.size()-1));

            // Check if this node has any connections we haven't visited.
            Node destination = null;
            for (Node n : current.connections) {
                if (!n.visited) {
                    destination = n;
                    destination.visited = true;
                    break;
                }
            }

            // If all connections are visited, pull back and try again.
            if (destination == null) {
                returnPath.remove(returnPath.size()-1);
                continue;
            }

            // If this isn't the node we are looking for, add it to the list and continue
            returnPath.add(destination.nodeID);
            if (destination.connections.size() % 2 != 1) { continue; }

            // Exit if it is the node we are looking for
            break;
        }

        return returnPath;
    }

    /**
     * Driver function for the Recursive Eulerian Cycles
     * @param cycles All cycles within the graph found.
     * @param odds If any odd cycles exist.
     * @return The path, in reverse (not like it matters if it's in reverse, it's dominos).
     */
    public static ArrayList<Integer> eulerian(ArrayList<ArrayList<Integer>> cycles, boolean odds) {
        ArrayList<Integer> layer = cycles.get(0);
        cycles.remove(layer);
        int value = layer.get(0);

        ArrayList<Integer> returnPath = eulerianRecursive(cycles, layer, value);
        if (!odds) { returnPath.add(value); }
        return returnPath;
    }

    /**
     * Recursively determines the Eulerian Cycle.
     * @param otherCycles Cycles not including the layer input.
     * @param layer The list representing the layer we are entering at.\
     * @param value The value you enter this layer at.
     * @return The path, in reverse, so far (not like it matters if it's in reverse, it's dominos).
     */
    public static ArrayList<Integer> eulerianRecursive(ArrayList<ArrayList<Integer>> otherCycles, ArrayList<Integer> layer, int value) {

        ArrayList<Integer> returnPath = new ArrayList<>();
        int index = layer.indexOf(value);

        for (int i = 0; i < layer.size(); i++) {

            // See if this value exists in another path.
            for (ArrayList<Integer> a : otherCycles) {
                if (a.contains(layer.get(index))) {
                    otherCycles.remove(a);
                    returnPath.addAll(eulerianRecursive(otherCycles, a, layer.get(index)));
                    break;
                }
            }
            // Add this to the path.
            returnPath.add(layer.get(index));

            // Increment the index for the next loop.
            index = (index+1) % layer.size();
        }

        return returnPath;

    }

    public static Graph assembleGraph(ArrayList<int[]> dominos) {
        Graph g = new Graph();
        for (int[] a : dominos) {
            g.connect(a[0], a[1]);
        }
        return g;
    }

    public static void main(String[] args) {
        ArrayList<int[]> dominos = new ArrayList<>();
        // Test Case 1: PASSED
//        dominos.add(new int[]{1, 3});
//        dominos.add(new int[]{1, 3});
//        dominos.add(new int[]{2, 1});
//        dominos.add(new int[]{1, 3});
//        dominos.add(new int[]{3, 1});

        // Test Case 2: PASSED
//        dominos.add(new int[]{4, 2});
//        dominos.add(new int[]{7, 1});
//        dominos.add(new int[]{2, 3});
//        dominos.add(new int[]{6, 5});
//        dominos.add(new int[]{4, 3});
//        dominos.add(new int[]{4, 5});
//        dominos.add(new int[]{4, 1});
//        dominos.add(new int[]{6, 7});
//        dominos.add(new int[]{7, 4});

        // Test Case 3: PASSED
//        dominos.add(new int[]{3, 6});
//        dominos.add(new int[]{8, 3});
//        dominos.add(new int[]{1, 8});
//        dominos.add(new int[]{5, 2});
//        dominos.add(new int[]{4, 1});
//        dominos.add(new int[]{5, 2});
//        dominos.add(new int[]{8, 1});
//        dominos.add(new int[]{2, 4});
//        dominos.add(new int[]{3, 1});
//        dominos.add(new int[]{3, 5});
//        dominos.add(new int[]{5, 7});
//        dominos.add(new int[]{8, 1});
//        dominos.add(new int[]{5, 6});
//        dominos.add(new int[]{1, 2});

        // Test Case 4:
        dominos.add(new int[]{4, 9});
        dominos.add(new int[]{6, 7});
        dominos.add(new int[]{7, 10});
        dominos.add(new int[]{11, 5});
        dominos.add(new int[]{4, 1});
        dominos.add(new int[]{12, 6});
        dominos.add(new int[]{8, 11});
        dominos.add(new int[]{1, 8});
        dominos.add(new int[]{2, 8});
        dominos.add(new int[]{4, 12});
        dominos.add(new int[]{2, 9});
        dominos.add(new int[]{10, 5});
        dominos.add(new int[]{3, 9});
        dominos.add(new int[]{4, 8});
        dominos.add(new int[]{9, 7});
        dominos.add(new int[]{10, 9});
        dominos.add(new int[]{5, 10});
        dominos.add(new int[]{2, 3});
        dominos.add(new int[]{10, 7});
        dominos.add(new int[]{10, 9});

        printAnswer(solve(dominos));
    }

    public static void printAnswer(ArrayList<int[]> answer) {
        System.out.println();
        for (int[] i : answer) {
            System.out.print("(" + i[0] + ", " + i[1] + ")");
        }
        System.out.println("\n");
    }

    public static String stringAnswer(ArrayList<int[]> answer) {
        String returnString = "\n";
        for (int[] i : answer) {
            returnString += "(" + i[0] + ", " + i[1] + ")";
        }
        returnString += "\n";
        return returnString;
    }

}

class Node {
    ArrayList<Node> connections;
    boolean visited;

    final int nodeID;

    Node(int num) {
        nodeID = num;
        connections = new ArrayList<>();
    }

    int getDegree() { return connections.size(); }

    void connect(Node other) {
        this.connections.add(other);
        other.connections.add(this);
    }

    void disconnect(Node other) {
        this.connections.remove(other);
        other.connections.remove(this);

//        System.out.println("NumA(" + this.nodeID + ") NumB(" + other.nodeID + ")");
//        System.out.println("  Connections A: " + this.connections);
//        System.out.println("  Connections B: " + other.connections);
//        System.out.println("  Successful Removal? " + this.connections.remove(other));
//        System.out.println("  Successful Removal? " + other.connections.remove(this));
//        System.out.println("  Connections A: " + this.connections);
//        System.out.println("  Connections B: " + other.connections);
    }

    static void printNode(Node n) {
        System.out.println("Node " + n.nodeID + ":");
        System.out.print("   Connections: ");
        for (Node e : n.connections) {
            System.out.print(e.nodeID + ", ");
        }
        System.out.println();
    }

}

class Graph {

    HashMap<Integer, Node> nodesById;

    Graph() {
        nodesById = new HashMap<>();
    }

    void connect(int top, int bot) {
        if (!nodesById.containsKey(top)) { nodesById.put(top, new Node(top)); }
        if (!nodesById.containsKey(bot)) { nodesById.put(bot, new Node(bot)); }
        nodesById.get(top).connect(nodesById.get(bot));
    }

    void breakConnection(Node one, Node other) {
        one.disconnect(other);
        if (one.connections.size() == 0) { nodesById.remove(one.nodeID); }
        if (other.connections.size() == 0) { nodesById.remove(other.nodeID); }
    }

    void printGraph() {
        System.out.println("--== GRAPH ==--");
        for (Integer i : nodesById.keySet()) {
            Node.printNode(nodesById.get(i));
        }
        System.out.println("--== GRAPH ==--\n");
    }

    Node pullExistingNode() {
        for (Integer i : nodesById.keySet()) {
            return nodesById.get(i);
        }
        return null;
    }

    boolean hasNodes() {
        return nodesById.size() > 0;
    }

}