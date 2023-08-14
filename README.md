# CSULB CECS 328: Algorithms

These are the programming assignments given to us. The only resource we were allowed to use is official documentation of the language we were using and the provided PDF prompt.

We were allowed to choose between C++, Java, and Python. 10 automatically generated test cases were used to grade each assignment. Test Cases 1 and 2 were the simplest and smallest whereas Test Cases 9 and 10 tested the efficiency of the program with huge inputs. Total score of 50/50.

## PA1: Squid Game

You are given n players and x lines with the following rules:
- Players must choose a line in numerical order (Player 1 starts, then Player 2, until Player n).
- Players can either go to a line where...
  - The line is empty.
  - The sum of this player and the player in front of them must be a perfect square (Player 3 can stand behind Player 1, 3 + 1 = 4).

Return the number of valid configurations of lines, excluding duplicates.

This problem was intended to be solved with Recursion.
This problem was solved with Recursion.

 ## PA2: Borderlands

You are given x rows of 3 bulbs. The label of each of the bulbs can range from 1 to n. The bulb can also be negative. You are given the following rules:
- A bulb is controlled by a switch with the same label (Negatives are not considered, so Switch 1 controls Bulb -1 and 1).
- A bulb with a positive value will be ON when the switch is ON (Matching States).
- A bulb with a negative value will be OFF when the switch is ON (Inverse States).
- A bulb must be on in every row.

Return a boolean list that defines a valid state of the switches 1 through n.

This problem was intended to be solved with Divide and Conquer.
This problem was solved naively by toggling a random switch from a row where no bulbs were ON.

## PA3: Hopscotch

You are given an n by m grid of integers and the following rules:
- You may enter at any column at either the top or bottom row.
- You may move like so:
  - Horizontally, each move must consist of a step RIGHT.
    - You can actually choose to move left or right but it does not matter.
  - Vertically, you may step up, down, or not at all.
  - This means each step must be UP-RIGHT, RIGHT, or DOWN-RIGHT.
- The grid has these properties...
  - When you reach the right-most column, your RIGHT step will put you on the leftmost column.
  - When you reach the top row, your UP-RIGHT step will put you on the bottom row.
  - When you reach the bottom row, your DOWN-RIGHT step will put you on the top row.
- You must exit at either the top or bottom row after one full revolution around the grid.
- Each value you land on in the path is summed together.

Return an integer list that defines a path around the grid that produces the highest score.

This problem was intended to be solved with the All Points Shortest Path algorithm.
This problem was solved using Dynamic Programming.

## PA4: Dominos

You are given a set of dominos and the following rules:
- The dominos can only meet when they have the same values on the same side (`[5,2][2,6]` is valid, `[2,5][6,2]` is not).
- The last unconnected value must be the same as the first unconnected value (the dominos make a cycle).
- Each domino must be used once and only once.

Return a valid list of int[] representing the final domino configuration.

This problem was intended to be solved with the Eulerian Cycle algorithm.
This problem was solved using the Eulerian Cycle algorithm.

## PA5: Shang Chi

You are given a 3x3 grid, a line of ingredients `0`, `1`, and `2`, and the following rules:
- The row represents the left ingredient.
- The column represents the right ingredient.
- The combination of the left and right ingredients results in the ingredient associated to the grid.
  - If `grid[1][2] = 0`, then ingredients `1 2` reduces to `0`.
- You cannot change the order of the ingredients.
- When combining an ingredient, note the index of the left ingredient.
- You want to combine the ingredients until you are left with only `0`.

Return a list of integers representing the indeces of combinations at each step.

This problem was intended to be solved with Memoization.
This problem was solved by naively reducing the number of ingredients to 8, before performing Memoization.
