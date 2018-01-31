package com.framgia.englishconversation.screen.dialog;

import android.databinding.BaseObservable;
import android.view.View;

import com.framgia.englishconversation.data.model.TimelineModel;

/**
 * Created by Sony on 1/24/2018.
 */

public class OptionPostViewModel extends BaseObservable implements OptionPostContract.ViewModel,
        OnOptionItemClickListener{

    private OptionPostPresenter mPresenter;
    private TimelineModel mTimelineModel;

    public OptionPostViewModel(TimelineModel timelineModel) {
        mTimelineModel = timelineModel;
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
    public void setPresenter(OptionPostContract.Presenter presenter) {
        mPresenter = (OptionPostPresenter) presenter;
    }

    @Override
    public void onClickEditPost() {
        mPresenter.editPost(mTimelineModel);
    }

    @Override
    public void onClickDeletePost() {
        showConfirmDeleteDialog();
    }

    @Override
    public void showConfirmDeleteDialog() {

    }

}
