package com.framgia.audioselector.screen.audioselector;

import android.databinding.BaseObservable;
import android.view.View;

import com.framgia.audioselector.data.model.Audio;
import com.framgia.audioselector.BaseRecyclerViewAdapter;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class ItemAudioViewModel extends BaseObservable {

    private Audio mAudio;
    private BaseRecyclerViewAdapter.OnItemClickListener<Audio> mListener;
    private OnItemCheckChange mCheckChange;

    public ItemAudioViewModel(Audio audio,
                              BaseRecyclerViewAdapter.OnItemClickListener<Audio> listener,
                              OnItemCheckChange checkChange) {
        mAudio = audio;
        mListener = listener;
        mCheckChange = checkChange;
    }

    public Audio getAudio() {
        return mAudio;
    }

    public void onItemClick(View view, Audio audio, int position) {
        mListener.onItemClick(view, audio, position);
    }

    public void onCheckClick(Audio audio) {
        mCheckChange.onCheckChange(audio);
    }


    public interface OnItemCheckChange {
        void onCheckChange(Audio audio);
    }
}
