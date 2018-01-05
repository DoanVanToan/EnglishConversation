package com.framgia.englishconversation.screen.comment;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.utils.OnEndScrollListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the Comment screen.
 */

public class CommentViewModel extends BaseObservable
        implements CommentContract.ViewModel, OnEndScrollListener.OnEndScroll {

    private CommentContract.Presenter mPresenter;
    private CommentAdapter mAdapter;
    private Context mContext;
    private OnEndScrollListener mOnEndScrollListener;

    public CommentViewModel(Context context) {
        mAdapter = new CommentAdapter(new ArrayList<Comment>(), this);
        mContext = context;
        mOnEndScrollListener = new OnEndScrollListener(this);
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
}
