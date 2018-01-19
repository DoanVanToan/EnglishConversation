package com.framgia.audioselector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public abstract class BaseRecyclerViewAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {

    private Context mContext;

    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T data, int position);
    }
}
