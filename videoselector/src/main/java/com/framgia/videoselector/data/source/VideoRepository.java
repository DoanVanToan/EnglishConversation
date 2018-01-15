package com.framgia.videoselector.data.source;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.framgia.videoselector.data.model.VideoModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by doan.van.toan on 1/15/18.
 */

public class VideoRepository implements VideoDataSource {

    private VideoDataSource mLocalDataSource;

    public VideoRepository(VideoDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public void getVideos(Callback<List<VideoModel>> callback) {
        mLocalDataSource.getVideos(callback);
    }
}
