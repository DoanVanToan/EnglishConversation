package com.framgia.englishconversation.screen.createcomment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.databinding.ItemAudioCreateCommentBinding;
import com.framgia.englishconversation.databinding.ItemImageCreateCommentBinding;
import com.framgia.englishconversation.databinding.ItemVideoCreateCommentBinding;
import java.util.ArrayList;
import java.util.List;

/**
 * Exposes the data to be used in the CreatePost screen.
 */

public class MultimediaAdapter extends RecyclerView.Adapter<MultimediaAdapter.BaseViewHolder> {

    private List<MediaModel> mMediaModels;
    private CreateCommentViewModel mViewModel;

    MultimediaAdapter(CreateCommentViewModel viewModel) {
        mViewModel = viewModel;
        mMediaModels = new ArrayList<>();
    }

    public void setViewModel(CreateCommentViewModel viewModel) {
        mViewModel = viewModel;
    }

    private void updateData(List<MediaModel> mediaModels) {
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

    void removeItem(MediaModel mediaModel) {
        mMediaModels.remove(mediaModel);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mMediaModels.get(position).getType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.IMAGE:
                ItemImageCreateCommentBinding imageBinding =
                        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_image_create_comment, parent, false);
                return new ImageViewHolder(imageBinding);
            case MediaModel.MediaType.AUDIO:
                ItemAudioCreateCommentBinding audioBinding =
                        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_audio_create_comment, parent, false);
                return new AudioViewHolder(audioBinding);
            case MediaModel.MediaType.VIDEO:
                ItemVideoCreateCommentBinding binding =
                        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                R.layout.item_video_create_comment, parent, false);
                return new VideoViewHolder(binding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(mMediaModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mMediaModels != null ? mMediaModels.size() : 0;
    }

    /**
     * Viewholder to display video
     */
    public class VideoViewHolder extends BaseViewHolder {
        private ItemVideoCreateCommentBinding mVideoBinding;

        VideoViewHolder(ItemVideoCreateCommentBinding binding) {
            super(binding.getRoot());
            mVideoBinding = binding;
        }

        @Override
        public void bindData(MediaModel video) {
            mVideoBinding.setRecord(video);
            mVideoBinding.setViewModel(mViewModel);
            mVideoBinding.executePendingBindings();
        }
    }

    /**
     * Viewholder to display image
     */
    public class ImageViewHolder extends BaseViewHolder {
        private ItemImageCreateCommentBinding mImageBinding;

        ImageViewHolder(ItemImageCreateCommentBinding binding) {
            super(binding.getRoot());
            mImageBinding = binding;
        }

        @Override
        public void bindData(MediaModel video) {
            mImageBinding.setMediaModel(video);
            mImageBinding.setViewModel(mViewModel);
            mImageBinding.executePendingBindings();
        }
    }

    /**
     * Viewholder to display audio
     */
    public class AudioViewHolder extends BaseViewHolder {
        private ItemAudioCreateCommentBinding mAudioBinding;

        AudioViewHolder(ItemAudioCreateCommentBinding binding) {
            super(binding.getRoot());
            mAudioBinding = binding;
        }

        @Override
        public void bindData(MediaModel audio) {
            mAudioBinding.setRecord(audio);
            mAudioBinding.setViewModel(mViewModel);
            mAudioBinding.executePendingBindings();
        }
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(MediaModel video);
    }
}
