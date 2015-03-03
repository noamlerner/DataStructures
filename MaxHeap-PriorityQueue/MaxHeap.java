import java.util.NoSuchElementException;

public class MaxHeap<T extends Comparable<? super T>> implements
        HeapInterface<T> {

    private T[] arr;
    private int size;

    // Do not add any more instance variables

    /**
     * Creates a MaxHeap.
     */
    @SuppressWarnings("unchecked")
    public MaxHeap() {
        arr = (T[]) new Comparable[STARTING_SIZE];
        size = 1;
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (arr[arr.length - 1] != null) {
            resizeBackingArray();
        }
        int itemPos = size;
        arr[itemPos] = item;
        for (int i = size / 2; i > 0; i = i / 2) {
            if (item.compareTo(arr[i]) > 0) {
                T obj = arr[i];
                arr[i] = item;
                arr[itemPos] = obj;
                itemPos = i;
            } else {
                i = 0;
            }
        }
        size++;

    }

    /**
     * when called, this will create a new array that is double the size of arr
     * copy all of arr's contents over and assign this array to arr.
     */
    private void resizeBackingArray() {
        T[] tempArr = (T[]) new Comparable[arr.length * 2];
        for (int i = 1; i < arr.length; i++) {
            tempArr[i] = arr[i];
        }
        arr = tempArr;
    }

    @Override
    public T remove() {
        if (size == 1) {
            throw new NoSuchElementException("Heap is empty");
        }
        T obj = arr[1];
        arr[1] = arr[size - 1];
        arr[size - 1] = null;
        size--;
        int atLoc = 1;
        for (int i = 2; i < size; i = i * 2) {
            int biggerChild;
            if (arr[i + 1] != null) {
                biggerChild = arr[i].compareTo(arr[i + 1]) > 0 ? i : i + 1;
            } else {
                biggerChild = i;
            }
            if (arr[atLoc].compareTo(arr[biggerChild]) < 0) {
                T temp = arr[atLoc];
                arr[atLoc] = arr[biggerChild];
                arr[biggerChild] = temp;
                atLoc = biggerChild;
            } else {
                i = size;
            }
        }
        return obj;
    }

    @Override
    public boolean isEmpty() {
        return size == 1;
    }

    @Override
    public int size() {
        return size - 1;
    }

    @Override
    public void clear() {
        arr = (T[]) new Comparable[STARTING_SIZE];
        size = 1;
    }

    @Override
    public Comparable[] getBackingArray() {
        return arr;
    }
}
