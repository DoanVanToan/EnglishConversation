package com.framgia.englishconversation.screen.videoDetail;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.comment.CommentFragment;
import com.framgia.englishconversation.screen.profileuser.ProfileUserActivity;
import com.framgia.englishconversation.screen.timeline.OnTimelineItemTouchListener;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Exposes the data to be used in the VideoDetail screen.
 */

public class VideoDetailViewModel extends BaseObservable implements VideoDetailContract.ViewModel {

    private VideoDetailContract.Presenter mPresenter;
    private SimpleExoPlayer mSimpleExoPlayer;
    private long mCurrentPlaybackPosition = 0;
    private TimelineModel mTimelineModel;
    private Context mContext;
    private Navigator mNavigator;
    private FragmentManager mManager;
    private CommentFragment mFragment;
    private UserModel mTimelineUser;
    private OnTimelineItemTouchListener mTouchListener =
            new OnTimelineItemTouchListener<TimelineModel>() {
                @Override
                public void onItemTimelineClick(TimelineModel item) {
                    //TODO
                }

                /**
                 * @param item truyền vào khi người dùng click vào layout header item
                 * check điều kiện nếu userModel từ profile gừi sang trùng với user người tạo
                 * post thì không điều hướng sang activity profile mới
                 */
                @Override
                public void onItemUserNameClick(TimelineModel item) {
                    if (mTimelineUser != null && mTimelineUser.getId()
                            .equals(item.getCreatedUser().getId())) {
                        return;
                    }
                    mContext.startActivity(
                            ProfileUserActivity.getInstance(mContext, item.getCreatedUser()));
                }
            };

    VideoDetailViewModel(Context context, FragmentManager manager, TimelineModel timelineModel,
            UserModel userModel) {
        mContext = context;
        mTimelineModel = timelineModel;
        mNavigator = new Navigator((Activity) context);
        mManager = manager;
        mFragment = CommentFragment.newInstance(timelineModel.getId(), userModel);
        mTimelineUser = userModel;
    }

    @Bindable
    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

    public void setTimelineModel(TimelineModel timelineModel) {
        mTimelineModel = timelineModel;
        notifyPropertyChanged(BR.timelineModel);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
        if ((Util.SDK_INT <= Build.VERSION_CODES.M) || mSimpleExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        mPresenter.onPause();
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void setPresenter(VideoDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void finishActivity() {
        mNavigator.finishActivity();
    }

    @Bindable
    public SimpleExoPlayer getSimpleExoPlayer() {
        return mSimpleExoPlayer;
    }

    public void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer) {
        mSimpleExoPlayer = simpleExoPlayer;
        notifyPropertyChanged(BR.simpleExoPlayer);
    }

    private void initializePlayer() {
        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(mContext, new DefaultTrackSelector());
        setSimpleExoPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(mCurrentPlaybackPosition);
        Uri uri = Uri.parse(mTimelineModel.getMedias().get(Constant.INDEX_FIRST_ITEM).getUrl());
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory =
                new DefaultHttpDataSourceFactory(Constant.USER_AGENT);
        ExtractorMediaSource videoSource =
                new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);

        player.prepare(videoSource, true, false);
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mCurrentPlaybackPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
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
