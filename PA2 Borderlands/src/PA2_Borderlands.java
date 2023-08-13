import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PA2_Borderlands {

    /**
     * Provides a suitable answer with the following conditions:
     * - Given N number of rows of 3 sets of bulbs.
     * - The magnitude of the bulb determines what number set it is a part of:
     * -- Bulbs -5 and 5 are of the same set
     * - If the bulb is positive, it turns on when activated (activated).
     * - If the bulb is negative, it turns off when activated (!activated).
     * - At least 1 bulb must be on in each row.
     * - All bulbs in a single row are within 5 (magnitude) of each other.
     * -- Bulbs -7 and 2 are within 5 magnitude of each other.
     * @param bulbs Rows of bulbs
     * @return Configuration such that at least one bulb in each row is on.
     */
    public static ArrayList<Boolean> solve(ArrayList<ArrayList<Integer>> bulbs) {

        // Record mandatory answers...
        HashMap<Integer, Boolean> mandatoryAnswers = new HashMap<>();
        HashMap<Integer, Integer> timesFlipped = new HashMap<>();

        // Determine highest bulb count...
        int maxBulbValue = 0;
        for (ArrayList<Integer> wireSets : bulbs) {
            if (wireSets.get(0).equals(wireSets.get(1)) && wireSets.get(1).equals(wireSets.get(2)) ) {
                int bulbId = Math.abs(wireSets.get(0));
                mandatoryAnswers.put(Math.abs(bulbId), Math.abs(bulbId) == bulbId);
            }
            for (Integer i : wireSets) {
                if (Math.abs(i) > maxBulbValue) { maxBulbValue = Math.abs(i); }
            }
        }

        // Assemble solution ArrayList
        ArrayList<Boolean> solution = new ArrayList<>();
        for (int i = 0; i < maxBulbValue; i++) {
            // If the mandatory answers has that switch set...
            if (mandatoryAnswers.containsKey(i+1)) {
                solution.add(mandatoryAnswers.get(i+1));
                continue;
            }
            solution.add(false);
        }

        while (!checkSolution(bulbs, solution)) {
            // Set a switch for all rows...
            for (ArrayList<Integer> wireSets : bulbs) {
                boolean oneOn;
                ArrayList<Integer> candidates = new ArrayList<>();
                for (Integer i : wireSets) {
                    if (mandatoryAnswers.containsKey(Math.abs(i))) { continue; }
                    if (timesFlipped.getOrDefault(Math.abs(i), 0) > 100) { continue; }
                    oneOn = (i > 0) == solution.get(Math.abs(i) - 1);
                    if (oneOn) { break; }
                    if (!candidates.contains(i)) { candidates.add(i); }
                }
                for (Integer i : candidates) {
                    int bulbId = Math.abs(i);
                    solution.set(bulbId-1, !solution.get(bulbId-1));
                    timesFlipped.put(bulbId, timesFlipped.getOrDefault(bulbId, 0) + 1);
                    break;
                }
            }
        }

        return solution;

    }

    /**
     * Verify the solution provided is actually valid.
     * @param bulbs Rows of Bulbs
     * @param solution Provided booleans as ArrayList
     * @return True if valid solution, false otherwise.
     */
    public static boolean checkSolution(ArrayList<ArrayList<Integer>> bulbs, ArrayList<Boolean> solution) {

        for (ArrayList<Integer> rows : bulbs) {
            boolean oneOn = false;
            for (Integer i : rows) {
                boolean active = solution.get(Math.abs(i)-1);
                oneOn = (i > 0) == active;
                if (oneOn) { break; }
            }
            if (!oneOn) { return false; }
        }

        return true;

    }

    public static ArrayList<ArrayList<Integer>> generateBulbsFromFile(String fileName) {
        ArrayList<ArrayList<Integer>> returnBulbs = new ArrayList<>();
        File file;
        Scanner scnr;
        try {
            file = new File(fileName);
            scnr = new Scanner(file);
        } catch (FileNotFoundException e) { System.out.println("File not found..."); return null; }

        while (scnr.hasNextLine()) {
            ArrayList<Integer> wireSet = new ArrayList<>();
            String[] line = scnr.nextLine().split(" ");
            for (String s : line) {
                wireSet.add(Integer.valueOf(s));
            }
            returnBulbs.add(wireSet);
        }
        return returnBulbs;
    }

    public static void main(String[] args) {

        ArrayList<ArrayList<Integer>> inputBulbs = generateBulbsFromFile("inputbulbs1.txt");
        ArrayList<ArrayList<Integer>> inputBulbs5 = generateBulbsFromFile("inputbulbs5.txt");
        assert inputBulbs != null && inputBulbs5 != null;

        System.out.println(inputBulbs);
        ArrayList<Boolean> solution = solve(inputBulbs);
        System.out.println("Generated Solution (inputBulbs): " + solve(inputBulbs));
        System.out.println("Check Generated Solution (inputBulbs): " + checkSolution(inputBulbs, solution));

        System.out.println("\n"+inputBulbs5);
        ArrayList<Boolean> solution5 = solve(inputBulbs5);
        System.out.println("Generated Solution (inputBulbs5): " + solution5);
        System.out.println("Check Generated Solution (inputBulbs5): " + checkSolution(inputBulbs5, solution5));

    }

}
