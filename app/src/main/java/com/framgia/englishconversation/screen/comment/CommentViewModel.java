package com.framgia.englishconversation.screen.comment;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.screen.createcomment.CreateCommentFragment;
import com.framgia.englishconversation.screen.profileuser.ProfileUserActivity;
import com.framgia.englishconversation.screen.selectedimagedetail.SelectedImageDetailActivity;
import com.framgia.englishconversation.screen.timeline.OnTimelineItemTouchListener;
import com.framgia.englishconversation.utils.OnEndScrollListener;
import com.framgia.englishconversation.utils.navigator.Navigator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Exposes the data to be used in the Comment screen.
 */

public class CommentViewModel extends BaseObservable
        implements CommentContract.ViewModel, OnEndScrollListener.OnEndScroll,
        OnTimelineItemTouchListener<Comment> {
    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String DIMEN = "dimen";
    private static final String ANDROID = "android";
    private CommentContract.Presenter mPresenter;
    private CommentAdapter mAdapter;
    private Context mContext;
    private OnEndScrollListener mOnEndScrollListener;
    private Navigator mNavigator;
    private String mTimelineModelId;
    private int mDefaultHeight;
    private FragmentManager mManager;
    private Fragment mFragment;

    public CommentViewModel(Context context, String timelineModelId, FragmentManager manager) {
        mAdapter = new CommentAdapter(new ArrayList<Comment>(), this);
        mContext = context;
        mOnEndScrollListener = new OnEndScrollListener(this);
        mNavigator = new Navigator((Activity) mContext);
        mTimelineModelId = timelineModelId;
        mDefaultHeight =
                ((Activity) context).getWindow().getDecorView().getHeight() - getStatusBarHeight(
                        context);
        mManager = manager;
        mFragment = CreateCommentFragment.getInstance(timelineModelId);
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
    public void onEndScrolled() {
        mPresenter.fetchCommentData(mAdapter.getLastComment());
    }

    public void onCreateCommentTouched() {
      /*  Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_TIMELINE, mTimelineModelId);
        bundle.putParcelable(Constant.EXTRA_USER, mUserModel);
        mNavigator.startActivityForResult(CreateCommentFragment.class, bundle,
                Constant.RequestCode.POST_COMMENT);*/
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
    }

    @Override
    public void onItemTimelineClick(Comment item) {

        mContext.startActivity(SelectedImageDetailActivity.getIntent(mContext,
                new ArrayList(Arrays.asList(item.getMediaModel())), 0));
    }

    @Override
    public void onItemUserNameClick(Comment item) {
        mNavigator.startActivity(ProfileUserActivity.getInstance(mContext, item.getCreateUser()));
    }
}
