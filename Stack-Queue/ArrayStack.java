import java.util.NoSuchElementException;

/**
 * ArrayStack Implementation of a stack using an array as a backing structure
 *
 * @author Noam Lerner
 * @version 1.0
 */
public class ArrayStack<T> implements StackADT<T> {

    // Do not add instance variables
    private T[] backing;
    private int size;

    /**
     * Construct an ArrayStack with an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public ArrayStack() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Construct an ArrayStack with the specified initial capacity of
     * initialCapacity
     *
     * @param initialCapacity
     *            Initial capacity of the backing array.
     */
    public ArrayStack(int initialCapacity) {
        backing = (T[]) new Object[initialCapacity];
        size = 0;
    }

    @Override
    public void push(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (size >= backing.length) {
            backing = regrow();
        }
        backing[size] = data;
        size++;
    }

    @Override
    public T pop() {
        if (size == 0) {
            throw new NoSuchElementException("Stack is empty");
        }
        T cur = backing[size - 1];
        backing[size - 1] = null;
        size--;
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
     *
     * @return new array containing all the elements, with twice as many
     * containers
     */
    public T[] regrow() {
        T[] arr = (T[]) new Object[size * 2];
        for (int i = 0; i < size; i++) {
            arr[i] = backing[i];
        }
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
