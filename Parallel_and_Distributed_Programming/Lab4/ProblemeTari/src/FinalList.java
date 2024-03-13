import java.util.*;
public class FinalList {

    private List<Node> theList = new LinkedList<>();

    private Set<Integer> restrictedIds = new HashSet<>();

    public synchronized void add(Node n) {
        if (restrictedIds.contains(n.ID)) {
            return;
        }
        int index = theList.indexOf(n);
        if (index == -1) {
            if (n.punctaj == -1) {
                restrictedIds.add(n.ID);
            } else {
                theList.add(n);
                Collections.sort(theList);
            }
        } else {
            var node = theList.get(index);
            if (n.punctaj == -1) {
                theList.remove(index);
                restrictedIds.add(n.ID);
            } else {
                theList.get(index).punctaj += n.punctaj;
                Collections.sort(theList);
            }
        }
    }

    public List<Node> getTheList() {
        return theList;
    }
}