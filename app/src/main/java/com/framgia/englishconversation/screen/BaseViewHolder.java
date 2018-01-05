package com.framgia.englishconversation.screen;

/**
 * Created by doan.van.toan on 1/5/18.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.framgia.englishconversation.data.model.TimelineModel;

/**
 * Base timeline model
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(T model);
}
