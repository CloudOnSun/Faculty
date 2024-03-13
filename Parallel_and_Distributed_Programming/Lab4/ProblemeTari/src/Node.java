import java.util.Objects;

public class Node implements Comparable<Node> {
    public Integer ID;
    public Integer punctaj;

    public Node(Integer ID, Integer punctaj) {
        this.ID = ID;
        this.punctaj = punctaj;
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