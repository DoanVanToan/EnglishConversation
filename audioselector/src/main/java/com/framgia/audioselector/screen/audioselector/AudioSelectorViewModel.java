package com.framgia.audioselector.screen.audioselector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.framgia.audioselector.R;
import com.framgia.audioselector.data.model.Audio;
import com.framgia.audioselector.data.source.AudioDataSource;
import com.framgia.audioselector.data.source.AudioRepository;
import com.framgia.audioselector.data.source.callback.OnGetDataCallback;
import com.framgia.audioselector.data.source.local.AudioLocalDataSource;
import com.framgia.audioselector.screen.BaseRecyclerViewAdapter;
import com.framgia.audioselector.screen.BaseViewModel;
import com.framgia.audioselector.util.loader.PlayerLoader;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class AudioSelectorViewModel implements BaseViewModel,
        OnGetDataCallback<List<Audio>>,
        BaseRecyclerViewAdapter.OnItemClickListener<Audio>, ItemAudioViewModel.OnItemCheckChange {

    private static final int LIMIT_CHOOSE_ITEM = 1;

    private int mSelectedCount;
    private AudioSelectorActivity mActivity;
    private AudioSelectorAdapter mAdapter;
    private PlayerLoader mPlayerLoader;

    public AudioSelectorViewModel(AudioSelectorActivity activity) {
        mActivity = activity;
        AudioDataSource.LocalDataSource localDataSource = new AudioLocalDataSource(activity);
        AudioRepository audioRepository = new AudioRepository(localDataSource);
        audioRepository.getLocalAudios(this);
        mPlayerLoader = new PlayerLoader(mActivity);
    }

    @Override
    public void onGetDataSuccess(List<Audio> data) {
        mAdapter = new AudioSelectorAdapter(mActivity, data);
        mAdapter.setListener(this);
        mAdapter.setCheckChange(this);
    }

    @Override
    public void onGetDataFailed() {
        // No ops
    }

    @Override
    public void onStart() {
        if (Util.SDK_INT > Build.VERSION_CODES.M && mAdapter != null) {
            mPlayerLoader.initPlayer();
        }
    }

    @Override
    public void onResume() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M && mAdapter != null) {
            mPlayerLoader.initPlayer();
        }
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            mPlayerLoader.releasePlayer();
        }
    }

    @Override
    public void onStop() {
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            mPlayerLoader.releasePlayer();
        }
    }

    public AudioSelectorAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onItemClick(View view, Audio data, int position) {
        mPlayerLoader.play(Uri.parse(data.getPath()));
    }

    @Override
    public void onCheckChange(Audio audio) {
        if (mSelectedCount >= LIMIT_CHOOSE_ITEM && !audio.isSelected()) {
            Toast.makeText(mActivity,
                    String.format(mActivity.getString(R.string.limit_audio), LIMIT_CHOOSE_ITEM),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        audio.setSelected(!audio.isSelected());
        if (audio.isSelected()) {
            mSelectedCount++;
        } else {
            mSelectedCount--;
        }
        mActivity.onItemAudioClick(mSelectedCount);
    }

    public void clearCheck() {
        mSelectedCount = 0;
        mAdapter.clearCheck();
    }

    public void finishActivity() {
        if (mAdapter.getSelectedAudios() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(AudioSelectorActivity.EXTRA_AUDIO,
                mAdapter.getSelectedAudios());
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }
}
