package com.framgia.englishconversation.data.source.local.sharedprf;

/**
 *
 */

public interface SharedPrefsApi {
    <T> T get(String key, Class<T> clazz);

    Boolean get(String key, boolean defaultValue);

    <T> void put(String key, T data);

    void clear();
}
