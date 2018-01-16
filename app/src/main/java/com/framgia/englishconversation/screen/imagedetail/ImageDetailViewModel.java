package com.framgia.englishconversation.screen.imagedetail;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.FragmentManager;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.screen.comment.CommentFragment;
import com.framgia.englishconversation.screen.profileuser.ProfileUserActivity;
import com.framgia.englishconversation.screen.selectedimagedetail.SelectedImageDetailActivity;
import com.framgia.englishconversation.screen.timeline.OnTimelineItemTouchListener;
import java.util.List;

/**
 * Exposes the data to be used in the ImageDetail screen.
 */

public class ImageDetailViewModel extends BaseObservable implements ImageDetailContract.ViewModel {

    private ImageDetailContract.Presenter mPresenter;
    private TimelineModel mTimelineModel;
    private FragmentManager mManager;
    private CommentFragment mFragment;
    private Context mContext;
    private OnMediaModelItemTouchListener mListener =
            new OnMediaModelItemTouchListener<List<MediaModel>>() {
                @Override
                public void onTouchListener(List<MediaModel> items, int postion) {
                    mContext.startActivity(
                            SelectedImageDetailActivity.getIntent(mContext, items, postion));
                }
            };
    private OnTimelineItemTouchListener mTouchListener =
            new OnTimelineItemTouchListener<TimelineModel>() {
                @Override
                public void onItemTimelineClick(TimelineModel item) {
                    //TODO
                }

                @Override
                public void onItemUserNameClick(TimelineModel item) {
                    mContext.startActivity(
                            ProfileUserActivity.getInstance(mContext, item.getCreatedUser()));
                }
            };

    public ImageDetailViewModel(Context context, TimelineModel timelineModel,
            FragmentManager manager) {
        mContext = context;
        mTimelineModel = timelineModel;
        mManager = manager;
        mFragment = CommentFragment.newInstance(timelineModel.getId());
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
    public CommentFragment getFragment() {
        return mFragment;
    }

    public void setFragment(CommentFragment fragment) {
        mFragment = fragment;
        notifyPropertyChanged(BR.fragment);
    }

    @Bindable
    public OnMediaModelItemTouchListener getListener() {
        return mListener;
    }

    public void setListener(OnMediaModelItemTouchListener listener) {
        mListener = listener;
        notifyPropertyChanged(BR.listener);
    }

    public void onClickComment() {
        mFragment.show(mManager, mFragment.getTag());
    }

    @Bindable
    public OnTimelineItemTouchListener getTouchListener() {
        return mTouchListener;
    }

    public void setTouchListener(OnTimelineItemTouchListener touchListener) {
        mTouchListener = touchListener;
        notifyPropertyChanged(BR.touchListener);
    }
}

