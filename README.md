# CSULB CECS 328: Algorithms
 
## PA1: Squid Game

You are given n players and x lines with the following rules:
- Players must choose a line in numerical order (Player 1 starts, then Player 2, until Player n).
- Players can either go to a line where...
  - The line is empty.
  - The sum of this player and the player in front of them must be a perfect square (Player 3 can stand behind Player 1, 3 + 1 = 4).

 Return the number of valid configurations of lines, excluding duplicates.

 ## PA2: Borderlands

You are given x rows of 3 bulbs. The label of each of the bulbs can range from 1 to n. The bulb can also be negative. You are given the following rules:
- A bulb is controlled by a switch with the same label (Negatives are not considered, so Switch 1 controls Bulb -1 and 1).
- A bulb with a positive value will be ON when the switch is ON (Matching States).
- A bulb with a negative value will be OFF when the switch is ON (Inverse States).
- A bulb must be on in every row.

Return a boolean list that defines a valid state of the switches 1 through n.

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

## PA4: Dominos

You are given a set of dominos and the following rules:
- The dominos can only meet when they have the same values on the same side (`[5,2][2,6]` is valid, `[2,5][6,2]` is not).
- The last unconnected value must be the same as the first unconnected value (the dominos make a cycle).
- Each domino must be used once and only once.

Return a valid list of int[] representing the final domino configuration.

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
