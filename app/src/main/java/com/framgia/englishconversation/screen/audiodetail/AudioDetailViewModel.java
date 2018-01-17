package com.framgia.englishconversation.screen.audiodetail;

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
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.ExoPlayer.STATE_ENDED;

/**
 * Created by fs-sournary.
 * Date on 12/19/17.
 * Description: ViewModel of audio detail screen
 */

public class AudioDetailViewModel extends BaseObservable implements AudioDetailContract.View {

    public static final int INDEX_AUDIO = 0;

    private int mCurrentWindow;
    private long mPlaybackPosition;
    private String mPathAudio;
    private ComponentListener mComponentListener;
    private SimpleExoPlayer mExoPlayer;
    private AudioDetailActivity mActivity;
    private TimelineModel mTimelineModel;
    private Navigator mNavigator;
    private AudioDetailContract.Presenter mPresenter;
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
                    mActivity.startActivity(
                            ProfileUserActivity.getInstance(mActivity, item.getCreatedUser()));
                }
            };

    public AudioDetailViewModel(AudioDetailActivity activity, TimelineModel timelineModel,
            FragmentManager manager, UserModel userModel) {
        mActivity = activity;
        mTimelineModel = timelineModel;
        mNavigator = new Navigator(activity);
        initComponents();
        mManager = manager;
        mFragment = CommentFragment.newInstance(timelineModel.getId(), userModel);
        mTimelineUser = userModel;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            setUpExoPlayer();
        }
    }

    @Override
    public void onResume() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            setUpExoPlayer();
        }
    }

    @Override
    public void onPause() {
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

    private void initComponents() {
        mPathAudio = mTimelineModel.getMedias().get(INDEX_AUDIO).getUrl();
        mComponentListener = new ComponentListener();
    }

    private void setUpExoPlayer() {
        if (mExoPlayer != null) {
            return;
        }
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mActivity),
                new DefaultTrackSelector(), new DefaultLoadControl());
        Uri uriAudio = Uri.parse(mPathAudio);
        mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
        mExoPlayer.prepare(getMediaSource(uriAudio), true, false);
        mExoPlayer.addListener(mComponentListener);
        setExoPlayer(mExoPlayer);
    }

    private MediaSource getMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory(Constant.USER_AGENT),
                new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer() {
        if (mExoPlayer == null) {
            return;
        }
        mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
        mPlaybackPosition = mExoPlayer.getCurrentPosition();
        mExoPlayer.removeListener(mComponentListener);
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onFinishActivity() {
        mNavigator.finishActivity();
    }

    @Bindable
    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }

    public void setExoPlayer(SimpleExoPlayer exoPlayer) {
        mExoPlayer = exoPlayer;
        notifyPropertyChanged(BR.exoPlayer);
    }

    @Override
    public void setPresenter(AudioDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

    public void onClickComment() {
        mFragment.show(mManager, mFragment.getTag());
    }

    /**
     * Listen ExoPlayer
     */
    private class ComponentListener implements ExoPlayer.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            // no ops
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroupArray,
                TrackSelectionArray trackSelectionArray) {
            // no opsListener
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            // no ops
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == STATE_ENDED) {
                mExoPlayer.seekToDefaultPosition();
                mExoPlayer.setPlayWhenReady(false);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            // no ops
        }

        @Override
        public void onPositionDiscontinuity() {
            // no ops
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            // no ops
        }
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
