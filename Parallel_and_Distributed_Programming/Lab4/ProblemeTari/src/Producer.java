import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Producer extends Thread {
    private BlockingQueue coada;
    private SyncronizedQueue fisiere;

    private String fisierCurent = "";

    private boolean done = false;

    public Producer(BlockingQueue coada, SyncronizedQueue fisiere) {
        this.coada = coada;
        this.fisiere = fisiere;
    }

    public void getFisier() {
        var f = fisiere.pop();
        if (f == null) {
            done = true;
            Main.producersDone.incrementAndGet();
            coada.notifica();
        } else {
            fisierCurent = f;
        }
    }

    @Override
    public void run() {
        getFisier();
        while (!done) {
            try {
                File fileObj = new File(fisierCurent);
                Scanner scanner = new Scanner(fileObj);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    line = line.replace("\n", "");
                    var elems = line.split(",");
                    Node n = new Node(Integer.parseInt(elems[0]), Integer.parseInt(elems[1]));
                    coada.put(n);
                }
                getFisier();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}