package assignment4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MyHashTable<K, V> implements Iterable<HashPair<K, V>> {
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K, V>>> buckets;

    // constructor
    public MyHashTable(int initialCapacity) {
        // ADD YOUR CODE BELOW THIS

        if (initialCapacity < 0)
            throw new IllegalArgumentException("Capacity must be > 0");

        this.numBuckets = initialCapacity;
        this.buckets = new ArrayList<LinkedList<HashPair<K, V>>>(initialCapacity);

        for (int i = 0; i < this.numBuckets; i++)
            this.buckets.add(new LinkedList<>());

        //ADD YOUR CODE ABOVE THIS
    }

    public int size() {
        return this.numEntries;
    }

    public int numBuckets() {
        return this.numBuckets;
    }

    /**
     * Returns the buckets vairable. Usefull for testing  purposes.
     */
    public ArrayList<LinkedList<HashPair<K, V>>> getBuckets() {
        return this.buckets;
    }

    /**
     * Given a key, return the bucket position for the key.
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode()) % this.numBuckets;
        return hashValue;
    }

    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
        //  ADD YOUR CODE BELOW HERE

        HashPair<K, V> h = new HashPair(key, value);
        int index = hashFunction(key);

        LinkedList<HashPair<K, V>> list = buckets.get(index);

        for (HashPair cur : list)
            if (cur.getValue().equals(key)) {
                Object old = cur.getValue();
                cur.setValue(value);
                return (V) old;
            }

        numEntries++;
        buckets.get(index).add(h);

        double loadFactor = 1.0 * this.numEntries / numBuckets;

        if (loadFactor > MAX_LOAD_FACTOR)
            rehash();

        return null;

        //  ADD YOUR CODE ABOVE HERE
    }


    /**
     * Get the value corresponding to key. Expected average runtime = O(1)
     */

    public V get(K key) {
        //ADD YOUR CODE BELOW HERE

        int index = hashFunction(key);
        LinkedList<HashPair<K, V>> list = buckets.get(index);

        for (HashPair cur : list)
            if (cur.getKey().equals(key))
                return (V) cur.getValue();

        return null;
        //ADD YOUR CODE ABOVE HERE
    }

    /**
     * Remove the HashPair correspoinding to key . Expected average runtime O(1)
     */
    public V remove(K key) {
        //ADD YOUR CODE BELOW HERE
        int index = hashFunction(key);
        LinkedList<HashPair<K, V>> list = buckets.get(index);

        for (HashPair cur : list)
            if (cur.getKey().equals(key)) {
                Object old = cur.getValue();
                list.remove(cur);
                numEntries--;
                return (V) old;
            }
        return null;
        //ADD YOUR CODE ABOVE HERE
    }

    // Method to double the size of the hashtable if load factor increases
    // beyond MAX_LOAD_FACTOR.
    // Made public for ease of testing.

    public void rehash() {
        //ADD YOUR CODE BELOW HERE
        numBuckets = numBuckets * 2;
        ArrayList<LinkedList<HashPair<K, V>>> old = buckets;
        buckets = new ArrayList<>(numBuckets);

        for (int i = 0; i < numBuckets; i++)
            buckets.add(new LinkedList<>());

        for (LinkedList<HashPair<K, V>> prevList : old)
            for (HashPair<K, V> cur : prevList)
                buckets.get(hashFunction(cur.getKey())).addLast(new HashPair(cur.getKey(), cur.getValue()));

        //ADD YOUR CODE ABOVE HERE
    }


    /**
     * Return a list of all the keys present in this hashtable.
     */

    public ArrayList<K> keys() {
        //ADD YOUR CODE BELOW HERE

        ArrayList<K> list = new ArrayList<>();
        int arraySize = buckets.size();
        for (int i = 0; i < arraySize; i++)
            for (HashPair cur : buckets.get(i)) {
                K k = (K) cur.getKey();
                list.add(k);
            }
        return list;
        //ADD YOUR CODE ABOVE HERE
    }

    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(n)
     */
    public ArrayList<V> values() {
        //ADD CODE BELOW HERE

        MyHashTable<V, K> t = new MyHashTable<V, K>(numBuckets);
        ArrayList<V> list = t.keys();
        int arraySize = buckets.size();

        for (int i = 0; i < arraySize; i++) {
            LinkedList<HashPair<K, V>> lList = buckets.get(i);
            for (HashPair<K, V> cur : lList) {
                V v = cur.getValue();
                K k = t.get(v);
                t.put(v, k);
                if (t.get(v) == null) {
                    t.put(v, (K) v);
                    list.add(v);
                }
            }
        }
        return list;
        //ADD CODE ABOVE HERE
    }


    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }

    private class MyHashIterator implements Iterator<HashPair<K, V>> {
        private LinkedList<HashPair<K, V>> entries;

        private MyHashIterator() {
            //ADD YOUR CODE BELOW HERE

            entries = new LinkedList<>();

            for (LinkedList<HashPair<K, V>> list : buckets)
                for (HashPair<K, V> cur : list)
                    entries.add(cur);

            //ADD YOUR CODE ABOVE HERE
        }

        @Override
        public boolean hasNext() {
            //ADD YOUR CODE BELOW HERE
            return (entries.peekFirst() != null);// remove
            //ADD YOUR CODE ABOVE HERE
        }

        @Override
        public HashPair<K, V> next() {
            //ADD YOUR CODE BELOW HERE
            return entries.removeFirst();//remove
            //ADD YOUR CODE ABOVE HERE
        }

    }
}
