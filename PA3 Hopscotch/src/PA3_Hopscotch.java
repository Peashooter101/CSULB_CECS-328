import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PA3_Hopscotch {

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

        printGrid(grid);
        System.out.println();
        ArrayList<Integer> returnValue = new ArrayList<>();

        int width = grid.get(0).size();
        int height = grid.size();

        int invalidInt = 7327; // Type it into a Phone NumPad :)
        ArrayList<ArrayList<Integer>> grid0 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> prevStep = new ArrayList<>();

        // Initialize r0 and prevStep
        for (int i = 0; i < height; i++) {
            prevStep.add(new ArrayList<>());

            if (i == 0) {
                grid0.add(new ArrayList<>(grid.get(0)));
                for (int j = 0; j < width; j++) {
                    prevStep.get(i).add(invalidInt);
                }
            }
            else if (i < height-1) {
                grid0.add(new ArrayList<>());
                for (int j = 0; j < width; j++) {
                    grid0.get(i).add(0);
                    prevStep.get(i).add(invalidInt);
                }
            }
            else {
                grid0.add(new ArrayList<>(grid.get(i)));
                for (int j = 0; j < width; j++) {
                    prevStep.get(i).add(invalidInt);
                }
            }

        }

        ArrayList<Integer> maxPath = findMaximums(grid, grid0, prevStep, width-1);

        int y = maxPath.get(maxPath.size()-1);
        maxPath.remove(maxPath.size()-1);
        int x = maxPath.get(maxPath.size()-1);
        maxPath.remove(maxPath.size()-1);

        if (y == 0) { returnValue.add(1); }
        else { returnValue.add(0); }
        returnValue.add(x);

        // System.out.println("MAX PATH DEBUG: " + maxPath.get(0));
        for (int i = maxPath.size(); i > 1; i--) {
            returnValue.add(maxPath.get(i-1));
        }

        // returnValue.remove(returnValue.size()-1);

        System.out.println(returnValue);

        return returnValue;

    }

    /**
     * Describes the current maximum possible value and its corresponding path.
     * @param originalGrid Matrix of the original grid.
     * @param previousValues Matrix containing Max Score so far.
     * @param previousStep Matrix containing previous step to get to Max Score so far.
     * @return The path in reverse, then (X, Y) of the previous step's location
     */
    public static ArrayList<Integer> findMaximums(ArrayList<ArrayList<Integer>> originalGrid, ArrayList<ArrayList<Integer>> previousValues, ArrayList<ArrayList<Integer>> previousStep, int stepsRemaining) {

        printGrid(previousValues);
        System.out.println();

        ArrayList<ArrayList<Integer>> gridK = new ArrayList<>();
        ArrayList<ArrayList<Integer>> thisStep = new ArrayList<>();

        int width = originalGrid.get(0).size();
        int height = originalGrid.size();

        // When there are no steps remaining, find the largest value and start returning positions...
        if (stepsRemaining <= 0) {
            ArrayList<Integer> returnArray = new ArrayList<>();
            // Highest Value (S) and their Coordinates (X, Y)
            int topX = -1, topY = -1, topS = Integer.MIN_VALUE;
            // Check top line...
            for (int x = 0; x < previousValues.get(0).size(); x++) {
                if (topS < previousValues.get(0).get(x)) {
                    topS = previousValues.get(0).get(x);
                    topX = x;
                    topY = 0;
                }
            }
            // Check bottom line...
            for (int x = 0; x < previousValues.get(0).size(); x++) {
                if (topS < previousValues.get(previousValues.size()-1).get(x)) {
                    topS = previousValues.get(previousValues.size()-1).get(x);
                    topX = x;
                    topY = previousValues.size()-1;
                }
            }
            returnArray.add(previousStep.get(topY).get(topX));
            returnArray.add(topX);
            returnArray.add(topY);
            return returnArray;
        }

        // If Steps Remaining isn't 0, calculate the next step's possible points...
        // For each row...
        for (int y = 0; y < height; y++) {
            gridK.add(new ArrayList<>());
            thisStep.add(new ArrayList<>());
            // For each column in y row.
            for (int x = 0; x < width; x++) {

                // Coordinate Calculations for Previous Values...
                int yUp = (y-1 < 0) ? height-1 : y-1;
                int yDown = ((y+1) % (height));
                int xPrev = (x-1 < 0) ? width-1 : x-1;

                int fromTop = previousValues.get(yUp).get(xPrev);       // Last Step: -1
                int fromLef = previousValues.get(y).get(xPrev);         // Last Step: 0
                int fromBot = previousValues.get(yDown).get(xPrev);     // Last Step: 1

                // Original Grid Value
                int gridVal = originalGrid.get(y).get(x);

                // If the highest previous value was from the above.
                if (fromTop >= fromLef && fromTop >= fromBot) {
                    gridK.get(y).add((fromTop + gridVal));
                    thisStep.get(y).add(-1);
                }
                // If the highest previous value was from the same line.
                else if (fromLef >= fromTop && fromLef >= fromBot) {
                    gridK.get(y).add((fromLef + gridVal));
                    thisStep.get(y).add(0);
                }
                // If the highest previous value was from the below.
                else {
                    gridK.get(y).add((fromBot + gridVal));
                    thisStep.get(y).add(1);
                }
            }
        }

        ArrayList<Integer> pathSoFar = findMaximums(originalGrid, gridK, thisStep, stepsRemaining-1);

        // Pull out the X, Y Coordinates
        int thisY = pathSoFar.get(pathSoFar.size()-1);
        pathSoFar.remove(pathSoFar.size()-1);
        int thisX = pathSoFar.get(pathSoFar.size()-1);
        pathSoFar.remove(pathSoFar.size()-1);

        // Where to go next?
        int yUp = (thisY-1 < 0) ? height-1 : thisY-1;
        int yDown = ((thisY+1) % (height));
        int xPrev = (thisX-1 < 0) ? width-1 : thisX-1;
        switch (thisStep.get(thisY).get(thisX)) {
            case -1:
                // If the value came from above...
                pathSoFar.add(thisStep.get(thisY).get(thisX));
                pathSoFar.add(xPrev);
                pathSoFar.add(yUp);
                break;
            case 0:
                // If the value came from left...
                pathSoFar.add(thisStep.get(thisY).get(thisX));
                pathSoFar.add(xPrev);
                pathSoFar.add(thisY);
                break;
            case 1:
                // If the value came from below...
                pathSoFar.add(thisStep.get(thisY).get(thisX));
                pathSoFar.add(xPrev);
                pathSoFar.add(yDown);
                break;
            default:
                // Should not fire...
                System.out.println("There is no remaining path");
        }

        return pathSoFar;
    }

    /**
     * Return the score of the solution...
     * @param grid 2D ArrayList representing a Matrix.
     * @param path Provided path as ArrayList of Integers
     * @return The score of the path.
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
                System.out.printf("%-5s", i);
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

        ArrayList<Integer> solution = solve(inputGrid);
        System.out.println("Points of solve() Path: " + getScore(inputGrid, solution));

        ArrayList<ArrayList<Integer>> inputGrid2 = generateGridFromFile("inputgrid2.txt");
        assert inputGrid2 != null;
        System.out.println("Input Grid 2 Score: " + getScore(inputGrid2, solve(inputGrid2)));

    }

}
