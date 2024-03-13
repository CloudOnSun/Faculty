
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static Queue<String> files = new ArrayDeque<>();
    public static AtomicInteger filesDone = new AtomicInteger(0);
    public static Integer producers;
    public static Integer consumers;

    public static void writeFile(String fileName, FinalList rezultate) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        StringBuilder line = new StringBuilder();

        for (var node : rezultate.getTheList()) {
            line.append(node.ID).append(",").append(node.punctaj).append("\n");
            writer.write(line.toString());
            line.setLength(0);
        }
        writer.close();
    }

    public static void secvential(FinalList rezultateFinale) {
        for (var f : files) {
            try {
                File fileObj = new File(f);
                Scanner scanner = new Scanner(fileObj);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    line = line.replace("\n", "");
                    var elems = line.split(",");
                    var tara = f.charAt(9) - '0';
                    Node n = new Node(Integer.parseInt(elems[0]), Integer.parseInt(elems[1]), tara);
                    rezultateFinale.adaugaNod(n);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        var startTime = System.nanoTime();

        producers = Integer.parseInt(args[0]);
        consumers = Integer.parseInt(args[1]);
        int secvOrParalel = Integer.parseInt(args[2]);

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 10; j++) {
                String file = "Rezultate" + i + "_" + j + ".txt";
                files.add(file);
            }
        }

        BlockingQueue coadaCitire = new BlockingQueue();
        FinalList listaRezultate = new FinalList();
        IdLocksMap idLocksMap = new IdLocksMap();

        if (secvOrParalel == 0) {
            secvential(listaRezultate);
            writeFile("output1.txt", listaRezultate);
        } else {
            ExecutorService pool = Executors.newFixedThreadPool(producers);
            for (var f : files) {
                pool.execute(new Producer(coadaCitire, f, idLocksMap));
            }
            pool.shutdown();

            Thread[] consumatoriTh = new Thread[consumers];
            for (int i = 0; i < consumers; i++) {
                consumatoriTh[i] = new Consumer(coadaCitire, listaRezultate, idLocksMap);
                consumatoriTh[i].start();
            }

            pool.awaitTermination(5, TimeUnit.SECONDS);

            for (int i = 0; i < consumers; i++) {
                consumatoriTh[i].join();
            }

            writeFile("output2.txt", listaRezultate);
        }

        var endTime = System.nanoTime();
        System.out.println((double) (endTime - startTime));
    }
}