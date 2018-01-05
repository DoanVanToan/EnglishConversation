package com.framgia.englishconversation.screen.timeline;

/**
 * Created by doan.van.toan on 1/5/18.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.englishconversation.data.model.TimelineModel;

/**
 * Base timeline model
 */
public abstract class BaseTimelineViewHolder extends RecyclerView.ViewHolder {

    public BaseTimelineViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(TimelineModel model);
}
