package com.framgia.englishconversation.screen.imagedetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.screen.comment.CommentFragment;

/**
 * Exposes the data to be used in the ImageDetail screen.
 */

public class ImageDetailViewModel extends BaseObservable implements ImageDetailContract.ViewModel {

    private ImageDetailContract.Presenter mPresenter;
    private TimelineModel mTimelineModel;
    private FragmentManager mManager;
    private Fragment mFragment;

    public ImageDetailViewModel(TimelineModel timelineModel, FragmentManager manager) {
        mTimelineModel = timelineModel;
        mManager = manager;
        mFragment = CommentFragment.newInstance();
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
    public void setPresenter(ImageDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

    public void setTimelineModel(TimelineModel timelineModel) {
        mTimelineModel = timelineModel;
        notifyPropertyChanged(BR.timelineModel);
    }

    @Bindable
    public FragmentManager getManager() {
        return mManager;
    }

    public void setManager(FragmentManager manager) {
        mManager = manager;
        notifyPropertyChanged(BR.manager);
    }

    @Bindable
    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
        notifyPropertyChanged(BR.fragment);
    }
}
