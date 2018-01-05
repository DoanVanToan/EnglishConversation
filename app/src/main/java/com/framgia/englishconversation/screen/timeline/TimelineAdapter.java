package com.framgia.englishconversation.screen.timeline;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.databinding.ItemTimelineAudioBinding;
import com.framgia.englishconversation.databinding.ItemTimelineConversationBinding;
import com.framgia.englishconversation.databinding.ItemTimelineImageBinding;
import com.framgia.englishconversation.databinding.ItemTimelineOnlyTextBinding;
import com.framgia.englishconversation.databinding.ItemTimelineVideoBinding;
import com.framgia.englishconversation.screen.BaseMediaViewHolder;
import com.framgia.englishconversation.screen.BaseViewHolder;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.List;

/**
 * Created by toand on 5/17/2017.
 */

public class TimelineAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<TimelineModel> mData;
    private OnTimelineItemTouchListener mItemTouchListener;
    private TimelineViewModel mViewModel;

    public TimelineAdapter(List<TimelineModel> items, TimelineViewModel viewModel) {
        mData = items;
        mViewModel = viewModel;
    }

    public void setRecyclerViewItemClickListener(OnTimelineItemTouchListener itemTouchListener) {
        mItemTouchListener = itemTouchListener;
    }

    public void updateDataForward(TimelineModel timelineModel) {
        if (timelineModel == null) {
            return;
        }
        mData.add(0, timelineModel);
        notifyDataSetChanged();
    }

    public void updateData(List<TimelineModel> timelines) {
        if (timelines == null) return;
        mData.addAll(timelines);
        notifyDataSetChanged();
    }

    public void updateData(TimelineModel timeline) {
        mData.add(timeline);
        notifyItemInserted(mData.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getPostType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.ONLY_TEXT:
                ItemTimelineOnlyTextBinding onlyTextBinding = ItemTimelineOnlyTextBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false);
                return new OnlyTextViewHolder(onlyTextBinding);
            case MediaModel.MediaType.AUDIO:
                ItemTimelineAudioBinding audioBinding =
                        ItemTimelineAudioBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new AudioViewHolder(audioBinding);
            case MediaModel.MediaType.VIDEO:
                ItemTimelineVideoBinding videoBinding =
                        ItemTimelineVideoBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new VideoViewHolder(videoBinding, mItemTouchListener);
            case MediaModel.MediaType.IMAGE:
                ItemTimelineImageBinding imageBinding =
                        ItemTimelineImageBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new ImageViewHolder(imageBinding, mItemTouchListener);
            case MediaModel.MediaType.CONVERSATION:
                ItemTimelineConversationBinding conversationBinding =
                        ItemTimelineConversationBinding.inflate(
                                LayoutInflater.from(parent.getContext()), parent, false);
                return new ConversationViewHolder(conversationBinding, mItemTouchListener);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public TimelineModel getLastItem() {
        return mData != null && !mData.isEmpty() ? mData.get(mData.size() - 1) : null;
    }

    public TimelineModel getFirstItem() {
        return mData != null && !mData.isEmpty() ? mData.get(0) : null;
    }

    /**
     * Display timeline model with only text (without media)
     */
    public class OnlyTextViewHolder extends BaseViewHolder<TimelineModel> {
        private ItemTimelineOnlyTextBinding mBinding;

        public OnlyTextViewHolder(ItemTimelineOnlyTextBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(TimelineModel model) {
            mBinding.setTimelineModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display timeline model with audio media
     */
    public class AudioViewHolder extends BaseMediaViewHolder<TimelineModel> {
        private ItemTimelineAudioBinding mBinding;

        public AudioViewHolder(ItemTimelineAudioBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(TimelineModel model) {
            if (model == null || model.getMedias() == null || model.getMedias().isEmpty()) {
                return;
            }
            mUri = Uri.parse(model.getMedias().get(0).getUrl());
            mBinding.setTimelineModel(model);
            mBinding.setViewModel(mViewModel);
            mBinding.executePendingBindings();
        }

        @Override
        protected SimpleExoPlayerView getMediaPlayerView() {
            return mBinding.player;
        }
    }

    /**
     * Display timneline model with image media
     */
    public class ImageViewHolder extends BaseViewHolder<TimelineModel> {
        private ItemTimelineImageBinding mBinding;
        private OnTimelineItemTouchListener mOnTouchListener;

        public ImageViewHolder(ItemTimelineImageBinding itemView,
                               OnTimelineItemTouchListener onTouchListener) {
            super(itemView.getRoot());
            mBinding = itemView;
            mOnTouchListener = onTouchListener;
        }

        @Override
        public void bindData(TimelineModel model) {
            mBinding.setTimelineModel(model);
            mBinding.setTouchListener(mOnTouchListener);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display timeline model with video media
     */
    public class VideoViewHolder extends BaseMediaViewHolder<TimelineModel> {
        private ItemTimelineVideoBinding mBinding;
        private OnTimelineItemTouchListener mOnTouchListener;

        VideoViewHolder(ItemTimelineVideoBinding itemView,
                        OnTimelineItemTouchListener onTouchListener) {
            super(itemView.getRoot());
            mBinding = itemView;
            mOnTouchListener = onTouchListener;
        }

        @Override
        public void bindData(TimelineModel model) {
            if (model == null || model.getMedias() == null || model.getMedias().isEmpty()) {
                return;
            }
            mUri = Uri.parse(model.getMedias().get(0).getUrl());
            mBinding.setTimelineModel(model);
            mBinding.setTouchListener(mOnTouchListener);
            mBinding.executePendingBindings();
        }


        @Override
        protected SimpleExoPlayerView getMediaPlayerView() {
            return mBinding.videoView;
        }
    }

    /**
     * display timeline model with conversation
     */

    public class ConversationViewHolder extends BaseViewHolder<TimelineModel> {
        private ItemTimelineConversationBinding mBinding;
        private OnTimelineItemTouchListener mTouchListener;

        ConversationViewHolder(ItemTimelineConversationBinding itemView,
                               OnTimelineItemTouchListener touchListener) {
            super(itemView.getRoot());
            mBinding = itemView;
            mTouchListener = touchListener;
        }

        @Override
        public void bindData(TimelineModel model) {
            mBinding.setTimelineModel(model);
            mBinding.setTouchListener(mTouchListener);
            mBinding.executePendingBindings();
        }
    }

}
