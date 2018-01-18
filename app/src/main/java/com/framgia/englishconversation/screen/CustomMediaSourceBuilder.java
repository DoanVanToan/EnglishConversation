package com.framgia.englishconversation.screen;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.framgia.englishconversation.utils.Constant;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import im.ene.toro.exoplayer.MediaSourceBuilder;

/**
 * Created by fs-sournary.
 * Data: 1/18/18.
 * Description:
 */

public class CustomMediaSourceBuilder extends MediaSourceBuilder {

    private Context mContext;
    private String mExtension;
    private Handler mHandler;
    private Uri mMediaUri;

    public CustomMediaSourceBuilder(Context context, Uri mediaUri, String extension) {
        this(context, mediaUri, extension, new Handler());
    }

    public CustomMediaSourceBuilder(Context context, Uri mediaUri) {
        this(context, mediaUri, null);
    }

    public CustomMediaSourceBuilder(Context context, Uri mediaUri,
                                    String extension, Handler handler) {
        super(context, mediaUri, extension, handler);
        mContext = context;
        mExtension = extension;
        mHandler = handler;
        mMediaUri = mediaUri;
    }

    public MediaSource build() {
        return this.build(new DefaultBandwidthMeter(mHandler, null));
    }

    @Override
    @SuppressWarnings("SameParameterValue")
    public MediaSource build(BandwidthMeter bandwidthMeter) {
        TransferListener<? super DataSource> transferListener;
        try {
            //noinspection unchecked
            transferListener = bandwidthMeter != null
                    ? (TransferListener<? super DataSource>) bandwidthMeter : null;
        } catch (ClassCastException er) {
            throw new IllegalArgumentException("BandwidthMeter must implement TransferListener.");
        }
        DataSource.Factory mediaDataSourceFactory = buildDataSourceFactory(transferListener);
        int type = Util.inferContentType(!TextUtils.isEmpty(mExtension)
                ? "." + mExtension : mMediaUri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(mMediaUri, buildDataSourceFactory(transferListener),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mHandler, null);
            case C.TYPE_DASH:
                return new DashMediaSource(mMediaUri, buildDataSourceFactory(transferListener),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mHandler, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(mMediaUri, mediaDataSourceFactory, mHandler, null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(mMediaUri, mediaDataSourceFactory,
                        new DefaultExtractorsFactory(), mHandler, null);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private DataSource.Factory buildDataSourceFactory(
            TransferListener<? super DataSource> transferListener) {
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(
                Constant.USER_AGENT,
                transferListener,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );
        return new DefaultDataSourceFactory(
                mContext,
                transferListener,
                httpDataSourceFactory);
    }
}
