package com.framgia.videoselector.screen;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.videoselector.R;
import com.framgia.videoselector.data.model.VideoModel;
import com.framgia.videoselector.databinding.ItemVideoSelectorBinding;
import java.util.ArrayList;
import java.util.List;

public class VideoSelectorAdapter extends RecyclerView.Adapter<VideoSelectorAdapter.ViewHolder> {
    private List<VideoModel> mData;
    private VideoSelectorViewModel mListenner;
    private LayoutInflater mLayoutInflater;

    public VideoSelectorAdapter(VideoSelectorViewModel listenner) {
        mListenner = listenner;
        mData = new ArrayList<>();
    }

    public void addData(List<VideoModel> data) {
        if (data == null) {
            return;
        }
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemVideoSelectorBinding binding =
            DataBindingUtil.inflate(mLayoutInflater, R.layout.item_video_selector, parent, false);
        return new ViewHolder(binding);
    }

    public List<VideoModel> getSelectedItem() {
        if (mData == null || mData.isEmpty()) {
            return null;
        }
        List<VideoModel> result = new ArrayList<>();
        for (VideoModel videoModel : mData) {
            if (videoModel.isSelected()) {
                result.add(videoModel);
            }
        }
        return result;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position), mListenner);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void clearSelectedItem() {
        for (VideoModel model : mData) {
            model.setSelected(false);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemVideoSelectorBinding mBinding;

        public ViewHolder(ItemVideoSelectorBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindData(VideoModel videoModel, VideoSelectorViewModel listenner) {
            if (videoModel == null) {
                return;
            }
            mBinding.setVideo(videoModel);
            mBinding.setViewModel(listenner);
        }
    }
}
