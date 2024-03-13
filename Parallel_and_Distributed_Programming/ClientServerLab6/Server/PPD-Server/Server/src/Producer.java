import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Producer implements Runnable {
    private BlockingQueue coada;
    private IdLocksMap idLocksMap;

    private List<MyNode> currentLoad;

    private boolean done = false;

    public Producer(BlockingQueue coada, List<MyNode> load, IdLocksMap idLocksMap) {
        this.coada = coada;
        this.currentLoad = load;
        this.idLocksMap = idLocksMap;
    }


    @Override
    public void run() {
        for (var n : this.currentLoad) {
            idLocksMap.putLock(n.id);
            coada.put(n);
        }
        coada.notifica();
    }
}
