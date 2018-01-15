package com.framgia.videoselector.data.source;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.telecom.Call;
import com.framgia.videoselector.data.model.VideoModel;
import java.util.List;

/**
 * Created by doan.van.toan on 1/15/18.
 */

public interface VideoDataSource {
    interface Callback<T> {
        void onStart();

        void onSuccess(T data);

        void onFailure(String message);

        void onComplete();
    }

    void getVideos(Callback<List<VideoModel>> callback);
}
