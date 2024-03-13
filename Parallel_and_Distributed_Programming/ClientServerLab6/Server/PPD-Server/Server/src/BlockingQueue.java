
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue {
    private Queue<MyNode> coada = new LinkedList<>();

    private int capacity = 50;

    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public MyNode get() {
        lock.lock();
        try {
            while (coada.isEmpty() && !Main.done) {
                    notEmpty.await();
            }
            var elem = coada.poll();
            notFull.signal();
            return elem;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public synchronized void put(MyNode node) {
        lock.lock();
        try {
            while (coada.size() == capacity) {
                notFull.await();
            }
            coada.add(node);
            notEmpty.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public synchronized void notifica() {
        lock.lock();
        notEmpty.signalAll();
        lock.unlock();
    }
}