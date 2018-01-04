package com.framgia.englishconversation.screen.conversationdetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Build;

import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.ConversationModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.utils.Constant;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
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

    private static final int IDLE_SCROLL_POSITION = -1;
    private static final int DEFAULT_SCROLL_POSITION = 0;
    private static final int NUMBER_INCREASE_SCROLL_POSITION = 1;
    private static final int NUMBER_DECREASE_SCROLL_POSITION = 1;
    private static final long DEFAULT_PLAYBACK_POSITION = 0;

    private int mCurrentPosition;
    private int mScrollPosition;
    private boolean mIsPlaying;
    private int mSelectedIndex;
    private long mPlaybackPosition;
    private ComponentListener mComponentListener;
    private SimpleExoPlayer mExoPlayer;
    private ConversationDetailAdapter mAdapter;
    private ConversationDetailContract.Presenter mPresenter;
    private ConversationDetailActivity mActivity;
    private TimelineModel mTimelineModel;

    public ConversationDetailViewModel(ConversationDetailActivity activitiy,
                                       TimelineModel timelineModel) {
        mActivity = activitiy;
        mTimelineModel = timelineModel;
        mAdapter = new ConversationDetailAdapter(
                activitiy, timelineModel.getConversations(), this);
        mComponentListener = new ComponentListener();
        mScrollPosition = IDLE_SCROLL_POSITION;
        setPlaying(false);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releaseExoPlayer();
            setPlaying(false);
        }
    }

    @Override
    public void onResume() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releaseExoPlayer();
            setPlaying(false);
        }
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releaseExoPlayer();
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releaseExoPlayer();
        }
    }

    @Override
    public void setPresenter(ConversationDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public boolean isExitMedia() {
        for (ConversationModel conversationModel : mTimelineModel.getConversations()) {
            if (conversationModel.getMediaModel() != null) {
                return true;
            }
        }
        return false;
    }

    private void initializeExoPlayer(int index) {
        if (mExoPlayer != null
                || mTimelineModel.getConversations().get(index).getMediaModel() == null) {
            return;
        }
        mSelectedIndex = index;
        mScrollPosition = index - NUMBER_DECREASE_SCROLL_POSITION;
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mActivity),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mExoPlayer.seekTo(DEFAULT_PLAYBACK_POSITION);
        List<ConversationModel> conversationModels = mTimelineModel.getConversations()
                .subList(index, mTimelineModel.getConversations().size());
        List<Uri> uris = new ArrayList<>();
        for (ConversationModel conversationModel : conversationModels) {
            if (conversationModel.getMediaModel() != null) {
                String url = conversationModel.getMediaModel().getUrl();
                uris.add(Uri.parse(url));
            }
        }
        MediaSource mediaSource = getMediaSources(uris);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.prepare(mediaSource, true, false);
        mExoPlayer.addListener(mComponentListener);
        setPlaying(true);
    }

    private void initializeExoPlayer() {
        if (mExoPlayer != null) {
            return;
        }
        mScrollPosition = mSelectedIndex - NUMBER_DECREASE_SCROLL_POSITION;
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mActivity),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        List<ConversationModel> conversationModels = mTimelineModel.getConversations()
                .subList(mSelectedIndex, mTimelineModel.getConversations().size());
        mExoPlayer.seekTo(mPlaybackPosition);
        List<Uri> uris = new ArrayList<>();
        for (ConversationModel conversationModel : conversationModels) {
            if (conversationModel.getMediaModel() != null) {
                String url = conversationModel.getMediaModel().getUrl();
                uris.add(Uri.parse(url));
            }
        }
        MediaSource mediaSource = getMediaSources(uris);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.prepare(mediaSource, true, false);
        mExoPlayer.addListener(mComponentListener);
    }

    private MediaSource getMediaSources(List<Uri> uris) {
        DefaultHttpDataSourceFactory defaultHttpDataSourceFactory =
                new DefaultHttpDataSourceFactory(Constant.USER_AGENT);
        DefaultExtractorsFactory defaultExtractorsFactory =
                new DefaultExtractorsFactory();
        List<MediaSource> mediaSources = new ArrayList<>();
        for (Uri uri : uris) {
            MediaSource mediaSource = new ExtractorMediaSource(uri,
                    defaultHttpDataSourceFactory, defaultExtractorsFactory,
                    null, null);
            mediaSources.add(mediaSource);
        }
        MediaSource[] mediaSourcesResults = new MediaSource[mediaSources.size()];
        return new ConcatenatingMediaSource(mediaSources.toArray(mediaSourcesResults));
    }

    private void releaseExoPlayer() {
        if (mExoPlayer == null) {
            return;
        }
        mSelectedIndex = mScrollPosition;
        mPlaybackPosition = mExoPlayer.getCurrentPosition();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    public void onAudioConversationClick(int index) {
        if (mIsPlaying) {
            setPlaying(false);
        }
        releaseExoPlayer();
        initializeExoPlayer(index);
    }

    public void onControllerConversationClick() {
        if (mIsPlaying) {
            releaseExoPlayer();
        } else {
            initializeExoPlayer();
        }
        setPlaying(!mIsPlaying);
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
    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
        notifyPropertyChanged(BR.currentPosition);
    }

    public ConversationDetailAdapter getAdapter() {
        return mAdapter;
    }

    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

    /**
     * Event for ExoPlayer variable
     */
    private class ComponentListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            // no ops
        }

        /**
         * Define: conversation audio list = CAL
         * In this class I used ConcatenatingMediaSource which concat all the audio (with uri)
         * When CAL is played, onTracksChanged method will be invoked.
         * <p>
         * In this case, trackSelectionArray = CAL
         * trackGroupArray = 1 (style = audio)
         *
         * @param trackGroupArray:     include number of render which is able to render tracks of single
         *                             type (ex: audio, video ...)
         * @param trackSelectionArray: include tracks is selected and rendered by each renderer
         *                             of trackGroupArray
         */
        @Override
        public void onTracksChanged(TrackGroupArray trackGroupArray,
                                    TrackSelectionArray trackSelectionArray) {
            // Reset to init position
            // Timeline includes a lot of Periods that I concat them into a timeline.
            int numberConversation = mTimelineModel.getConversations().size();
            mScrollPosition += NUMBER_INCREASE_SCROLL_POSITION;
            for (int i = 0; i < numberConversation; i++) {
                if (i == mScrollPosition
                        && mTimelineModel.getConversations().get(i).getMediaModel() == null) {
                    mScrollPosition += NUMBER_INCREASE_SCROLL_POSITION;
                }
            }
            setCurrentPosition(mScrollPosition);
            if (mScrollPosition == numberConversation - 1) {
                mScrollPosition = DEFAULT_SCROLL_POSITION;
            }
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            // no ops
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                mExoPlayer.seekTo(DEFAULT_PLAYBACK_POSITION);
                mExoPlayer.setPlayWhenReady(false);
                releaseExoPlayer();
                setPlaying(false);
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            // no ops
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean b) {
            // no ops
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            // no ops
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            // no ops
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            // no ops
        }

        @Override
        public void onSeekProcessed() {
            // no ops
        }
    }

}
