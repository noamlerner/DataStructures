import java.util.NoSuchElementException;
/**
 * ArrayQueue Implementation of a queue using an array as the backing structure
 *
 * @author Noam Lerner
 * @version 1.0
 */
public class ArrayQueue<T> implements QueueADT<T> {

    // Do not add instance variables
    private T[] backing;
    private int size;
    private int front;
    private int back;

    /**
     * Construct an ArrayQueue with an initial capacity of INITIAL_CAPACITY
     *
     * Use Constructor Chaining
     */
    public ArrayQueue() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Construct an ArrayQueue with the specified initial capacity of
     * initialCapacity
     *
     * @param initialCapacity
     *            Initial capacity of the backing array.
     */
    public ArrayQueue(int initialCapacity) {
        backing = (T[]) new Object[initialCapacity];
        size = 0;
        front = 0;
        back = 0;
    }

    @Override
    public void enqueue(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (size >= backing.length) {
            backing = regrow();
        }
        if (back >= backing.length) {
            back = 0;
        }
        backing[back] = data;
        size++;
        back++;

    }

    @Override
    public T dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        if (front >= backing.length) {
            front = 0;
        }
        T cur = backing[front];
        backing[front] = null;
        size--;
        front++;
        return cur;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * regrows the backing array when the size+1 element has been added
     * @return a new array thats twice the size and contains all
     * the elements in the original backing array
     */
    private T[] regrow() {
        T[] arr = (T[]) new Object[size * 2];
        int onElem = 0;
        for (int i = front; i < size; i++) {
            if (backing[i] != null) {
                arr[onElem] = backing[i];
                onElem++;
            }
        }
        for (int i = 0; i < front; i++) {
            if (backing[i] != null) {
                arr[onElem] = backing[i];
                onElem++;
            }
        }
        front = 0;
        back = size;
        return arr;
    }

    /**
     * Returns the backing array for your queue. This is for grading purposes
     * only. DO NOT EDIT THIS METHOD.
     *
     * @return the backing array
     */
    public Object[] getBackingArray() {
        return backing;
    }
}
