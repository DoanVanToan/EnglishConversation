package com.framgia.audioselector.screen.audioselector;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.audioselector.R;
import com.framgia.audioselector.data.model.Audio;
import com.framgia.audioselector.databinding.ItemAudioSelectorBinding;
import com.framgia.audioselector.screen.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class AudioSelectorAdapter extends
        BaseRecyclerViewAdapter<AudioSelectorAdapter.AudioSelectorViewHolder> {

    private List<Audio> mAudios;
    private BaseRecyclerViewAdapter.OnItemClickListener<Audio> mListener;
    private ItemAudioViewModel.OnItemCheckChange mCheckChange;
    private Context mContext;

    public AudioSelectorAdapter(Context context, List<Audio> audios) {
        super(context);
        mContext = context;
        mAudios = audios;
    }

    @Override
    public AudioSelectorAdapter.AudioSelectorViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        ItemAudioSelectorBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_audio_selector, parent, false);
        return new AudioSelectorViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(AudioSelectorAdapter.AudioSelectorViewHolder holder, int position) {
        holder.bindView(mAudios.get(position));
    }

    @Override
    public int getItemCount() {
        return mAudios == null ? 0 : mAudios.size();
    }

    public void setListener(OnItemClickListener<Audio> listener) {
        mListener = listener;
    }

    public void setCheckChange(ItemAudioViewModel.OnItemCheckChange checkChange) {
        mCheckChange = checkChange;
    }

    public void clearCheck() {
        for (Audio audio : mAudios) {
            if (audio.isSelected()) {
                audio.setSelected(false);
            }
        }
    }

    public ArrayList<Audio> getSelectedAudios() {
        ArrayList<Audio> results = new ArrayList<>();
        for (Audio audio : mAudios) {
            if (audio.isSelected()) {
                results.add(audio);
            }
        }
        return results;
    }

    class AudioSelectorViewHolder extends RecyclerView.ViewHolder {

        private ItemAudioSelectorBinding mBinding;

        AudioSelectorViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        void bindView(Audio audio) {
            mBinding.setViewModel(new ItemAudioViewModel(audio, mListener, mCheckChange));
            mBinding.setPosition(getAdapterPosition());
            mBinding.setAudio(audio);
            mBinding.executePendingBindings();
        }
    }
}
