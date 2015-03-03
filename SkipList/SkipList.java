import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class SkipList<T extends Comparable<? super T>> implements
        SkipListInterface<T> {
    // Do not add any additional instance variables
    private CoinFlipper coinFlipper;
    private int size;
    private Node<T> head;

    /**
     * Constructs a SkipList object that stores data in ascending order. When an
     * item is inserted, the flipper is called until it returns a tails. If, for
     * an item, the flipper returns n heads, the corresponding node has n + 1
     * levels.
     *
     * @param coinFlipper
     *            the source of randomness
     */
    public SkipList(CoinFlipper coinFlipper) {
        this.coinFlipper = coinFlipper;
        size = 0;
        head = new Node<T>(null, 1);
    }

    @Override
    public T first() {
        if (size == 0) {
            throw new NoSuchElementException("Skiplist is empty");
        }
        Node<T> curNode = head;
        for (int i = head.getLevel(); i > 1; i--) {
            curNode = curNode.getDown();
        }
        return curNode.getNext().getData();

    }

    @Override
    public T last() {
        if (size == 0) {
            throw new NoSuchElementException("Skip list is empty");
        }
        Node<T> curNode = head;
        while (curNode.getLevel() != 1 || curNode.getNext() != null) {
            if (curNode.getNext() == null) {
                curNode = curNode.getDown();
            } else {
                curNode = curNode.getNext();
            }
        }
        return curNode.getData();
    }

    @Override
    public void put(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        int level = 1;
        while (coinFlipper.flipCoin() == CoinFlipper.Coin.HEADS) {
            level++;
        }
        int curLevel = head.getLevel();
        Node<T> rowHead = head;
        while (curLevel > level) {
            rowHead = rowHead.getDown();
            curLevel--;
        }
        while (curLevel < level) {
            curLevel++;
            Node<T> upperHead = new Node<T>(null, curLevel);
            rowHead.setUp(upperHead);
            upperHead.setDown(rowHead);
            head = upperHead;
            rowHead = upperHead;
        }
        Node<T> curNode = rowHead;
        Node<T> upperNode = null;
        while (level >= 1) {
            while (curNode.getNext() != null
                    && data.compareTo(curNode.getNext().getData()) > 0) {
                curNode = curNode.getNext();
            }
            if (data.equals(curNode.getData())) {
                curNode.setData(data);
            } else {
                Node<T> addNode = new Node<T>(data, level);
                if (upperNode != null) {
                    addNode.setUp(upperNode);
                    upperNode.setDown(addNode);
                }
                upperNode = addNode;
                pointToEachOther(addNode, curNode.getNext());
                pointToEachOther(curNode, addNode);
                curNode = curNode.getDown();
            }
            level--;
        }
        size++;
    }

    /**
     * make two nodes point to eachother
     *
     * @param prev
     *            prev node
     * @param next
     *            next node
     */
    private void pointToEachOther(Node<T> prev, Node<T> next) {
        prev.setNext(next);
        if (next != null) {
            next.setPrev(prev);
        }
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        Node<T> curNode = head;
        T returnData = null;
        for (int i = head.getLevel(); i >= 1; i--) {
            while (curNode.getNext() != null
                    && data.compareTo(curNode.getNext().getData()) > 0) {
                curNode = curNode.getNext();
            }
            if (data.equals(curNode.getNext().getData())) {
                returnData = curNode.getNext().getData();
                pointToEachOther(curNode, curNode.getNext().getNext());
                if (curNode.getData() == null && curNode.getNext() == null) {
                    if (curNode == head && head.getLevel() != 1) {
                        head = head.getDown();
                        head.setUp(null);
                    }
                }
            }
            curNode = curNode.getDown();
        }
        if (returnData == null) {
            throw new NoSuchElementException(
                    "Element does not exist in skiplist");
        }
        size--;
        return returnData;
    }

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        Node<T> curNode = head;
        for (int i = head.getLevel(); i >= 1; i--) {
            while (curNode.getNext() != null
                    && data.compareTo(curNode.getNext().getData()) > 0) {
                curNode = curNode.getNext();
            }
            if (curNode.getData().equals(data)) {
                return true;
            }
            curNode = curNode.getDown();
        }
        return false;
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        Node<T> curNode = head;
        for (int i = head.getLevel(); i >= 1; i--) {
            while (curNode.getNext() != null
                    && data.compareTo(curNode.getNext().getData()) > 0) {
                curNode = curNode.getNext();
            }
            if (curNode.getData().equals(data)) {
                return curNode.getData();
            }
            curNode = curNode.getDown();
        }

        throw new NoSuchElementException("element does not exist");
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        head = new Node<T>(null, 1);
    }

    @Override
    public Set<T> dataSet() {
        Node<T> curNode = head;
        HashSet<T> set = new HashSet<T>();
        for (int i = head.getLevel(); i > 1; i--) {
            curNode = curNode.getDown();
        }
        curNode = curNode.getNext();
        while (curNode.getNext() != null) {
            set.add(curNode.getData());
            curNode = curNode.getNext();
        }
        return set;
    }

    @Override
    public Node<T> getHead() {
        return head;
    }

}
