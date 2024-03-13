import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node implements Comparable<Node> {
    public Integer ID;
    public Integer punctaj;

    public Integer country;

    private Node next = null;

    Lock mutex = new ReentrantLock();

    public Node(Integer ID, Integer punctaj, Integer country) {
        this.ID = ID;
        this.punctaj = punctaj;
        this.country = country;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Integer punctaj) {
        this.punctaj = punctaj;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(ID, node.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public int compareTo(Node o) {
        if (!Objects.equals(this.punctaj, o.punctaj)) {
            return o.punctaj - this.punctaj;
        }
        else {
            return this.ID - o.ID;
        }
    }
}