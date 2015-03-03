import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class HashMap<K, V> implements HashMapInterface<K, V> {

    // Do not make any new instance variables.
    private MapEntry<K, V>[] table;
    private int size;

    /**
     * Create a hash map with no entries.
     */
    public HashMap() {
        table = new MapEntry[STARTING_SIZE];
        size = 0;
    }

    @Override
    public V add(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or Value cannot be null");
        }
        if (size + 1 > (int) (table.length * MAX_LOAD_FACTOR)) {
            regrow();
        }
        int index = Math.abs(key.hashCode()) % table.length;
        int ogIndex = index;
        MapEntry<K, V> entry = new MapEntry<K, V>(key, value);
        entry.setRemoved(false);
        boolean entered = false;
        V inBucket = null;
        int quadratic = 1;
        while (!entered) {
            if (table[index] == null) {
                table[index] = entry;
                entered = true;
            } else {
                if (table[index].isRemoved()) {
                    inBucket = checkDuplicate(entry, quadratic, ogIndex);
                    if (inBucket != null) {
                        size--;
                        entered = true;
                    } else {
                        table[index] = entry;
                        entered = true;
                    }
                } else if (table[index].getKey().equals(key)) {
                    inBucket = table[index].getValue();
                    table[index] = entry;
                    size--;
                    entered = true;
                } else {
                    index = ogIndex + quadratic * quadratic;
                    quadratic++;
                    if (index >= table.length) {
                        int factor = index / table.length;
                        index = index - table.length * factor;

                    }
                    // after a certain amount of time, it is not efficient to
                    // continue looking for spots
                    if (quadratic > table.length) {
                        entered = true;
                        regrow();
                        add(key, value);
                    }
                }
            }
        }
        size++;
        return inBucket;
    }

    /**
     * helper method so that if an encountered bucket has a removed object, the
     * table continues to look for duplicate keys
     *
     * @param entry
     *            entry that is being entered
     * @param quadratic
     *            current quadratic that is being added
     * @param ogIndex
     *            original hash index
     * @return value if entered, null if not
     */
    private V checkDuplicate(MapEntry<K, V> entry, int quadratic, int ogIndex) {
        int index = ogIndex + quadratic * quadratic;
        if (index >= table.length) {
            int factor = index / table.length;
            index = index - table.length * factor;

        }
        for (int i = 0; i < table.length * 5; i++) {
            if (table[index] == null) {
                return null;
            } else if (table[index].getKey().equals(entry.getKey())) {
                if (table[index].isRemoved()) {
                    return null;
                } else {
                    V temp = table[index].getValue();
                    table[index] = entry;
                    return temp;
                }
            } else {
                index = ogIndex + quadratic * quadratic;
                quadratic++;
                if (index >= table.length) {
                    int factor = index / table.length;
                    index = index - table.length * factor;
                }
            }
        }
        return null;
    }

    /**
     * regrows table to 2n +1 size
     */
    private void regrow() {
        MapEntry<K, V>[] tempTable = table;
        int actSize = size;
        table = new MapEntry[table.length * 2 + 1];
        for (int i = 0; i < tempTable.length; i++) {
            if (tempTable[i] != null && !tempTable[i].isRemoved()) {
                add(tempTable[i].getKey(), tempTable[i].getValue());
            }
        }
        size = actSize;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = Math.abs(key.hashCode()) % table.length;
        int ogIndex = index;
        V inBucket = null;
        int quadratic = 1;
        boolean removed = false;
        while (!removed) {
            if (table[index] == null) {
                throw new NoSuchElementException("Key Does Not Exist");
            } else if (table[index].getKey().equals(key)) {
                if (table[index].isRemoved()) {
                    throw new NoSuchElementException("Key Does Not Exist");
                } else {
                    inBucket = table[index].getValue();
                    table[index].setRemoved(true);
                    removed = true;
                }
            } else {
                index = ogIndex + quadratic * quadratic;
                quadratic++;
                if (index >= table.length) {
                    int factor = index / table.length;
                    index = index - table.length * factor;
                }
            }
        }
        size--;
        return inBucket;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        int index = Math.abs(key.hashCode()) % table.length;
        int ogIndex = index;
        V inBucket = null;
        int quadratic = 1;
        boolean found = false;
        while (!found) {
            if (table[index] == null) {
                throw new NoSuchElementException("Key Does Not Exist");
            } else if (table[index].getKey().equals(key)) {
                if (table[index].isRemoved()) {
                    throw new NoSuchElementException("Key Does Not Exist");
                } else {
                    inBucket = table[index].getValue();
                    found = true;
                }
            } else {
                index = ogIndex + quadratic * quadratic;
                quadratic++;
                if (index >= table.length) {
                    int factor = index / table.length;
                    index = index - table.length * factor;
                }
            }
        }
        return inBucket;

    }

    @Override
    public boolean contains(K key) {
        try {
            get(key);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public void clear() {
        table = new MapEntry[STARTING_SIZE];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MapEntry<K, V>[] toArray() {
        return table;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> keys = new HashSet<K>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                keys.add(table[i].getKey());
            }
        }
        return keys;
    }

    @Override
    public List<V> values() {
        LinkedList<V> vals = new LinkedList<V>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                vals.add(table[i].getValue());
            }
        }
        return vals;
    }

}
