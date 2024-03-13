import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyNode implements Comparable<MyNode>, Serializable {
    public int id;
    public int punctaj;

    public int tara;

    private MyNode next = null;

    Lock mutex = new ReentrantLock();

    public MyNode(int id, int punctaj, int tara) {
        this.id = id;
        this.punctaj = punctaj;
        this.tara = tara;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPunctaj() {
        return punctaj;
    }

    public void setPunctaj(Integer punctaj) {
        this.punctaj = punctaj;
    }

    public Integer getTara() {
        return tara;
    }

    public void setTara(Integer tara) {
        this.tara = tara;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyNode node = (MyNode) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int compareTo(MyNode o) {
        if (!Objects.equals(this.punctaj, o.punctaj)) {
            return o.punctaj - this.punctaj;
        }
        else {
            return this.id - o.id;
        }
    }

    @Override
    public String toString() {
        return "MyNode{" +
                "id=" + id +
                ", punctaj=" + punctaj +
                ", tara=" + tara +
                '}';
    }
}