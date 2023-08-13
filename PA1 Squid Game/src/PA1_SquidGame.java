import java.util.ArrayList;

public class PA1_SquidGame {

    /**
     * Provides the number of configurations that solve the following problem:
     * - Players play in sequential order, starting from 1.
     * - They must fill up all lines by the end of the game.
     * - Players can only stand in front of another player if the sum of the player + the player behind them is a perfect square.
     * - First player in a line does not need to fulfill the above requirement.
     * @param x Integer representing the number of lines.
     * @param n Integer representing the number of players.
     * @return Number of configurations saving all n players for x lines.
     */
    public static int solve(int x, int n) {
        ArrayList<ArrayList<Integer>> lines = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            lines.add(new ArrayList<>());
        }
        return recursePath(lines, 1, n);
    }

    public static int recursePath(ArrayList<ArrayList<Integer>> lines, int nextPlayer, int playerCount) {

        // Verify player count...
        if (nextPlayer > playerCount) {
            // For each line...
            for (ArrayList<Integer> line : lines) {
                // If there is an empty line, this has failed.
                if (line.isEmpty()) { return 0; }
            }
            // If there is no empty lines, this has passed and is a solution.
            System.out.println("SOLUTION FOUND: " + lines);
            return 1;
        }


        // Recursion Variables
        boolean filledEmpty = false;
        int returnValue = 0;

        // For each line...
        for (ArrayList<Integer> line : lines) {
            // If an empty line has not been filled yet (avoids duplicates) and the line is empty.
            if (line.isEmpty() && !filledEmpty) {
                line.add(nextPlayer);
                returnValue += recursePath(lines, nextPlayer + 1, playerCount);
                line.remove(line.size()-1);
                filledEmpty = true;
            }
            // If the line is not empty and meets the sum perfect square parameter.
            if (!line.isEmpty() && (Math.floor(Math.sqrt(line.get(line.size()-1) + nextPlayer)) == Math.sqrt(line.get(line.size()-1) + nextPlayer))) {
                line.add(nextPlayer);
                returnValue += recursePath(lines, nextPlayer + 1, playerCount);
                line.remove(line.size()-1);
            }
        }
        return returnValue;
    }

    public static void main(String[] args) {
        System.out.println(solve(4,7));
    }

}
