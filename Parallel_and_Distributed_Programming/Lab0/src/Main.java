import java.util.Date;
import java.util.Random;

public class Main {

    public static class ThreadSum extends Thread {

        private int a[], b[], c[], start, finish;

        public ThreadSum(int a[], int b[], int c[], int start, int finish) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.start = start;
            this.finish = finish;
        }

        @Override
        public void run() {
            for (int i = start; i < finish; i++) {
                c[i] = (int) ( Math.pow(a[i], 3) + Math.pow(b[i], 3));
            }
        }
    }

    public static void sum(int a[], int b[], int c[], int n) {
        for (int i = 0; i < n; i++) {
            c[i] = (int) ( Math.pow(a[i], 3) + Math.pow(b[i], 3));
        }
    }

    public static void sumP(int a[], int b[], int c[], int n, int p) throws InterruptedException {
        int start = 0;
        int cat = n / p;
        int rest = n % p;
        ThreadSum threads[] = new ThreadSum[p];
        for (int i = 0; i < p; i++) {

            int finish = start + cat;
            if (rest > 0) {
                finish++;
                rest--;
            }
            threads[i] = new ThreadSum(a, b, c, start, finish);
            threads[i].start();

            start = finish;
        }
        for (int i = 0; i < p; i++) {
            threads[i].join();
        }

    }

    public static class ThreadSumCicl extends Thread {

        private int a[], b[], c[], p, start, n;

        public ThreadSumCicl(int a[], int b[], int c[], int start, int p, int n) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.start = start;
            this.p = p;
            this.n = n;
        }

        @Override
        public void run() {
            for (int i = this.start; i < n; i = i + p) {
                c[i] = (int) ( Math.pow(a[i], 3) + Math.pow(b[i], 3));
            }
        }
    }

    public static void sumPCicl(int a[], int b[], int c[], int n, int p) throws InterruptedException {
        ThreadSumCicl threads[] = new ThreadSumCicl[p];
        for (int i = 0; i < p; i++) {
            threads[i] = new ThreadSumCicl(a, b, c, i, p, n);
            threads[i].start();

        }
        for (int i = 0; i < p; i++) {
            threads[i].join();
        }

    }

    public static void main(String[] args) throws InterruptedException {

        int n = 5000000;
        int p = 8;
        int v1[] = new int[n];
        int v2[] = new int[n];
        int v3[] = new int[n];

        for (int i = 0; i < n; i++) {
            v1[i] = i + 1;
            v2[i] = -i;
        }

        var startTime = System.nanoTime();
        sumP(v1, v2, v3, n, p);
        var endTime = System.nanoTime();
        System.out.println("ParalelLin: " + (double)(endTime - startTime));

        startTime = System.nanoTime();
        sum(v1, v2, v3, n);
        endTime = System.nanoTime();
        System.out.println("Secvential: " + (double)(endTime - startTime));
        startTime = System.nanoTime();
        sumPCicl(v1, v2, v3, n, p);
        endTime = System.nanoTime();
        System.out.println("ParalelCicl:" + (double)(endTime - startTime));

    }
}