package com.framgia.englishconversation.screen.timeline;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.audiodetail.AudioDetailActivity;
import com.framgia.englishconversation.screen.createPost.CreatePostActivity;
import com.framgia.englishconversation.screen.videoDetail.VideoDetailActivity;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.navigator.Navigator;

import java.util.ArrayList;

/**
 * Exposes the data to be used in the Timeline screen.
 */

public class TimelineViewModel extends BaseObservable
        implements TimelineContract.ViewModel, OnTimelineItemTouchListener<TimelineModel> {

    private TimelineContract.Presenter mPresenter;
    private Navigator mNavigator;
    private Context mContext;
    private ObservableField<UserModel> mUser = new ObservableField<>();
    private String mUserUrl;
    private TimelineAdapter mAdapter;

    public TimelineViewModel(Context context, Navigator navigator) {
        mContext = context;
        mNavigator = navigator;
        mAdapter = new TimelineAdapter(new ArrayList<TimelineModel>(), this);
        mAdapter.setRecyclerViewItemClickListener(this);
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
    public void setPresenter(TimelineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetUserSuccess(UserModel data) {
        mUser.set(data);
        setUserUrl(data.getPhotoUrl().toString());
    }

    @Override
    public void onCreateNewPostClick() {
        mNavigator.startActivity(CreatePostActivity.getInstance(mContext));
    }

    @Override
    public void onChildAdded(TimelineModel timeline) {
        mAdapter.updateData(timeline);
    }

    public ObservableField<UserModel> getUser() {
        return mUser;
    }

    public void setUser(ObservableField<UserModel> user) {
        mUser = user;
    }

    @Bindable
    public String getUserUrl() {
        return mUserUrl;
    }

    public void setUserUrl(String userUrl) {
        mUserUrl = userUrl;
        notifyPropertyChanged(BR.userUrl);
    }

    public TimelineAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(TimelineAdapter adapter) {
        mAdapter = adapter;
    }

    public void onAudioTimeLineClick(TimelineModel timelineModel, View view) {
        Intent intent = AudioDetailActivity.getInstance(mContext, timelineModel);
        mNavigator.startActivityBySharedElement(intent, view, ViewCompat.getTransitionName(view));
    }

    @Override
    public void onHeaderTouchListener(TimelineModel timelineModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_TIMELINE, timelineModel);
        mNavigator.startActivity(VideoDetailActivity.class, bundle);
    }

}
