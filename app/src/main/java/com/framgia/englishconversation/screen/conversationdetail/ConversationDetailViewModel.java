package com.framgia.englishconversation.screen.conversationdetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.ConversationModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.comment.CommentFragment;
import com.framgia.englishconversation.screen.profileuser.ProfileUserActivity;
import com.framgia.englishconversation.screen.timeline.OnTimelineItemTouchListener;
import com.framgia.englishconversation.utils.Constant;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 12/27/17.
 * Description:
 */

public class ConversationDetailViewModel extends BaseObservable
        implements ConversationDetailContract.ViewModel {

    private static final int DEF_WINDOW_INDEX = 0;
    private static final int DEF_SCROLL_POSITION = 0;

    private int mScrollPosition;
    private boolean mIsPlaying;
    private int mSelectedItemPosition;
    private long mPlaybackPosition;
    private TimelineModel mTimelineModel;
    private SimpleExoPlayer mExoPlayer;
    private List<ConversationModel> mDetailModels;
    private PlayerListener mPlayerListener;
    private ConversationDetailActivity mDetailActivity;
    private ConversationDetailAdapter mAdapter;
    private ConversationDetailContract.Presenter mPresenter;
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
                    mDetailActivity.startActivity(ProfileUserActivity.getInstance(mDetailActivity,
                            item.getCreatedUser()));
                }

                @Override
                public boolean onItemLongClick(View viewGroup, TimelineModel item) {
                    return false;
                }

                @Override
                public void onItemOptionClick(TimelineModel item) {

                }

            };

    public ConversationDetailViewModel(ConversationDetailActivity detailActivity,
                                       FragmentManager manager,
                                       TimelineModel timelineModel, UserModel userModel) {
        mDetailActivity = detailActivity;
        mTimelineModel = timelineModel;
        mAdapter = new ConversationDetailAdapter(mDetailActivity, timelineModel.getConversations(),
                this);
        mPlayerListener = new PlayerListener();
        initDefaultData(timelineModel);
        mManager = manager;
        mFragment = CommentFragment.newInstance(timelineModel.getId(), userModel);
        mTimelineUser = userModel;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        if (Util.SDK_INT >= Build.VERSION_CODES.M) {
            initPlayer();
            setPlaying(false);
        }
    }

    @Override
    public void onResume() {
        if (Util.SDK_INT < Build.VERSION_CODES.M) {
            initPlayer();
            setPlaying(false);
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
    public void onPause() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void setPresenter(ConversationDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void initDefaultData(TimelineModel timelineModel) {
        int index = 0;
        mDetailModels = new ArrayList<>();
        for (ConversationModel conversationModel : timelineModel.getConversations()) {
            if (conversationModel.getMediaModel() != null) {
                conversationModel.setIndex(index);
                mDetailModels.add(conversationModel);
                index++;
            }
        }
        setScrollPosition(DEF_SCROLL_POSITION);
    }

    private void initPlayer() {
        mExoPlayer =
                ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mDetailActivity),
                        new DefaultTrackSelector(), new DefaultLoadControl());
        List<Uri> uris = new ArrayList<>();
        for (int i = 0; i < mDetailModels.size(); i++) {
            String url = mDetailModels.get(i).getMediaModel().getUrl();
            uris.add(Uri.parse(url));
        }
        mExoPlayer.prepare(getMediaSource(uris), true, false);
        mExoPlayer.addListener(mPlayerListener);
    }

    public void onMediaEntryClick() {
        if (!mIsPlaying) {
            play();
        } else {
            pause();
        }
        setPlaying(!mIsPlaying);
    }

    public void onMediaItemClick(ConversationModel conversationModel) {
        setPlaying(true);
        mExoPlayer.seekToDefaultPosition(conversationModel.getIndex());
        mExoPlayer.setPlayWhenReady(true);
    }

    private void play() {
        mExoPlayer.seekTo(mSelectedItemPosition, mPlaybackPosition);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void pause() {
        mExoPlayer.setPlayWhenReady(false);
        mSelectedItemPosition = mExoPlayer.getCurrentWindowIndex();
        mPlaybackPosition = mExoPlayer.getCurrentPosition();
    }

    private void releasePlayer() {
        if (mExoPlayer == null) {
            return;
        }
        mPlaybackPosition = mExoPlayer.getCurrentPosition();
        mSelectedItemPosition = mExoPlayer.getCurrentWindowIndex();
        mExoPlayer.removeListener(mPlayerListener);
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private MediaSource getMediaSource(List<Uri> uris) {
        DefaultHttpDataSourceFactory httpDataSourceFactory =
                new DefaultHttpDataSourceFactory(Constant.USER_AGENT);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        List<MediaSource> mediaSources = new ArrayList<>();
        for (Uri uri : uris) {
            mediaSources.add(
                    new ExtractorMediaSource(uri, httpDataSourceFactory, extractorsFactory, null,
                            null));
        }
        MediaSource results[] = new MediaSource[mediaSources.size()];
        return new ConcatenatingMediaSource(mediaSources.toArray(results));
    }

    @Bindable
    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void setPlaying(boolean playing) {
        mIsPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    @Bindable
    public int getScrollPosition() {
        return mScrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        mScrollPosition = scrollPosition;
        notifyPropertyChanged(BR.scrollPosition);
    }

    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

    public ConversationDetailAdapter getAdapter() {
        return mAdapter;
    }

    public List<ConversationModel> getDetailModels() {
        return mDetailModels;
    }

    public void onClickComment() {
        mFragment.show(mManager, mFragment.getTag());
    }

    /**
     * Event for ExoPlayer
     */
    private class PlayerListener implements ExoPlayer.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object o) {
            // No ops
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroupArray,
                                    TrackSelectionArray trackSelectionArray) {
            // No ops
        }

        @Override
        public void onLoadingChanged(boolean b) {
            // No ops
        }

        @Override
        public void onPlayerStateChanged(boolean b, int i) {
            switch (i) {
                case ExoPlayer.STATE_ENDED:
                    mExoPlayer.seekToDefaultPosition(DEF_WINDOW_INDEX);
                    mExoPlayer.setPlayWhenReady(false);
                    setPlaying(false);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            // No ops
        }

        @Override
        public void onPositionDiscontinuity() {
            int position = mTimelineModel.getConversations()
                    .indexOf(mDetailModels.get(mExoPlayer.getCurrentWindowIndex()));
            setScrollPosition(position);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            // No ops
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

    public UserModel getTimelineUser() {
        return mTimelineUser;
    }
}
