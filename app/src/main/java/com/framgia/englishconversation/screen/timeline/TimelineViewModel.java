package com.framgia.englishconversation.screen.timeline;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.Setting;
import com.framgia.englishconversation.data.model.Status;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.audiodetail.AudioDetailActivity;
import com.framgia.englishconversation.screen.conversationdetail.ConversationDetailActivity;
import com.framgia.englishconversation.screen.createPost.CreatePostActivity;
import com.framgia.englishconversation.screen.dialog.OptionPostFragment;
import com.framgia.englishconversation.screen.imagedetail.ImageDetailActivity;
import com.framgia.englishconversation.screen.profileuser.ProfileUserActivity;
import com.framgia.englishconversation.screen.videoDetail.VideoDetailActivity;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.OnEndScrollListener;
import com.framgia.englishconversation.utils.navigator.Navigator;

import java.util.ArrayList;
import java.util.List;

import static com.framgia.englishconversation.utils.Constant.EXTRA_USER;

/**
 * Exposes the data to be used in the Timeline screen.
 */

public class TimelineViewModel extends BaseObservable
        implements TimelineContract.ViewModel, OnTimelineItemTouchListener<TimelineModel>,
        OnEndScrollListener.OnEndScroll {

    private TimelineContract.Presenter mPresenter;
    private Navigator mNavigator;
    private Context mContext;
    private UserModel mUserModel;
    private TimelineAdapter mAdapter;
    private OnEndScrollListener mOnEndScrollListener;
    private UserModel mTimelineUser;
    private boolean mIsAllowCreatePost;
    private Setting mSetting;
    private boolean mIsLoadingMore;

    public TimelineViewModel(Context context, Navigator navigator, UserModel userModel) {
        mContext = context;
        mNavigator = navigator;
        mAdapter = new TimelineAdapter(new ArrayList<TimelineModel>(), this);
        mAdapter.setRecyclerViewItemClickListener(this);
        mOnEndScrollListener = new OnEndScrollListener(this);
        mTimelineUser = userModel;
        mIsLoadingMore = false;
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
        setLoadingMore(true);
        mPresenter.fetchTimelineData(null, mTimelineUser);
    }

    @Override
    public void onGetUserSuccess(UserModel data) {
        setUserModel(data);
        setAllowCreatePost(allowCreatePost(data));
    }

    /**
     * allow show create post button
     * if this screen is timeline main screen, or this screen is current user profile
     */
    private boolean allowCreatePost(UserModel currentUser) {
        if (mTimelineUser == null) {
            return true;
        }
        return currentUser != null && currentUser.getId().equals(mTimelineUser.getId());
    }

    @Override
    public void onCreateNewPostClick() {
        mNavigator.startActivity(CreatePostActivity.getInstance(mContext));
    }

    @Bindable
    public OnEndScrollListener getOnEndScrollListener() {
        return mOnEndScrollListener;
    }

    public void setOnEndScrollListener(OnEndScrollListener onEndScrollListener) {
        mOnEndScrollListener = onEndScrollListener;
        notifyPropertyChanged(BR.onEndScrollListener);
    }

    @Override
    public void onGetTimelinesSuccess(List<TimelineModel> timelines) {
        mOnEndScrollListener.setIsFetchingData(false);
        mAdapter.updateData(timelines);
        setLoadingMore(false);
    }

    @Override
    public void onGetTimelinesFailure(String message) {
        mOnEndScrollListener.setIsFetchingData(false);
        setLoadingMore(false);
    }

    @Override
    public void onGetTimelineSuccess(TimelineModel timelineModel) {
        if (timelineModel == null) {
            return;
        }
        if (!mAdapter.isExitTimeline(timelineModel)) {
            mAdapter.addTimeline(timelineModel);
            return;
        }
        if (timelineModel.getStatusModel() == null
                || timelineModel.getStatusModel().getStatus() == Status.ADD) {
            mAdapter.updateTimeline(timelineModel);
            return;
        }
        if (timelineModel.getStatusModel().getStatus() == Status.DELETE) {
            mAdapter.deleteTimeline(timelineModel);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
    }

    @Override
    public void onGetSettingSuccess(Setting setting) {
        setSetting(setting);
    }

    public TimelineAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(TimelineAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onItemTimelineClick(TimelineModel timelineModel) {
        switch (timelineModel.getPostType()) {
            case MediaModel.MediaType.VIDEO:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.EXTRA_TIMELINE, timelineModel);
                bundle.putParcelable(EXTRA_USER, mTimelineUser);
                mNavigator.startActivity(VideoDetailActivity.class, bundle);
                break;
            case MediaModel.MediaType.ONLY_TEXT:
            case MediaModel.MediaType.IMAGE:
                mNavigator.startActivity(
                        ImageDetailActivity.getInstance(mContext, timelineModel, mTimelineUser));
                break;
            case MediaModel.MediaType.CONVERSATION:
                mNavigator.startActivity(
                        ConversationDetailActivity.getInstance(mContext, timelineModel,
                                mTimelineUser));
                break;
            case MediaModel.MediaType.AUDIO:
                mNavigator.startActivity(
                        AudioDetailActivity.getInstance(mContext, timelineModel, mTimelineUser));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemUserNameClick(TimelineModel item) {
        if (mTimelineUser != null && mTimelineUser.getId().equals(item.getCreatedUser().getId())) {
            return;
        }
        mNavigator.startActivity(ProfileUserActivity.getInstance(mContext, item.getCreatedUser()));
    }

    @Override
    public boolean onItemLongClick(View viewGroup, TimelineModel item) {

        return true;
    }

    @Override
    public void onItemOptionClick(TimelineModel item) {
        OptionPostFragment.newInstance(item)
                .show(((AppCompatActivity) mContext).getSupportFragmentManager(), null);
    }


    @Override
    public void onEndScrolled() {
        setLoadingMore(true);
        mPresenter.fetchTimelineData(mAdapter.getLastItem(), mTimelineUser);
    }

    public void setUserModel(UserModel userModel) {
        mUserModel = userModel;
        notifyPropertyChanged(BR.userModel);
    }

    @Bindable
    public UserModel getUserModel() {
        return mUserModel;
    }

    @Bindable
    public boolean isAllowCreatePost() {
        return mIsAllowCreatePost;
    }

    public void setAllowCreatePost(boolean allowCreatePost) {
        mIsAllowCreatePost = allowCreatePost;
        notifyPropertyChanged(BR.allowCreatePost);
    }

    @Bindable
    public Setting getSetting() {
        return mSetting;
    }

    public void setSetting(Setting setting) {
        mSetting = setting;
        notifyPropertyChanged(BR.setting);
    }

    @Bindable
    public boolean isLoadingMore() {
        return mIsLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        mIsLoadingMore = loadingMore;
        notifyPropertyChanged(BR.loadingMore);
    }
}
