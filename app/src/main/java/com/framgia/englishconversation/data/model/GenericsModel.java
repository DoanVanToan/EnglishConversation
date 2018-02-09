package com.framgia.englishconversation.data.model;

/**
 * Created by Sony on 2/12/2018.
 */

/**
 * @param K Key
 * @param V Value
 * @return
 */
public class GenericsModel<K, V> {
    private K mKey;
    private V mValue;

    public GenericsModel(K key, V value) {
        mKey = key;
        mValue = value;
    }

    public K getKey() {
        return mKey;
    }

    public void setKey(K key) {
        mKey = key;
    }

    public V getValue() {
        return mValue;
    }

    public void setValue(V value) {
        mValue = value;
    }
}
