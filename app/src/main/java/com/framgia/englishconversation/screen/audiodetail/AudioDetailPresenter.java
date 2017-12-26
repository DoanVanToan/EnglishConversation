package com.framgia.englishconversation.screen.audiodetail;

/**
 * Created by fs-sournary.
 * Date on 12/19/17.
 * Description:
 */

public class AudioDetailPresenter implements AudioDetailContract.Presenter {

    private AudioDetailContract.View mView;

    public AudioDetailPresenter(AudioDetailContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
