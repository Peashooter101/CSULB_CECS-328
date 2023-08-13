import java.util.ArrayList;
import java.util.HashMap;

public class PA4_Dominos_NoFlipping {

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
    public static ArrayList<int[]> solve(ArrayList<int[]> dominos) {

        Graph g = assembleGraph(dominos);
        g.printGraph();

        ArrayList<int[]> answer = new ArrayList<>();

        // Start at the node with the least edges in.
        Node start = null;
        for (Node n : g.nodes) {
            if (start == null) { start = n; continue; }
            if (start.edgesIn.size() > n.edgesIn.size()) {
                start = n;
            }
            if (start.edgesIn.size() == n.edgesIn.size() && start.edgesOut.size() > n.edgesOut.size()) {
                start = n;
            }
        }
        Node current = start;
        assert current != null;
        current.visited = true;
        g.nodes.remove(current);
        answer.add(new int[] {current.topValue,current.botValue});

        // Check unvisited nodes for least edges in.
        while (g.nodes.size() > 0) {
            Node destination = null;
            for (Node n : current.edgesOut) {
                if (n.visited) { continue; }
                if (destination == null) { destination = n; continue; }
                if (destination.edgesIn.size() > n.edgesIn.size()) {
                    destination = n;
                }
                if (destination.edgesIn.size() == n.edgesIn.size() && destination.edgesOut.size() > n.edgesOut.size()) {
                    destination = n;
                }
            }
            // Error Check: No paths out remain!
            if (destination == null) { System.out.println("NO PATHS REMAIN FROM NODE " + current.nodeID); return answer; }

            // Go to destination!
            current = destination;
            current.visited = true;
            g.nodes.remove(current);
            answer.add(new int[] {current.topValue,current.botValue});
        }

        // If a destination node has the same number of edges in, choose one with the least edges out.
        // Return the answer set.

        return answer;
    }

    public static Graph assembleGraph(ArrayList<int[]> dominos) {
        Graph g = new Graph();
        for (int[] a : dominos) {
            g.addNode(a[0], a[1]);
        }
        return g;
    }

    public static void main(String[] args) {
        ArrayList<int[]> dominos = new ArrayList<>();
        dominos.add(new int[]{1, 1});
        dominos.add(new int[]{3, 1});
        dominos.add(new int[]{2, 1});
        dominos.add(new int[]{1, 3});
        dominos.add(new int[]{2, 2});
        dominos.add(new int[]{1, 2});

        printAnswer(solve(dominos));
    }

    public static void printAnswer(ArrayList<int[]> answer) {
        System.out.println();
        for (int[] i : answer) {
            System.out.print("(" + i[0] + ", " + i[1] + ")");
        }
        System.out.println("\n");
    }

}

class Node {
    static int nodeIDCounter = 0;

    ArrayList<Node> edgesIn;
    ArrayList<Node> edgesOut;

    boolean visited;

    final int nodeID;
    final int topValue;
    final int botValue;

    Node(int top, int bot) {
        topValue = top;
        botValue = bot;
        nodeID = nodeIDCounter;
        edgesIn = new ArrayList<>();
        edgesOut = new ArrayList<>();
        visited = false;

        nodeIDCounter++;
    }

    void connect(Node other) {
        if (this.nodeID == other.nodeID) { return; }
        if (this.botValue == other.topValue) {
            this.edgesOut.add(other);
            other.edgesIn.add(this);
        }
        if (this.topValue == other.botValue) {
            this.edgesIn.add(other);
            other.edgesOut.add(this);
        }
    }

    static void printNode(Node n) {
        System.out.println("Node " + n.nodeID + ": (" + n.topValue + ", " + n.botValue + ")");
        System.out.print("   In: ");
        for (Node e : n.edgesIn) {
            System.out.print(e.nodeID + ", ");
        }
        System.out.println();
        System.out.print("   Out: ");
        for (Node e : n.edgesOut) {
            System.out.print(e.nodeID + ", ");
        }
        System.out.println();
    }

}

class Graph {

    HashMap<Integer, ArrayList<Node>> nodesTop; // Nodes with that top value.
    HashMap<Integer, ArrayList<Node>> nodesBot; // Nodes with that bot value.
    ArrayList<Node> nodes;

    Graph() {
        Node.nodeIDCounter = 0;
        nodesTop = new HashMap<>();
        nodesBot = new HashMap<>();
        nodes = new ArrayList<>();
    }

    void addNode(int top, int bot) {
        Node node = new Node(top, bot);
        nodes.add(node);

        nodesTop.computeIfAbsent(top, k -> new ArrayList<>());
        nodesTop.get(top).add(node);

        nodesBot.computeIfAbsent(bot, k -> new ArrayList<>());
        nodesBot.get(bot).add(node);

        for (Node n : nodes) {
            node.connect(n);
        }
    }

    void printGraph() {
        for (Node n : nodes) {
            Node.printNode(n);
        }
    }

}
