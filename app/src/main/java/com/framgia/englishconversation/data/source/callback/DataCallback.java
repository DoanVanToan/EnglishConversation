package com.framgia.englishconversation.data.source.callback;

/**
 * Created by framgia on 11/05/2017.
 */

public interface DataCallback<T> {
    void onGetDataSuccess(T data);

    void onGetDataFailed(String msg);
}
