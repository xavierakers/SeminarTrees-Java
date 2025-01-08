package main.java.com.xakers.model;

/**
 * Record class is a generic container that holds a key-value pair.
 * Provides getter and setter methods for both the key and value.
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 * @author Xavier Akers
 * @version 2025-01-05
 * @since 2025-01-05
 */
public class Record<K, V> {
    private K key;      // Key of the record
    private V value;    // Value of the record

    /**
     * Constructs a new Record with the specified key and value.
     *
     * @param key   The key of the record.
     * @param value The value of the record.
     */
    public Record(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of the record.
     *
     * @return The key of the record.
     */
    public K getKey() {
        return key;
    }

    /**
     * Sets the key for the record.
     *
     * @param key The key to set for the record.
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Returns the value associated with the key of the record.
     *
     * @return The value associated with the key
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets the value for the record.
     *
     * @param value The value to set for the record.
     */
    public void setValue(V value) {
        this.value = value;
    }
}
