package com.github.snakebook.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Generates an arbitrary random value.
 *
 * @param <T> an object type to return as random value.
 */
public class ArbitraryRandom<T> {

    /**
     * Pair object to create represents pair
     *
     * @param <K> key class
     * @param <V> value class
     */
    public static class Pair<K, V> {
        public final K key;
        public final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public static <X, Y> Pair<X, Y> create(X key, Y value) {
            return new Pair<>(key, value);
        }
    }

    private int totalOccurrences = 0;
    private final LinkedList<Pair<Integer, T>> arbitraryItems = new LinkedList<>();

    public void add(int occurrence, T object) {
        if (occurrence <= 0) {
            throw new UnsupportedOperationException("An occurrence should be greater than 0.");
        }
        this.arbitraryItems.add(Pair.create(occurrence, object));
        this.totalOccurrences += occurrence;
    }

    /**
     * An optimized object to generate a random value from pairs.
     *
     * @return a random object from arbitrary item pairs.
     */
    public T getRandom() {
        LinkedList<Pair<Integer, T>> cloneMap = new LinkedList<>(arbitraryItems);

        // Start to generate a random integer then return an object in range [0, integer]
        int resultInteger = MathHelper.randomNextInt(0, totalOccurrences);

        // Pop out bottom-to-top
        Pair<Integer, T> currentEntry = cloneMap.element();
        while (resultInteger >= 0) {
            currentEntry = cloneMap.pop();
            resultInteger -= currentEntry.getKey();
        }

        return currentEntry.getValue();
    }

    @SafeVarargs
    public static <K> ArbitraryRandom<K> createInstance(Pair<Integer, K>... values) {
        ArbitraryRandom<K> arbitraryRandom = new ArbitraryRandom<>();
        for (Pair<Integer, K> value : values) {
            arbitraryRandom.add(value.key, value.value);
        }
        return arbitraryRandom;
    }

}
