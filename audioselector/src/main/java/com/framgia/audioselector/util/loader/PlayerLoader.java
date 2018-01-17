package com.framgia.audioselector.util.loader;

import android.content.Context;
import android.net.Uri;

import com.framgia.audioselector.util.Constant;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class PlayerLoader {

    private static final long DEFAULT_PLAYER_POSITION = 0;

    private Context mContext;
    private SimpleExoPlayer mPlayer;

    public PlayerLoader(Context context) {
        mContext = context;
    }

    public void releasePlayer() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.release();
        mPlayer = null;
    }

    public void initPlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
    }

    public void play(Uri uri) {
        mPlayer.seekTo(DEFAULT_PLAYER_POSITION);
        mPlayer.prepare(getMediaSource(uri), true, false);
        mPlayer.setPlayWhenReady(true);
    }

    private MediaSource getMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, new DefaultDataSourceFactory(mContext, Constant.USER),
                new DefaultExtractorsFactory(), null, null);
    }
}
