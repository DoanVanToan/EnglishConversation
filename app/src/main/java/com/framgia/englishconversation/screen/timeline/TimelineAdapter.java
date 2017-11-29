package com.framgia.englishconversation.screen.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.databinding.ItemTimelineBinding;

import java.util.List;

/**
 * Created by toand on 5/17/2017.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
    private List<TimelineModel> mItems;

    public TimelineAdapter(List<TimelineModel> items) {
        mItems = items;
    }

    public void updateData(List<TimelineModel> timelines) {
        if (timelines == null) return;
        mItems.addAll(timelines);
        notifyDataSetChanged();
    }

    public void updateData(TimelineModel timeline) {
        mItems.add(timeline);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public TimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTimelineBinding item =
                ItemTimelineBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                        false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(TimelineAdapter.ViewHolder holder, int position) {
        TimelineModel model = mItems.get(position);
        holder.bindData(model);
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTimelineBinding mBinding;

        public ViewHolder(ItemTimelineBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        public void bindData(TimelineModel model) {
            mBinding.setTimelineModel(model);
            mBinding.executePendingBindings();
        }
    }
}
