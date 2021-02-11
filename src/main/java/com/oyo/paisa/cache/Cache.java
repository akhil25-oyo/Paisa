package com.oyo.paisa.cache;

import java.util.List;
import java.util.Map;

public interface Cache<K, V> {

    List<V> mget(List<K> keys);

    V get(K key);

    void batchSet(Map<K,V> keyAndValue);

    void set(K key, V value);

    void set(K key, V value, Long ttlSeconds);

    Boolean exists(K key);

    void delete(K key);
}
