package com.framgia.englishconversation.screen.comment;

import android.databinding.BaseObservable;

/**
 * Exposes the data to be used in the Comment screen.
 */

public class CommentViewModel extends BaseObservable implements CommentContract.ViewModel {

    private CommentContract.Presenter mPresenter;

    public CommentViewModel() {
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
