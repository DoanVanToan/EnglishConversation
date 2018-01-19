package com.framgia.audioselector;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public interface BaseViewModel<T extends BasePresenter> {

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void setPresenter(T presenter);
}
