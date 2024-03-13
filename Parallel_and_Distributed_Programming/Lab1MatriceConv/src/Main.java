import java.io.*;
import java.util.Scanner;

public class Main {

    public static int p;
    public static int n;
    public static int m;
    public static int k;
    public static int seqOrPara;
    public static String fileName;
    public static int impartireThreaduri;

    public static class ThreadCalc extends Thread {

        int linieStart;
        int linieFinal;
        int colStart;
        int colFinal;

        int[][] matrixInitial;
        int[][] matrixFinal;
        int[][] matrixConv;

        public ThreadCalc(int linieStart, int linieFinal, int colStart, int colFinal,
                          int[][] matrixInitial, int[][] matrixFinal, int[][] matrixConv) {
            this.linieStart = linieStart;
            this.linieFinal = linieFinal;
            this.colStart = colStart;
            this.colFinal = colFinal;
            this.matrixInitial = matrixInitial;
            this.matrixFinal = matrixFinal;
            this.matrixConv = matrixConv;
        }

        @Override
        public void run() {
            calcMatrix(matrixInitial, matrixFinal, matrixConv, linieStart, linieFinal, colStart, colFinal);
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


    public static void calcMatrix(int[][] matrixInitial, int[][] matrixFinal, int[][] convMatrix,
                                  int linieInceput, int linieFinal, int colInceput, int colFinal) {
        for (int i = linieInceput; i < linieFinal; i++) {
            for (int j = colInceput; j < colFinal; j++) {
                matrixFinal[i][j] = calcElementConv(matrixInitial, convMatrix, i, j);
            }
        }
    }

    public static void citireMatrici(int[][] matrix, int[][] convMatrix) throws FileNotFoundException {
        FileInputStream file = new FileInputStream(fileName);
        Scanner scanner = new Scanner(file);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                String line = scanner.nextLine();
                matrix[i][j] = Integer.parseInt(line);
            }
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                convMatrix[i][j] = Integer.parseInt(scanner.nextLine());
            }
        }
    }

    public static void writeFile(String fileName, int[][] newMatrix) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                line.append(String.valueOf(newMatrix[i][j])).append(" ");
            }
            line.append("\n");
            writer.write(line.toString());
            line.setLength(0);
        }
        writer.close();
    }

    public static void pararelLinii(int[][] matrix, int[][] newMatrix, int[][] convMatrix) throws InterruptedException {
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
            threads[i] = new ThreadCalc(start, end, 0, m, matrix, newMatrix, convMatrix);
            start = end;
        }
        for (int i = 0; i < p; i++) {
            threads[i].start();
        }
        for (int i = 0; i < p; i++) {
            threads[i].join();
        }
    }

    public static void pararelCol(int[][] matrix, int[][] newMatrix, int[][] convMatrix) throws InterruptedException {
        int cat = m / p;
        int rest = m % p;
        Thread[] threads = new Thread[p];
        int start = 0;
        int end = 0;
        for (int i = 0; i < p; i++) {
            end = start + cat;
            if (rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new ThreadCalc(0, n, start, end, matrix, newMatrix, convMatrix);
            start = end;
        }
        for (int i = 0; i < p; i++) {
            threads[i].start();
        }
        for (int i = 0; i < p; i++) {
            threads[i].join();
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        p = Integer.parseInt(args[0]);
        fileName = args[1];
        seqOrPara = Integer.parseInt(args[2]);
        n = Integer.parseInt(args[3]);
        m = Integer.parseInt(args[4]);
        k = Integer.parseInt(args[5]);
        impartireThreaduri = Integer.parseInt(args[6]);
        int[][] matrix = new int[n][m];
        int[][] convMatrix = new int[k][k];
        int[][] newMatrix = new int[n][m];
        citireMatrici(matrix, convMatrix);

        if (seqOrPara == 0) {
            var startTime = System.nanoTime();
            calcMatrix(matrix, newMatrix, convMatrix, 0, n, 0, m);
            var endTime = System.nanoTime();
            writeFile("output1.txt", newMatrix);
            System.out.println((double) (endTime - startTime));
        } else {
            var startTime = System.nanoTime();
            if (impartireThreaduri == 0)
                pararelLinii(matrix, newMatrix, convMatrix);
            else if(impartireThreaduri == 1)
                pararelCol(matrix, newMatrix, convMatrix);
            var endTime = System.nanoTime();
            writeFile("output2.txt", newMatrix);
            System.out.println((double) (endTime - startTime));
        }
    }
}