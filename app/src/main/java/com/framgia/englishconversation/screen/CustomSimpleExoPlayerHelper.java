package com.framgia.englishconversation.screen;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.concurrent.atomic.AtomicInteger;

import im.ene.toro.ToroPlayer;
import im.ene.toro.exoplayer.ExoPlayerHelper;
import im.ene.toro.helper.ToroPlayerHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

/**
 * Created by fs-sournary.
 * Data: 1/18/18.
 * Description:
 */

public final class CustomSimpleExoPlayerHelper extends ToroPlayerHelper {

    private final ExoPlayerHelper.EventListener internalListener =
            new ExoPlayerHelper.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    CustomSimpleExoPlayerHelper.super.onPlayerStateUpdated(
                            playWhenReady, playbackState);
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                }
            };

    private final AtomicInteger counter = new AtomicInteger(0); // initialize count
    private final ExoPlayerHelper helper;
    private final CustomMediaSourceBuilder mediaSourceBuilder;

    public CustomSimpleExoPlayerHelper(Container container, ToroPlayer player, Uri mediaUri) {
        super(container, player);
        if (!(player.getPlayerView() instanceof SimpleExoPlayerView)) {
            throw new IllegalArgumentException("Only SimpleExoPlayerView is supported.");
        }
        this.helper = new ExoPlayerHelper((SimpleExoPlayerView) player.getPlayerView());
        this.mediaSourceBuilder = new CustomMediaSourceBuilder(container.getContext(), mediaUri);
    }

    /**
     * {@inheritDoc}
     *
     * @param playbackInfo the initial playback info. {@code null} if no such info available.
     */
    @Override
    public void initialize(@Nullable PlaybackInfo playbackInfo) {
        if (counter.getAndIncrement() == 0) { // prevent the multiple time init
            this.helper.addEventListener(internalListener);
            try {
                this.helper.prepare(this.mediaSourceBuilder);
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }

        this.helper.setPlaybackInfo(playbackInfo);
    }

    @Override
    public void play() {
        this.helper.play();
    }

    @Override
    public void pause() {
        this.helper.pause();
    }

    @Override
    public boolean isPlaying() {
        return this.helper.isPlaying();
    }

    @NonNull
    @Override
    public PlaybackInfo getLatestPlaybackInfo() {
        return this.helper.getPlaybackInfo();
    }

    @Override
    public void release() {
        counter.set(0); // reset
        this.helper.removeEventListener(internalListener);
        this.helper.release();
        super.release();
    }
}
