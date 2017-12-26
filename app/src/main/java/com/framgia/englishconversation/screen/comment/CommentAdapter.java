package com.framgia.englishconversation.screen.comment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.databinding.ItemCommentAudioBinding;
import com.framgia.englishconversation.databinding.ItemCommentImageBinding;
import com.framgia.englishconversation.databinding.ItemCommentOnlyTextBinding;
import com.framgia.englishconversation.databinding.ItemCommentVideoBinding;
import java.util.List;

/**
 * Created by anh on 12/21/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.BaseCommentViewHolder> {
    private List<Comment> mComments;

    public CommentAdapter(List<Comment> comments) {
        mComments = comments;
    }

    public void updateData(List<Comment> comments) {
        if (comments == null) {
            return;
        }
        mComments.addAll(comments);
        notifyDataSetChanged();
    }

    public void updateData(Comment comment) {
        mComments.add(0, comment);
        notifyItemInserted(0);
    }

    @Override
    public int getItemViewType(int position) {
        return mComments.get(position).getCommentType();
    }

    @Override
    public CommentAdapter.BaseCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.ONLY_TEXT:
                ItemCommentOnlyTextBinding onlyTextBinding =
                        ItemCommentOnlyTextBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new OnlyTextViewHolder(onlyTextBinding);
            case MediaModel.MediaType.AUDIO:
                ItemCommentAudioBinding audioBinding =
                        ItemCommentAudioBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new AudioViewHolder(audioBinding);
            case MediaModel.MediaType.VIDEO:
                ItemCommentVideoBinding videoBinding =
                        ItemCommentVideoBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new VideoViewHolder(videoBinding);
            case MediaModel.MediaType.IMAGE:
                ItemCommentImageBinding imageBinding =
                        ItemCommentImageBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new ImageViewHolder(imageBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseCommentViewHolder holder, int position) {
        holder.bindData(mComments.get(position));
    }

    @Override
    public int getItemCount() {
        return mComments != null ? mComments.size() : 0;
    }

    /**
     * Display comment model with only text (without media)
     */
    public class OnlyTextViewHolder extends BaseCommentViewHolder {
        private ItemCommentOnlyTextBinding mBinding;

        public OnlyTextViewHolder(ItemCommentOnlyTextBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display comment model with audio media
     */
    public class AudioViewHolder extends BaseCommentViewHolder {
        private ItemCommentAudioBinding mBinding;

        public AudioViewHolder(ItemCommentAudioBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display comment model with image media
     */
    public class ImageViewHolder extends BaseCommentViewHolder {
        private ItemCommentImageBinding mBinding;

        public ImageViewHolder(ItemCommentImageBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display comment model with video media
     */
    public class VideoViewHolder extends BaseCommentViewHolder {
        private ItemCommentVideoBinding mBinding;

        VideoViewHolder(ItemCommentVideoBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Base timeline model
     */
    public abstract class BaseCommentViewHolder extends RecyclerView.ViewHolder {

        public BaseCommentViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(Comment model);
    }
}
