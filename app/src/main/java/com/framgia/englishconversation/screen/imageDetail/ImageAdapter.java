package com.framgia.englishconversation.screen.imageDetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.databinding.ItemImageDetailBinding;
import java.util.List;

/**
 * Created by anh on 12/20/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<MediaModel> mMediaModels;

    public ImageAdapter(List<MediaModel> mediaModels) {
        mMediaModels = mediaModels;
    }

    public void updateData(List<MediaModel> mediaModels) {
        if (mediaModels == null) return;
        mMediaModels.addAll(mediaModels);
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(
                ItemImageDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bindData(mMediaModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mMediaModels == null ? 0 : mMediaModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ItemImageDetailBinding mBinding;

        public ImageViewHolder(ItemImageDetailBinding itemViewBinding) {
            super(itemViewBinding.getRoot());
            mBinding = itemViewBinding;
        }

        public void bindData(MediaModel model) {
            mBinding.setMediaModel(model);
            mBinding.executePendingBindings();
        }
    }
}
