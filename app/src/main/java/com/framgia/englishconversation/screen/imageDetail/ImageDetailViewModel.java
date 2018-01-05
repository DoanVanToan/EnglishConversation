package com.framgia.englishconversation.screen.imagedetail;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.screen.comment.CommentFragment;
import com.framgia.englishconversation.screen.createcomment.CreateCommentActivity;
import com.framgia.englishconversation.screen.selectedimagedetail.SelectedImageDetailActivity;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.navigator.Navigator;
import java.util.List;

/**
 * Exposes the data to be used in the ImageDetail screen.
 */

public class ImageDetailViewModel extends BaseObservable implements ImageDetailContract.ViewModel {

    private ImageDetailContract.Presenter mPresenter;
    private TimelineModel mTimelineModel;
    private FragmentManager mManager;
    private Fragment mFragment;
    private Context mContext;
    private Navigator mNavigator;
    private OnMediaModelItemTouchListener<List<MediaModel>> mListener =
            new OnMediaModelItemTouchListener<List<MediaModel>>() {
                @Override
                public void onTouchListener(List<MediaModel> items, int postion) {
                    mContext.startActivity(
                            SelectedImageDetailActivity.getIntent(mContext, items, postion));
                }
            };

    public ImageDetailViewModel(Context context, TimelineModel timelineModel,
            FragmentManager manager) {
        mContext = context;
        mTimelineModel = timelineModel;
        mManager = manager;
        mFragment = CommentFragment.newInstance();
        mNavigator = new Navigator((Activity) context);
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

    @Bindable
    public OnMediaModelItemTouchListener<List<MediaModel>> getListener() {
        return mListener;
    }

    public void setListener(OnMediaModelItemTouchListener<List<MediaModel>> listener) {
        mListener = listener;
        notifyPropertyChanged(BR.listener);
    }

    public void onCreateCommentTouched() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.EXTRA_TIMELINE, mTimelineModel);
        mNavigator.startActivityForResult(CreateCommentActivity.class, bundle,
                Constant.RequestCode.POST_COMMENT);
    }
}
