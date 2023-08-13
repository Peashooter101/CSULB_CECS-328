import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PA3_Hopscotch_Naive {

    /**
     * Finds the most optimal play of hopscotch such that...
     * - They move rightward on the gri, if they reach the right edge, they teleport to the left edge on move right.
     * - They must make 1 full rotation.
     * -- Total Steps Taken = width - 1.
     * -- Path Array Size = width + 1 (Top / Bottom, Column Index, Steps...).
     * - Gets the most points, points are summed by the path taken.
     * @param grid Rows of bulbs
     * @return A valid path for the maximum score.
     */
    public static ArrayList<Integer> solve(ArrayList<ArrayList<Integer>> grid) {

        ArrayList<Integer> returnValue = new ArrayList<>();

        int height = grid.size();
        int width = grid.get(0).size();

        int maxTop = Integer.MIN_VALUE;
        int maxBot = Integer.MIN_VALUE;

        int x;
        int y;

        for (Integer i : grid.get(0)) { maxTop = (maxTop < i) ? i : maxTop; }
        for (Integer i : grid.get(grid.size()-1)) { maxBot = (maxBot < i) ? i : maxBot; }

        if (maxTop > maxBot) {
            y = 0;
            x = grid.get(y).indexOf(maxTop);
            returnValue.add(1); // Denotes Top
        } else {
            y = grid.size()-1;
            x = grid.get(y).indexOf(maxBot);
            returnValue.add(0); // Denotes Bottom
        }
        returnValue.add(x); // Denotes Column

        for (int i = 1; i < width; i++) {
            // Determine most valuable step...
            int yUp = (y-1 < 0) ? height-1 : y;
            int yDown = ((y+1) % (height));
            int xNext = ((x+1) % (width));

            int valUp = grid.get(yUp).get(xNext);
            int valNext = grid.get(y).get(xNext);
            int valDown = grid.get(yDown).get(xNext);

            if (valUp >= valNext && valUp >= valDown) {
                returnValue.add(1);
                y = yUp;
            }
            else if (valNext >= valUp && valNext >= valDown) {
                returnValue.add(0);
            }
            else {
                returnValue.add(-1);
                y = yDown;
            }
            x = xNext;
        }

        return returnValue;

    }

    /**
     * Return the score of the solution...
     * @param grid 2D ArrayList representing a Matrix.
     * @param path Provided path as ArrayList of Integers
     * @return True if valid solution, false otherwise.
     */
    public static int getScore(ArrayList<ArrayList<Integer>> grid, ArrayList<Integer> path) {

        // Initialize the limits and current (x,y) location
        int height = grid.size();
        int width = grid.get(0).size();
        int x, y;

        // Determine top row or bottom row.
        if (path.get(0) == 1) { y = 0; }
        else if (path.get(0) == 0) { y = height-1; }
        else { System.out.println("Answer Key Incorrect, path[0] should be 1 or 0"); return Integer.MIN_VALUE; }

        // Determine column.
        x = path.get(1);

        // Sum up the path...
        int sum = grid.get(y).get(x);
        System.out.println("\nStart Path\n" + grid.get(y).get(x) + " (" + x + ", " + y + ")");

        // Traverse the path...
        for (int i = 2; i < path.size(); i++) {
            int step = path.get(i);
            if (step < -1 || step > 1) {
                System.out.println("Answer Key Incorrect, steps can only be -1, 0, or 1.");
                return Integer.MIN_VALUE;
            }
            System.out.println("Step: " + step);
            x = ((x+1) % (width));
            y = ((y-step) % (height));
            y = (y < 0) ? height-1 : y;

            sum += grid.get(y).get(x);
            System.out.println(grid.get(y).get(x) + " (" + x + ", " + y + ")");
        }

        return sum;

    }

    public static ArrayList<ArrayList<Integer>> generateGridFromFile(String fileName) {
        ArrayList<ArrayList<Integer>> returnGrid = new ArrayList<>();
        File file;
        Scanner scnr;
        try {
            file = new File(fileName);
            scnr = new Scanner(file);
        } catch (FileNotFoundException e) { System.out.println("File not found..."); return null; }

        while (scnr.hasNextLine()) {
            ArrayList<Integer> row = new ArrayList<>();
            String[] line = scnr.nextLine().split(" ");
            for (String s : line) {
                row.add(Integer.valueOf(s));
            }
            returnGrid.add(row);
        }
        return returnGrid;
    }

    public static void printGrid(ArrayList<ArrayList<Integer>> grid) {
        for (ArrayList<Integer> row : grid) {
            for (Integer i : row) {
                System.out.printf("%-4s", i);
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {

        ArrayList<ArrayList<Integer>> inputGrid = generateGridFromFile("inputgrid1.txt");
        ArrayList<Integer> solution1 = new ArrayList<>(Arrays.asList(1, 4, 0, 1, 1, 0, -1));
        assert inputGrid != null;
        printGrid(inputGrid);
        System.out.println("Points of Given Path: " + getScore(inputGrid, solution1));
        System.out.println("Points of Calculated Path: " + getScore(inputGrid, solve(inputGrid)));

    }

}
