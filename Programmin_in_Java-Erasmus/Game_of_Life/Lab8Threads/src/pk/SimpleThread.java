package pk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;

//divide the square in rectangles, with the same length but smaller height, each rectangle is processed by one thread
public class SimpleThread extends Thread {

    private boolean[][] square;
    private int row1; // where the rectangle starts in the initial square
    private int row2; // where the rectangle stops in the initial square
    private int length; // the length of the square = the length of the rectangle
    private final CountDownLatch latch;

    public SimpleThread(boolean[][] square, int row1, int row2, int length, CountDownLatch latch){
        this.square = square;
        this.row1 = row1;
        this.row2 = row2;
        this.length = length;
        this.latch = latch;
    }

    //calculate the i,j cell by its neighbours
    private boolean calcCell(int i, int j) {
        int nrAliveAround = 0;
        if(square[i-1][j-1])
            nrAliveAround++;
        if(square[i-1][j])
            nrAliveAround++;
        if(square[i-1][j+1])
            nrAliveAround++;
        if(square[i][j-1])
            nrAliveAround++;
        if(square[i][j+1])
            nrAliveAround++;
        if(square[i+1][j-1])
            nrAliveAround++;
        if(square[i+1][j])
            nrAliveAround++;
        if(square[i+1][j+1])
            nrAliveAround++;

        if (square[i][j] && (nrAliveAround == 2 || nrAliveAround == 3))
            return true;
        if (!square[i][j] && (nrAliveAround == 3))
            return  true;
        return false;
    }

    //for each cell in the rectangle, play the game and keep the result in a copy rectangle
    private void playGame(boolean[][] rectCopy) {
        for(int i = row1; i <= row2; i++) {
            for (int j = 1; j <= length; j++) {
                rectCopy[i - row1][j] = calcCell(i, j);
            }
        }
    }

    //play the game for the given rectangle
    //when all other threads played the game, modify the initial square
    @Override
    public void run() {
        boolean[][] rectCopy = new boolean[row2-row1+1][length+1];
        playGame(rectCopy);
        latch.countDown();

        try {
            latch.await(); // waits for all other threads
            for(int i = row1; i <= row2; i++) {
                for (int j = 1; j <= length; j++) {
                    square[i][j] = rectCopy[i-row1][j];
                }
            }
        }
        catch (InterruptedException ex) {
            System.out.println("Latch interrupted!");
        }

    }

    //create borders in the initial square so that it fits the assignment:
    //  the upper border is the last line
    //  the left border is the most right line
    //  the right border is the most left line
    //  the lower border is the first line
    private static void createBorders(boolean[][] square, int length) {

        //left and right border
        for(int i = 1; i < length + 1; i++) {
            square[i][0] = square[i][length];
            square[i][length+1] = square[i][1];
        }

        //upper and lower border
        for (int j = 1; j < length + 1; j++) {
            square[0][j] = square[length][j];
            square[length+1][j] = square[1][j];
        }

        //modify the corners as well
        square[0][0] = square[length][length];
        square[0][length+1] = square[length][1];
        square[length+1][0] = square[1][length];
        square[length+1][length+1] = square[1][1];
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        int proc = Runtime.getRuntime().availableProcessors(); // how many threads we can create
        ArrayList<SimpleThread> threads = new ArrayList<>();

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        var line = console.readLine();
        var numbers = line.split(" ");
        int length = Integer.parseInt(numbers[0]);
        int steps = Integer.parseInt(numbers[1]);

        int height; // the height of each rectangle
        if (length%proc == 0) {
            height = length/proc; // we can share the work equally for each thread
        }
        else // we can not the share the work equally, the last thread will have less work to do
             // ex: length = 22, number of possible threads = 5 ->
             //         the firs 4 threads will work a rectangle of height 5
             //         the last will work a rectangle of height 4
             // ex: length = 9, number of possible threads = 8 ->
             //         4 threads will work a rectangle of height 2
             //         1 thread will work a rectangle of height 1
            height = length/proc + 1;

        boolean[][] square = new boolean[length+2][length+2]; // length+2, so that we can border

        int i = 1;
        for(i = 1; i <= length; i++) {
            line = console.readLine();
            for(int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '_') {
                    square[i][j + 1] = false;
                } else
                    square[i][j + 1] = true;
            }
        }

        createBorders(square, length);

        for (int k = 0; k < steps; k++) {
            threads.clear();

            CountDownLatch latch = new CountDownLatch(proc);

            int th = 0; // see exactly how many threads we actually created

            for (i = 1; i + height -1 <= length; i = i + height) {
                SimpleThread t = new SimpleThread(square, i, i + height - 1, length, latch);
                threads.add(t);
                th++;
            }
            //if we can not share the work equally
            if (i <= length) {
                SimpleThread t = new SimpleThread(square, i, length, length, latch);
                threads.add(t);
                th++;
            }

            for( var t : threads) {
                t.start();
            }

            //if we created fewer threads then the possible number
            for (i = th; i < proc; i++)
                latch.countDown();

            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    System.out.println("Interrupted exception occurred");
                }
            }
            createBorders(square, length);
        }

        for (i = 1; i <= length; i++) {
            for (int j = 1; j <= length; j++) {
                if(square[i][j])
                    System.out.print('X');
                else
                    System.out.print('_');
            }
            System.out.print("\n");
        }

    }
}