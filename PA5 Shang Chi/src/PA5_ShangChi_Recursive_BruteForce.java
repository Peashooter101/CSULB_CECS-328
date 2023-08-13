import java.util.ArrayList;
import java.util.Arrays;

public class PA5_ShangChi_Recursive_BruteForce {

    /**
     * Returns a list of integers representing the indexes of combinations made.
     * @param initialOrder The order of the ingredients.
     * @param magic The ingredient outcomes.
     * @return ArrayList of Integers that represents combinations made at each step.
     */
    public static ArrayList<Integer> solve(ArrayList<Integer> initialOrder, ArrayList<ArrayList<Integer>> magic) {
        System.out.println(initialOrder);
        return recursiveBruteForce(initialOrder, magic);
    }

    public static ArrayList<Integer> recursiveBruteForce(ArrayList<Integer> order, ArrayList<ArrayList<Integer>> magic) {
        // Exit Case
        if (order.size() == 1) {
            if (order.get(0) == 0) {
                return new ArrayList<>();
            }
            else { return null; }
        }

        ArrayList<Integer> returnAnswer = null;
        int index = 0;
        for (int i = 0; i < order.size()-1; i++) {
            ArrayList<Integer> newOrder = new ArrayList<>(order);
            int left = newOrder.get(i);
            int right = newOrder.get(i+1);
            newOrder.remove(i);
            newOrder.set(i, magic.get(left).get(right));
            returnAnswer = recursiveBruteForce(newOrder, magic);
            if (returnAnswer != null) {
                returnAnswer.add(i);
                break;
            }
        }
        return returnAnswer;
    }

    public static void printGrid(ArrayList<ArrayList<Integer>> grid) {
        for (ArrayList<Integer> row : grid) {
            System.out.println(row);
        }
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> magic = new ArrayList<>();
        magic.add(new ArrayList<>(Arrays.asList(1, 1, 0)));
        magic.add(new ArrayList<>(Arrays.asList(2, 1, 0)));
        magic.add(new ArrayList<>(Arrays.asList(0, 2, 2)));

        ArrayList<Integer> initialOrder = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 0, 2));

        printGrid(magic);
        System.out.println(solve(initialOrder, magic));
    }

}
