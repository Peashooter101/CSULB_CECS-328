import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PA5_ShangChi_Memoization {


    public static ArrayList<Integer> solve(ArrayList<Integer> initialOrder, ArrayList<ArrayList<Integer>> magic) {
        ArrayList<Integer> returnList = new ArrayList<>();
        while (initialOrder.size() > 8) {
            returnList.add(0);
            int left = initialOrder.get(0);
            int right = initialOrder.get(1);
            initialOrder.remove(0);
            initialOrder.set(0,magic.get(left).get(right));
        }
        returnList.addAll(solveA(initialOrder, magic));
        return returnList;
    }

    /**
     * Returns a list of integers representing the indexes of combinations made.
     * @param initialOrder The order of the ingredients.
     * @param magic The ingredient outcomes.
     * @return ArrayList of Integers that represents combinations made at each step.
     */
    public static ArrayList<Integer> solveA(ArrayList<Integer> initialOrder, ArrayList<ArrayList<Integer>> magic) {
        HashMap<String, ArrayList<ArrayList<Integer>>> masterList = new HashMap<>();
        StringBuilder buildGoal = new StringBuilder();
        for (Integer i : initialOrder) {
            buildGoal.append(i);
        }
        String goal = buildGoal.toString();
        // String goal = initialOrder.toString().replaceAll("\\D", "");

        // For every length of the string...
        for (int size = 1; size <= goal.length(); size++) {
            // Get all possible recipes for this length of string.
            int reqCombos = (int) Math.pow(3, size);
            for (int num = 0; num < reqCombos; num++) {
                String stringFormat = "%" + size + "s";
                String current = String.format(stringFormat,Integer.toString(num, 3)).replace(" ", "0");
                //System.out.println(current);

                // Create a new masterList Entry, fitting in a recipe for 0, 1 and 2.
                masterList.put(current, new ArrayList<>());
                masterList.get(current).add(null);
                masterList.get(current).add(null);
                masterList.get(current).add(null);

                // Size = 1: 0, 1, 2
                if (current.length() == 1) {
                    masterList.get(current).set(num, new ArrayList<>());
                    continue;
                }

                // Size = 2: 00 - 22
                if (current.length() == 2) {
                    int left = Integer.parseInt(current.substring(0,1));
                    int right = Integer.parseInt(current.substring(1,2));
                    int result = magic.get(left).get(right);
                    masterList.get(current).set(result, new ArrayList<>());
                    masterList.get(current).get(result).add(0);
                    continue;
                }

                // Size >= 3: 000 and on...
                for (int split = 1; split < current.length(); split = split*2) {
                    ArrayList<ArrayList<Integer>> recipes = masterList.get(current);

                    // Split the string into 2 substrings...
                    String leftStr = current.substring(0, split);
                    String rightStr = current.substring(split);

                    // For every recipe that exists for the left...
                    for (int resultLeft = 0; resultLeft <= 2; resultLeft++) {
                        // If the recipe does not exist, ignore it.
                        if (masterList.get(leftStr).get(resultLeft) == null) { continue; }

                        // If you have a recipe for each result, no need to keep checking.
                        if (recipes.get(0) != null && recipes.get(1) != null && recipes.get(2) != null) { break; }

                        // For every recipe that exists for the right...
                        for (int resultRight = 0; resultRight <= 2; resultRight++) {
                            // If the recipe does not exist, ignore it.
                            if (masterList.get(rightStr).get(resultRight) == null) { continue; }

                            // If there is already a recipe for this, ignore this, it's a duplicate.
                            if (recipes.get(magic.get(resultLeft).get(resultRight)) != null) { continue; }

                            // Build Recipe
                            ArrayList<Integer> finalRecipe = new ArrayList<>();
                            int offset = masterList.get(leftStr).get(resultLeft).size() + 1;
                            // Offset the right recipe to emulate combining all the Right Side first.
                            for (Integer i : masterList.get(rightStr).get(resultRight)) {
                                finalRecipe.add(i + offset);
                            }
                            // Add the left recipe to emulate combining the Left Side next.
                            finalRecipe.addAll(masterList.get(leftStr).get(resultLeft));
                            // Final Combination is always 0
                            finalRecipe.add(0);
                            recipes.set(magic.get(resultLeft).get(resultRight), finalRecipe);
                        }
                    }
                }
            }
        }
        return masterList.get(goal).get(0);
    }

    public static void printGrid(ArrayList<ArrayList<Integer>> grid) {
        for (ArrayList<Integer> row : grid) {
            System.out.println(row);
        }
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> magic = new ArrayList<>();
//        magic.add(new ArrayList<>(Arrays.asList(1, 1, 0)));
//        magic.add(new ArrayList<>(Arrays.asList(2, 1, 0)));
//        magic.add(new ArrayList<>(Arrays.asList(0, 2, 2)));
//
//        ArrayList<Integer> initialOrder = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 0, 2));

        magic.add(new ArrayList<>(Arrays.asList(1, 2, 2)));
        magic.add(new ArrayList<>(Arrays.asList(0, 0, 0)));
        magic.add(new ArrayList<>(Arrays.asList(1, 2, 2)));
        ArrayList<Integer> initialOrder = new ArrayList<>(Arrays.asList(2,2,1,2,2,2,2,1,0,1,2,2,0,1,0,1,0,1,1,2,1,0,0,0,1,0,0,2,1,2,2,2,2,1,0,1,2,2,0,1,0,1,0,1,1,2,1,0,0,0,1,0,0,2,1,2,2,2,2,1,0,1,2,2,0,1,0,1,0,1,1,2,1,0,0,0,1,0,0,2,1,2,2,2,2,1,0,1,2,2,0,1,0,1,0,1,1,2,1,0,0,0,1,0,0,2,1,2,2,2,2,1,0,1,2,2,0,1,0,1,0,1,1,2,1,0,0,0,1,0,0,2,1,2,2,2,2,1,0,1,2,2,0,1,0,1,0,1,1,2,1,0,0,0,1,0,0,2,1,2,2,2,2,1,0,1,2,2,0,1,0,1,0,1,1,2,1,0,0,0,1,0,0));

        printGrid(magic);
        System.out.println(solve(initialOrder, magic));
    }
}
