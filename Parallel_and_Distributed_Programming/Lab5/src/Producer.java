import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Producer implements Runnable {
    private BlockingQueue coada;
    private IdLocksMap idLocksMap;

    private String fisierCurent = "";

    private boolean done = false;

    public Producer(BlockingQueue coada, String fisier, IdLocksMap idLocksMap) {
        this.coada = coada;
        this.fisierCurent = fisier;
        this.idLocksMap = idLocksMap;
    }


    @Override
    public void run() {
        try {
            File fileObj = new File(fisierCurent);
            Scanner scanner = new Scanner(fileObj);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.replace("\n", "");
                var elems = line.split(",");
                var tara = fisierCurent.charAt(9) - '0';
                Node n = new Node(Integer.parseInt(elems[0]), Integer.parseInt(elems[1]), tara);
                idLocksMap.putLock(n.ID);
                coada.put(n);
            }
            Main.filesDone.incrementAndGet();
            coada.notifica();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
