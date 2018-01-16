package com.framgia.englishconversation.screen.comment;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.databinding.ItemCommentAudioBinding;
import com.framgia.englishconversation.databinding.ItemCommentImageBinding;
import com.framgia.englishconversation.databinding.ItemCommentOnlyTextBinding;
import com.framgia.englishconversation.databinding.ItemCommentVideoBinding;
import com.framgia.englishconversation.screen.BaseMediaViewHolder;
import com.framgia.englishconversation.screen.BaseViewHolder;
import com.framgia.englishconversation.screen.timeline.OnTimelineItemTouchListener;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import java.util.List;

/**
 * Created by anh on 12/21/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Comment> mComments;
    private OnTimelineItemTouchListener<Comment> mTouchListener;

    public CommentAdapter(List<Comment> comments,
            OnTimelineItemTouchListener<Comment> touchListener) {
        mComments = comments;
        mTouchListener = touchListener;
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

    public void updateDataForward(Comment comment) {
        if (comment == null) {
            return;
        }
        mComments.add(0, comment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mComments.get(position).getCommentType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.ONLY_TEXT:
                ItemCommentOnlyTextBinding onlyTextBinding =
                        ItemCommentOnlyTextBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new OnlyTextViewHolder(onlyTextBinding, mTouchListener);
            case MediaModel.MediaType.AUDIO:
                ItemCommentAudioBinding audioBinding =
                        ItemCommentAudioBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);

                return new AudioViewHolder(audioBinding, mTouchListener);
            case MediaModel.MediaType.VIDEO:
                ItemCommentVideoBinding videoBinding =
                        ItemCommentVideoBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new VideoViewHolder(videoBinding, mTouchListener);
            case MediaModel.MediaType.IMAGE:
                ItemCommentImageBinding imageBinding =
                        ItemCommentImageBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new ImageViewHolder(imageBinding, mTouchListener);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(mComments.get(position));
    }

    @Override
    public int getItemCount() {
        return mComments != null ? mComments.size() : 0;
    }

    public Comment getLastComment() {
        return mComments != null && !mComments.isEmpty() ? mComments.get(mComments.size() - 1)
                : null;
    }

    /**
     * Display comment model with only text (without media)
     */
    public class OnlyTextViewHolder extends BaseViewHolder<Comment> {
        private ItemCommentOnlyTextBinding mBinding;
        private OnTimelineItemTouchListener<Comment> mTouchListener;

        public OnlyTextViewHolder(ItemCommentOnlyTextBinding itemView,
                OnTimelineItemTouchListener<Comment> touchListener) {
            super(itemView.getRoot());
            mBinding = itemView;
            mTouchListener = touchListener;
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
    public class AudioViewHolder extends BaseMediaViewHolder<Comment> {
        private ItemCommentAudioBinding mBinding;
        private OnTimelineItemTouchListener<Comment> mTouchListener;

        public AudioViewHolder(ItemCommentAudioBinding itemView,
                OnTimelineItemTouchListener<Comment> touchListener) {
            super(itemView.getRoot());
            mBinding = itemView;
            mTouchListener = touchListener;
        }

        @Override
        public void bindData(Comment model) {
            mUri = Uri.parse(model.getMediaModel().getUrl());
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }

        @Override
        protected SimpleExoPlayerView getMediaPlayerView() {
            return mBinding.player;
        }
    }

    /**
     * Display comment model with image media
     */
    public class ImageViewHolder extends BaseViewHolder<Comment> {
        private ItemCommentImageBinding mBinding;
        private OnTimelineItemTouchListener<Comment> mTouchListener;

        public ImageViewHolder(ItemCommentImageBinding itemView,
                OnTimelineItemTouchListener<Comment> touchListener) {
            super(itemView.getRoot());
            mBinding = itemView;
            mTouchListener = touchListener;
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
    public class VideoViewHolder extends BaseMediaViewHolder<Comment> {
        private ItemCommentVideoBinding mBinding;
        private OnTimelineItemTouchListener<Comment> mTouchListener;

        VideoViewHolder(ItemCommentVideoBinding itemView,
                OnTimelineItemTouchListener<Comment> touchListener) {
            super(itemView.getRoot());
            mBinding = itemView;
            mTouchListener = touchListener;
        }

        @Override
        public void bindData(Comment model) {
            mUri = Uri.parse(model.getMediaModel().getUrl());
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }

        @Override
        protected SimpleExoPlayerView getMediaPlayerView() {
            return mBinding.videoView;
        }
    }
}
