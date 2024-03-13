import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FinalList {

    public class SyncronizedSet {
        private Set<Integer> restrictedIds = new HashSet<>();

        public synchronized boolean contains(Integer id) {
            return restrictedIds.contains(id);
        }

        public synchronized boolean add(Integer id) {
            return restrictedIds.add(id);
        }
    }

    private Node santinel = new Node(-1, Integer.MAX_VALUE, -1);
    private SyncronizedSet restrictedIds = new SyncronizedSet();

    private Node get(Node n) {
        var previous = santinel;
        var current = santinel.getNext();
        previous.mutex.lock();
        while (current != null) {
            current.mutex.lock();
            if (Objects.equals(current.ID, n.ID)) {
                previous.mutex.unlock();
                current.mutex.unlock();
                return current;
            }
            var aux = previous;
            previous = current;
            current = previous.getNext();
            aux.mutex.unlock();
        }
        previous.mutex.unlock();
        return null;
    }

    private void add(Node n) {
        var previous = santinel;
        var current = santinel.getNext();
        previous.mutex.lock();
        while (current != null) {
            current.mutex.lock();
            if (previous.compareTo(n) < 0 && current.compareTo(n) > 0) {
                n.setNext(current);
                previous.setNext(n);
                previous.mutex.unlock();
                current.mutex.unlock();
                return;
            }
            var aux = previous;
            previous = current;
            current = previous.getNext();
            aux.mutex.unlock();
        }
        previous.setNext(n);
        previous.mutex.unlock();
    }

    private void remove(Node n) {
        var previous = santinel;
        var current = santinel.getNext();
        previous.mutex.lock();
        while (current != null) {
            current.mutex.lock();
            if (Objects.equals(current.ID, n.ID)) {
                previous.setNext(current.getNext());
                previous.mutex.unlock();
                current.mutex.unlock();
                return;
            }
            var aux = previous;
            previous = current;
            current = previous.getNext();
            aux.mutex.unlock();
        }
        previous.mutex.unlock();
    }

    public synchronized void adaugaNod(Node n) {
        if (restrictedIds.contains(n.ID)) {
            return;
        }
        Node actualNode = this.get(n);
        if (actualNode == null) {
            if (n.punctaj == -1) {
                restrictedIds.add(n.ID);
            } else {
                this.add(n);
            }
        } else {
            if (n.punctaj == -1) {
                this.remove(n);
                restrictedIds.add(n.ID);
            } else {
                this.remove(n);
                n.punctaj += actualNode.punctaj;
                this.add(n);
            }
        }
    }

    public List<Node> getTheList() {
        List<Node> theList = new ArrayList<>();
        var previous = santinel;
        var current = santinel.getNext();
        previous.mutex.lock();
        while (current != null) {
            current.mutex.lock();
            theList.add(current);
            var aux = previous;
            previous = current;
            current = previous.getNext();
            aux.mutex.unlock();
        }
        previous.mutex.unlock();
        return theList;
    }
}