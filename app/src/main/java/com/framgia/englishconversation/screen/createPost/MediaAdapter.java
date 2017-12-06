package com.framgia.englishconversation.screen.createPost;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.databinding.ItemAudioBinding;
import com.framgia.englishconversation.databinding.ItemImageBinding;
import com.framgia.englishconversation.databinding.ItemVideoBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the CreatePost screen.
 */

public class MediaAdapter extends RecyclerView.Adapter {
    private List<MediaModel> mMediaModels;
    private CreatePostViewModel mViewModel;

    public MediaAdapter(CreatePostViewModel viewModel) {
        mViewModel = viewModel;
        mMediaModels = new ArrayList<>();
    }

    public void setViewModel(CreatePostViewModel viewModel) {
        mViewModel = viewModel;
    }

    public void updateData(List<MediaModel> mediaModels) {
        if (mediaModels == null) {
            return;
        }
        mMediaModels.addAll(mediaModels);
        notifyDataSetChanged();
    }

    public void setData(List<MediaModel> mediaModels) {
        mMediaModels.clear();
        updateData(mediaModels);
    }

    public void removeItem(MediaModel mediaModel) {
        mMediaModels.remove(mediaModel);
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        return mMediaModels.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.IMAGE:
                ItemImageBinding imageBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_image,
                        parent,
                        false);
                return new ImageViewHolder(imageBinding);
            case MediaModel.MediaType.AUDIO:
                ItemAudioBinding audioBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_audio,
                        parent,
                        false);
                return new AudioViewHolder(audioBinding);
       case MediaModel.MediaType.VIDEO:
                ItemVideoBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.item_video,
                        parent,
                        false);
                return new VideoViewHolder(binding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case MediaModel.MediaType.IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.bindData(mMediaModels.get(position));
                break;
            case MediaModel.MediaType.AUDIO:
                AudioViewHolder audioViewHolder = (AudioViewHolder) holder;
                audioViewHolder.bindData(mMediaModels.get(position));
                break;
            case MediaModel.MediaType.VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                videoViewHolder.bindData(mMediaModels.get(position));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMediaModels != null ? mMediaModels.size() : 0;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private ItemVideoBinding mVideoBinding;

        public VideoViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            mVideoBinding = binding;
        }

        public void bindData(MediaModel video) {
            mVideoBinding.setMediaModel(video);
            mVideoBinding.setViewModel(mViewModel);
            mVideoBinding.executePendingBindings();
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ItemImageBinding mImageBinding;

        public ImageViewHolder(ItemImageBinding binding) {
            super(binding.getRoot());
            mImageBinding = binding;
        }

        public void bindData(MediaModel video) {
            mImageBinding.setMediaModel(video);
            mImageBinding.setViewModel(mViewModel);
            mImageBinding.executePendingBindings();
        }
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder {
        private ItemAudioBinding mImageBinding;

        public AudioViewHolder(ItemAudioBinding binding) {
            super(binding.getRoot());
            mImageBinding = binding;
        }

        public void bindData(MediaModel video) {
            mImageBinding.setRecord(video);
            mImageBinding.setViewModel(mViewModel);
            mImageBinding.executePendingBindings();
        }
    }

}
