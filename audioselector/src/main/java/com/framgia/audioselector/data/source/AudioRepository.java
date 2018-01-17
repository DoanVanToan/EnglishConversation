package com.framgia.audioselector.data.source;

import com.framgia.audioselector.data.model.Audio;
import com.framgia.audioselector.data.source.callback.OnGetDataCallback;

import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class AudioRepository {

    private AudioDataSource.LocalDataSource mLocalDataSource;

    public AudioRepository(AudioDataSource.LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    public void getLocalAudios(OnGetDataCallback<List<Audio>> callback) {
        mLocalDataSource.getAudios(callback);
    }
}
