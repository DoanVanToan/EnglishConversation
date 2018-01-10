package com.framgia.englishconversation.screen.comment;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.createcomment.CreateCommentActivity;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.OnEndScrollListener;
import com.framgia.englishconversation.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the Comment screen.
 */

public class CommentViewModel extends BaseObservable
        implements CommentContract.ViewModel, OnEndScrollListener.OnEndScroll {
    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String DIMEN = "dimen";
    private static final String ANDROID = "android";
    private CommentContract.Presenter mPresenter;
    private CommentAdapter mAdapter;
    private Context mContext;
    private OnEndScrollListener mOnEndScrollListener;
    private Navigator mNavigator;
    private TimelineModel mTimelineModel;
    private UserModel mUserModel;
    private int mDefaultHeight;

    public CommentViewModel(Context context, TimelineModel timelineModel) {
        mAdapter = new CommentAdapter(new ArrayList<Comment>(), this);
        mContext = context;
        mOnEndScrollListener = new OnEndScrollListener(this);
        mNavigator = new Navigator((Activity) mContext);
        mTimelineModel = timelineModel;
        mDefaultHeight =
                ((Activity) context).getWindow().getDecorView().getHeight() - getStatusBarHeight(
                        context);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
    }

    @Override
    public void onGetCommentsSuccess(List<Comment> comments) {
        mAdapter.updateData(comments);
    }

    @Override
    public void onGetCommentsFailure(String message) {
        mOnEndScrollListener.setIsFetchingData(false);
    }

    @Override
    public void onGetCommentSuccess(Comment comment) {
        mAdapter.updateDataForward(comment);
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
    public void onPause() {
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.fetchCommentData(null);
    }

    @Bindable
    public CommentAdapter getAdapter() {
        return mAdapter;
    }

    @Bindable
    public void setAdapter(CommentAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    @Override
    public void onGetCurrentUserSuccess(UserModel data) {
        setUserModel(data);
    }

    @Override
    public void onGetCurrentUserFailed(String msg) {

    }

    @Override
    public void onEndScrolled() {
        mPresenter.fetchCommentData(mAdapter.getLastComment());
    }

    public void onCreateCommentTouched() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_TIMELINE, mTimelineModel);
        bundle.putParcelable(Constant.EXTRA_USER, mUserModel);
        mNavigator.startActivityForResult(CreateCommentActivity.class, bundle,
                Constant.RequestCode.POST_COMMENT);
    }

    @Bindable
    public UserModel getUserModel() {
        return mUserModel;
    }

    public void setUserModel(UserModel userModel) {
        mUserModel = userModel;
        notifyPropertyChanged(BR.userModel);
    }

    @Bindable
    public int getDefaultHeight() {
        return mDefaultHeight;
    }

    public void setDefaultHeight(int defaultHeight) {
        mDefaultHeight = defaultHeight;
        notifyPropertyChanged(BR.defaultHeight);
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
