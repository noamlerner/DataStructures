import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class BST<T extends Comparable<? super T>> implements BSTInterface<T> {
    private BSTNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty BST
     */
    public BST() {
        size = 0;
    }

    /**
     * Initializes the BST with the data in the collection. The data in the BST
     * should be added in the same order it is in the collection.
     *
     * @param data
     *            the data to add to the tree
     * @throws IllegalArgumentException
     *             if data or any element in data is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        size = 0;
        for (T t : data) {
            if (t == null) {
                throw new IllegalArgumentException("Data cannot be null");
            }
            add(t);
        }
    }

    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (root == null) {
            root = new BSTNode<T>(data);
            size++;
        } else {
            add(data, root);
        }
    }

    /**
     * recursively finds the correct location for a node and adds it to the tree
     *
     * @param data
     *            data to add
     * @param cur
     *            current node for recursive purposes
     */
    private void add(T data, BSTNode<T> cur) {
        int comparison = data.compareTo(cur.getData());
        if (comparison > 0) {
            if (cur.getRight() == null) {
                cur.setRight(new BSTNode<T>(data));
                size++;
            } else {
                add(data, cur.getRight());
            }
        } else if (comparison < 0) {
            if (cur.getLeft() == null) {
                cur.setLeft(new BSTNode<T>(data));
                size++;
            } else {
                add(data, cur.getLeft());
            }
        }
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (root == null) {
            throw new NoSuchElementException("Element does not exist");
        }
        if (root.getData().equals(data)) {
            BSTNode<T> node = new BSTNode<T>(null);
            node.setRight(root);
            T dT = remove(node, root);
            root = node.getRight();
            size--;
            return dT;
        } else {
            T dt = remove(data, root, root);
            if (data == null) {
                throw new NoSuchElementException("Element does not exist");
            }
            return dt;
        }

    }

    /**
     * finds the node that needs to be returned so it can be removed by another
     * remove method
     *
     * @param data
     *            data contained in node to remove
     * @param parent
     *            parent of current node, used for removing purposes
     * @param cur
     *            current node being checked
     * @return data inside removed node
     */
    private T remove(T data, BSTNode<T> parent, BSTNode<T> cur) {
        int comparison = data.compareTo(cur.getData());
        if (comparison > 0) {
            if (cur.getRight() == null) {
                throw new NoSuchElementException("Data does not exist in tree");
            } else {
                return remove(data, cur, cur.getRight());
            }
        } else if (comparison < 0) {
            if (cur.getLeft() == null) {
                throw new NoSuchElementException("Data does not exist in tree");
            } else {
                return remove(data, cur, cur.getLeft());
            }
        } else {
            size--;
            return remove(parent, cur);
        }
    }

    /**
     * deals with the different ways to remove data depending on how many
     * children node a node has
     *
     * @param parent
     *            parent node of node to be removed
     * @param node
     *            node to be removed
     * @return data from removed node
     */
    private T remove(BSTNode<T> parent, BSTNode<T> node) {
        T data = node.getData();
        if (node.getLeft() == null && node.getRight() == null) {
            if (parent.getRight().getData().equals(node.getData())) {
                parent.setRight(null);
            } else {
                parent.setLeft(null);
            }
        } else if (node.getLeft() == null && node.getRight() != null) {
            if (parent.getRight().getData().equals(node.getData())) {
                parent.setRight(node.getRight());
            } else {
                parent.setLeft(node.getRight());
            }
        } else if (node.getLeft() != null && node.getRight() == null) {
            if (parent.getRight().getData().equals(node.getData())) {
                parent.setRight(node.getLeft());
            } else {
                parent.setLeft(node.getLeft());
            }
        } else {
            BSTNode<T> n;
            if (parent.getRight().getData().equals(node.getData())) {
                n = getSuccessor(node.getRight(), node);
                parent.setRight(n);
            } else {
                n = getSuccessor(node.getRight(), node);
                parent.setLeft(n);
            }
            n.setLeft(node.getLeft());
            n.setRight(node.getRight());
        }
        return data;
    }

    /**
     * finds the successor of the node thats passed in and removes successor
     * from tree
     *
     * @param node
     *            node to get successor for
     * @param parent
     *            parent of node
     * @return successor nodes
     */
    private BSTNode<T> getSuccessor(BSTNode<T> node, BSTNode<T> parent) {
        if (node.getLeft() != null) {
            return getSuccessor(node.getLeft(), node);
        } else {
            BSTNode<T> n = node;
            parent.setLeft(node.getRight());
            return n;
        }
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (root == null) {
            throw new NoSuchElementException("Element does not exist");
        }
        T d = find(data, root);
        if (d != null) {
            return d;
        } else {
            throw new NoSuchElementException("Element does not exist");
        }
    }

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (root == null) {
            return false;
        }
        return find(data, root) != null;

    }

    /**
     * finds a node with the passed in data recursively
     *
     * @param data
     *            data of node to find
     * @param cur
     *            current node in recursive search
     * @return data if found, null otherwise
     */
    public T find(T data, BSTNode<T> cur) {
        int comparison = data.compareTo(cur.getData());
        if (comparison > 0) {
            if (cur.getRight() == null) {
                return null;
            } else {
                return find(data, cur.getRight());
            }
        } else if (comparison < 0) {
            if (cur.getLeft() == null) {
                return null;
            } else {
                return find(data, cur.getLeft());
            }
        } else {
            return cur.getData();
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<T> preorder() {
        ArrayList<T> list = new ArrayList<T>();
        if (root != null) {
            list.add(root.getData());
            preorder(list, root);
        }
        return list;
    }

    /**
     * returns a preorder arraylist and does so recurseively
     *
     * @param list
     *            list of elements
     * @param cur
     *            current node for recursive purposes
     */
    public void preorder(List<T> list, BSTNode<T> cur) {
        if (cur.getLeft() != null) {
            list.add(cur.getLeft().getData());
            preorder(list, cur.getLeft());
        }
        if (cur.getRight() != null) {
            list.add(cur.getRight().getData());
            preorder(list, cur.getRight());
        }
    }

    @Override
    public List<T> postorder() {
        ArrayList<T> list = new ArrayList<T>();
        if (root != null) {
            postorder(list, root);
        }
        return list;

    }

    /**
     * returns a postorder arraylist and does so recurseively
     *
     * @param list
     *            list of elements
     * @param cur
     *            current node for recursive purposes
     */
    public void postorder(List<T> list, BSTNode<T> cur) {
        if (cur.getLeft() != null) {
            postorder(list, cur.getLeft());
        }
        if (cur.getRight() != null) {
            postorder(list, cur.getRight());
        }
        list.add(cur.getData());

    }

    @Override
    public List<T> inorder() {
        ArrayList<T> list = new ArrayList<T>();
        if (root != null) {
            inorder(list, root);
        }
        return list;
    }

    /**
     * returns a inorder arraylist and does so recurseively
     *
     * @param list
     *            list of elements
     * @param cur
     *            current node for recursive purposes
     */
    private void inorder(List<T> list, BSTNode<T> cur) {
        if (cur.getLeft() != null) {
            inorder(list, cur.getLeft());
        }
        list.add(cur.getData());
        if (cur.getRight() != null) {
            inorder(list, cur.getRight());
        }
    }

    @Override
    public List<T> levelorder() {
        ArrayList<T> list = new ArrayList<T>();
        Queue<BSTNode<T>> q = new LinkedList<BSTNode<T>>();
        if (root != null) {
            q.add(root);
            levelorder(list, q);
        }
        return list;
    }

    /**
     * returns a levelorder arraylist and does so recurseively
     *
     * @param list
     *            list of elements
     * @param q
     *            queue of nodes to add to list
     */
    private void levelorder(List<T> list, Queue<BSTNode<T>> q) {
        BSTNode<T> cur = q.poll();
        if (cur != null) {
            list.add(cur.getData());
            if (cur.getLeft() != null) {
                q.add(cur.getLeft());
            }
            if (cur.getRight() != null) {
                q.add(cur.getRight());
            }
            levelorder(list, q);
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int height() {
        if (root == null) {
            return -1;
        } else {
            return getHeight(root, 0);

        }
    }

    /**
     * recursively finds the height of a tree
     *
     * @param cur
     *            the current node that is being counted
     * @param h
     *            height thus far
     * @return height of tree
     */
    private int getHeight(BSTNode<T> cur, int h) {
        int hR = h;
        int hL = h;
        if (cur.getRight() != null) {
            hR = getHeight(cur.getRight(), h + 1);
        }
        if (cur.getLeft() != null) {
            hL = getHeight(cur.getLeft(), h + 1);
        }
        return Math.max(hR, hL);
    }

    @Override
    public int depth(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return findDepth(data, root, 1);
    }

    /**
     * finds the depth of a node recursively
     *
     * @param data
     *            data of node that is being searched for
     * @param cur
     *            current node for recursive purposes
     * @param curDepth
     *            depth thus far
     * @return -1 or depth of node
     */
    private int findDepth(T data, BSTNode<T> cur, int curDepth) {
        int comparison = data.compareTo(cur.getData());
        if (comparison > 0) {
            if (cur.getRight() == null) {
                return -1;
            } else {
                return findDepth(data, cur.getRight(), curDepth + 1);
            }
        } else if (comparison < 0) {
            if (cur.getLeft() == null) {
                return -1;
            } else {
                return findDepth(data, cur.getLeft(), curDepth + 1);
            }
        } else {
            return curDepth;
        }
    }

    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES. DO NOT USE IT IN YOUR CODE DO
     * NOT CHANGE THIS METHOD
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        return root;
    }
}
