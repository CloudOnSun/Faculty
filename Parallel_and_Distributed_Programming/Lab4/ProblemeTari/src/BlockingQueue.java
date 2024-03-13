
import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue {
    private Queue<Node> coada = new LinkedList<>();

    public synchronized Node get() {
        while (coada.isEmpty() && Main.producersDone.get() < Main.producers) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Eroare producator");
            }
        }
        return coada.poll();
    }

    public synchronized void put(Node node) {
        coada.add(node);
        this.notifyAll();
    }

    public synchronized void notifica() {
        this.notifyAll();
    }
}