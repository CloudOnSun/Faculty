import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static int p;
    public static int n;
    public static int m;
    public static int k = 3;
    public static int seqOrPara;
    public static String fileName;

    public static class ThreadCalc extends Thread {

        int linieStart;
        int linieFinal;
        int[][] matrix;
        int[][] matrixConv;

        CyclicBarrier barrier;
        int[] firstAux = new int[m];
        int[] secondAux = new int[m];
        int[] firstRow = new int[m];
        int[] lastRow = new int[m];

        public ThreadCalc(int linieStart, int linieFinal, int[][] matrix, int[][] matrixConv, CyclicBarrier barrier) {
            this.linieStart = linieStart;
            this.linieFinal = linieFinal;
            this.matrix = matrix;
            this.matrixConv = matrixConv;
            this.barrier = barrier;
        }

        private void calcFirstRow() {
            for(int j = 0; j < m; j++) {
                firstRow[j] = calcElementConv(matrix, matrixConv, linieStart, j);
            }
        }

        private void calcLastRow() {
            for(int j = 0; j < m; j++) {
                lastRow[j] = calcElementConv(matrix, matrixConv, linieFinal - 1, j);
            }
        }

        private void calcInternRows() {
            for(int i = linieStart + 1; i < linieFinal - 1; i++) {
                if (i > linieStart + 2) {
                    for(int j = 0; j < m; j++) {
                        matrix[i-2][j] = firstAux[j];
                    }
                }
                for (int j = 0; j < m; j++) {
                    firstAux[j] = secondAux[j];
                }
                for (int j = 0; j < m; j++) {
                    secondAux[j] = calcElementConv(matrix, matrixConv, i, j);
                }
            }
            for(int j = 0; j < m; j++) {
                matrix[linieFinal-3][j] = firstAux[j];
                matrix[linieFinal-2][j] = secondAux[j];
            }
        }

        private void writeFirstLastRows() {
            for(int j = 0; j < m; j++) {
                matrix[linieStart][j] = firstRow[j];
                matrix[linieFinal-1][j] = lastRow[j];
            }
        }

        @Override
        public void run() {
            calcFirstRow();
            calcLastRow();
            calcInternRows();
            try {
                barrier.await();
                writeFirstLastRows();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void calcParalel(int[][] matrix, int[][] convMatrix) throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(p);
        int cat = n / p;
        int rest = n % p;
        Thread[] threads = new Thread[p];
        int start = 0;
        int end = 0;
        for (int i = 0; i < p; i++) {
            end = start + cat;
            if (rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new ThreadCalc(start, end, matrix, convMatrix, barrier);
            start = end;
        }
        for (int i = 0; i < p; i++) {
            threads[i].start();
        }
        for (int i = 0; i < p; i++) {
            threads[i].join();
        }
    }

    public static void calcSeq(int[][] matrix, int[][] convMatrix) {
        int[] firstAux = new int[m];
        int[] secondAux = new int[m];
        for(int i = 0; i < n; i++) {
            if (i > 1) {
                for(int j = 0; j < m; j++) {
                    matrix[i-2][j] = firstAux[j];
                }
            }
            for (int j = 0; j < m; j++) {
                firstAux[j] = secondAux[j];
            }
            for (int j = 0; j < m; j++) {
                secondAux[j] = calcElementConv(matrix, convMatrix, i, j);
            }
        }
        for(int j = 0; j < m; j++) {
            matrix[n-2][j] = firstAux[j];
            matrix[n-1][j] = secondAux[j];
        }
    }

    public static int calcElementConv(int[][] matrix, int[][] convMatrix, int linie, int col) {
        int linInceput = linie - k / 2;
        int linFinal = linie + k / 2;
        int colInceput = col - k / 2;
        int colFinal = col + k / 2;
        int suma = 0;
        for (int i = linInceput; i <= linFinal; i++) {
            for (int j = colInceput; j <= colFinal; j++) {
                if (i >= 0 && j >= 0 && i < n && j < m) {
                    suma += matrix[i][j] * convMatrix[i - linInceput][j - colInceput];
                }
            }
        }
        return suma;
    }

    public static void citireMatrici(int[][] matrix, int[][] convMatrix) throws FileNotFoundException {
        FileInputStream file = new FileInputStream(fileName);
        Scanner scanner = new Scanner(file);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                String line = scanner.nextLine();
                matrix[i][j] = Integer.parseInt(line);
            }
        }
        for (int i = 1; i < n+1; i++) {
            matrix[i][0] = matrix[i][1];
            matrix[i][m+1] = matrix[i][m];
        }
        for (int j = 1; j < m+1; j++) {
            matrix[0][j] = matrix[1][j];
            matrix[n+1][j] = matrix[n][j];
        }
        matrix[0][0]=matrix[1][1]; matrix[0][m+1]=matrix[1][m]; matrix[n+1][0]=matrix[n][1]; matrix[n+1][m+1]=matrix[n][m];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                convMatrix[i][j] = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    public static void writeFile(String fileName, int[][] newMatrix) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        StringBuilder line = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                line.append(String.valueOf(newMatrix[i][j])).append(" ");
            }
            line.append("\n");
            writer.write(line.toString());
            line.setLength(0);
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        p = Integer.parseInt(args[0]);
        fileName = args[1];
        seqOrPara = Integer.parseInt(args[2]);
        n = Integer.parseInt(args[3]);
        m = Integer.parseInt(args[4]);
        int[][] matrix = new int[n+2][m+2];
        int[][] convMatrix = new int[k][k];
        citireMatrici(matrix, convMatrix);

        if (seqOrPara == 0) {
            var startTime = System.nanoTime();
            calcSeq(matrix, convMatrix);
            var endTime = System.nanoTime();
            writeFile("output1.txt", matrix);
            System.out.println((double) (endTime - startTime));
        } else {
            var startTime = System.nanoTime();
            calcParalel(matrix, convMatrix);
            var endTime = System.nanoTime();
            writeFile("output2.txt", matrix);
            System.out.println((double) (endTime - startTime));
        }
    }
}