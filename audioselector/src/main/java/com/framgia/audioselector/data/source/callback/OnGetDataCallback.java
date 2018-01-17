package com.framgia.audioselector.data.source.callback;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public interface OnGetDataCallback<T> {

    void onGetDataSuccess(T data);

    void onGetDataFailed();
}
