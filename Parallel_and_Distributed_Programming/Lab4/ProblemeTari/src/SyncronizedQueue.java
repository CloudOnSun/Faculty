import java.util.ArrayDeque;
import java.util.Queue;

public class SyncronizedQueue {

    public synchronized String pop() {
        return Main.files.poll();
    }

    public synchronized boolean isEmpty() {
        return Main.files.isEmpty();
    }
}