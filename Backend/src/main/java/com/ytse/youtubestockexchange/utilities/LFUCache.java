package com.ytse.youtubestockexchange.utilities;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class LFUCache<K, V> {

    int CACHE_SIZE, size, minFreq;
    HashMap<K, V> valueMap;
    HashMap<K, Integer> countMap;
    HashMap<Integer, LinkedHashSet<K>> listMap;

    public LFUCache(int CACHE_SIZE) {
        this.CACHE_SIZE = CACHE_SIZE;
        this.size = 0;
        this.minFreq = 0;
        valueMap = new HashMap<K, V>();
        countMap = new HashMap<K, Integer>();
        listMap = new HashMap<Integer, LinkedHashSet<K>>();
        listMap.put(1, new LinkedHashSet<K>());
    }

    public boolean contains(K key) {
        if(valueMap.containsKey(key))
            return true;
        return false;
    }

    public V get(K key) {
        if(!contains(key))
            return null;
        
        int freq = countMap.get(key);
        listMap.get(freq).remove(key);
        freq++;
        countMap.put(key, freq);
        if(!listMap.containsKey(freq))
            listMap.put(freq, new LinkedHashSet<K>());
        listMap.get(freq).add(key);
        if(!listMap.containsKey(minFreq) || listMap.get(minFreq).size()==0)
            minFreq = freq;

        return valueMap.get(key);
    }

    public void put(K key, V value) {
        if(contains(key)) {
            valueMap.put(key, value);
            get(key);
        }
        if(size >= CACHE_SIZE) {
            K evit = listMap.get(minFreq).iterator().next();
            listMap.get(minFreq).remove(evit);
            valueMap.remove(evit);
            countMap.remove(evit);
            size--;
        }
        size++;
        valueMap.put(key, value);
        countMap.put(key, 1);
        listMap.get(1).add(key);
        minFreq = 1;
    }
}
