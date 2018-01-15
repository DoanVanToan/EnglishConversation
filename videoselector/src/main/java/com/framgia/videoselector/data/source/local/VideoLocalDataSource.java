package com.framgia.videoselector.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.framgia.videoselector.data.model.VideoModel;
import com.framgia.videoselector.data.source.VideoDataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by doan.van.toan on 1/15/18.
 */

public class VideoLocalDataSource implements VideoDataSource {

    private static final String SORT_VALUE = "DESC";
    private Context mContext;

    public VideoLocalDataSource(Context context) {
        mContext = context;
    }

    @Override
    public void getVideos(Callback<List<VideoModel>> callback) {
        callback.onStart();
        List<VideoModel> videos = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String sortOder = MediaStore.Video.Media.DATE_TAKEN + " " + SORT_VALUE;
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, sortOder, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                videos.add(new VideoModel(cursor));
            }
            cursor.close();
        }
        callback.onSuccess(videos);
        callback.onComplete();
    }
}
