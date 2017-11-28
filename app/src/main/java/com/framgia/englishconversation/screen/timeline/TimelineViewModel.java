package com.framgia.englishconversation.screen.timeline;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.PostType;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.createPost.CreatePostActivity;
import com.framgia.englishconversation.utils.navigator.Navigator;
import java.util.ArrayList;

/**
 * Exposes the data to be used in the Timeline screen.
 */

public class TimelineViewModel extends BaseObservable implements TimelineContract.ViewModel {

    private TimelineContract.Presenter mPresenter;
    private Navigator mNavigator;
    private Context mContext;
    private ObservableField<UserModel> mUser = new ObservableField<>();
    private String mUserUrl;
    private TimelineAdapter mAdapter;

    public TimelineViewModel(Context context, Navigator navigator) {
        mContext = context;
        mNavigator = navigator;
        mAdapter = new TimelineAdapter(new ArrayList<TimelineModel>());
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
    public void onCreateNewPostClick(@PostType int createType) {
        mNavigator.startActivity(CreatePostActivity.getInstance(mContext, createType));
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
}
