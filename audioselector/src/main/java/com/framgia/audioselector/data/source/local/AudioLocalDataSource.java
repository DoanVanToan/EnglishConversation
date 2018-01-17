package com.framgia.audioselector.data.source.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.framgia.audioselector.data.model.Audio;
import com.framgia.audioselector.data.source.AudioDataSource;
import com.framgia.audioselector.data.source.callback.OnGetDataCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class AudioLocalDataSource implements AudioDataSource.LocalDataSource {

    private static final String SORT_AUDIO = " DESC";

    private Context mContext;

    public AudioLocalDataSource(Context context) {
        mContext = context;
    }

    @Override
    public void getAudios(OnGetDataCallback<List<Audio>> callback) {
        List<Audio> audios = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + SORT_AUDIO;
        Cursor cursor = contentResolver.query(uri, null,
                null, null, sortOrder);
        if (cursor == null || cursor.getCount() == 0) {
            callback.onGetDataFailed();
            return;
        }
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            audios.add(new Audio(id, title, data, false));
        }
        cursor.close();
        callback.onGetDataSuccess(audios);
    }
}
