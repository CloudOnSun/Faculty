# Game of Life

The task is to implement Game Of Life, a program that computes the array configuration of the specified size after a specified number of steps.

The text is read from the standard input and is written to the standard output. The first line of input contains two positive integers. The first number, N, represents both the width and height of the square array of cells. The second number, separated by a space, is the number of steps the program should perform. On the following lines, at each line, there are N characters (plus \n), each one representing either a dead cell - '_', or a live cell 'X'. Each line (except for the first line) contains exactly N characters plus the line end character '\n', the input contains N + 1 rows.

At each step, the cell state (dead or alive) is computed for each cell based on the status of the eight neighbor cells (left, right, top, bottom, and diagonally in all four directions).

The rules are as follows:

If the cell is live and the number of living neighbor cells is less than 2, the cell is dead in the next step.

If the cell is live and the number of living neighbor cells is 2 or 3, the cell is alive in the next step.

If the cell is live and the number of living neighbor cells is more than 3, the cell is dead in the next step.

If the cell is dead and the number of living neighbor cells is 3, the cell is alive in the next step.

Otherwise, the cell remains dead.

When evaluating the number of alive cells, the situation of the previous step is always taken as the input, it is not updated continuously during the evaluation of the step.

Create a parallel implementation, i.e., evaluating one step is performed in parallel via multiple threads (use a suitable number of threads). If you create a non-parallel implementation, you will receive 2 points max. It is not necessary to use threads explicitly - it is better to use a suitable executor.

The upper edge of the cell field links to the lower edge and left to the right, i.e., the cell at the position (0, 0) is adjacent to the cells (N-1,N-1), (N-1,0), (N-1,1), (0, N-1), (0,1), (1,N-1), (1,0), (1,1).

The output contains N + 1 rows (the last line is empty), it contains the '_' and 'X' characters representing the cell states after N steps, where there is one row of cells per line.

The whole input fits into memory.

Input example:
8 2

\_\_\_\_\_\_\_\_

\_\_X\_\_X\_\_

\_X\_\_\_\_X\_

\_X_\_\_\_X\_

\_X\_\_\_\_X\_

\_X\_\_\_\_X\_

\_\_X\_\_X\_\_

\_\_\_\_\_\_\_\_

Output:

\_\_\_\_\_\_\_\_

\_\_\_\_\_\_\_\_

\_\_X\_\_X\_\_

\_\_\_XX\_\_\_

\_\_\_XX\_\_\_

\_\_X\_\_X\_\_

\_\_\_\_\_\_\_\_

\_\_\_\_\_\_\_\_
