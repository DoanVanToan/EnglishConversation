package com.framgia.videoselector.screen;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

/**
 * Created by doan.van.toan on 1/15/18.
 */

public class VideoBindingUtils {
    @BindingAdapter("adapter")
    public static void setRecyclerViewAdapter(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setAdapter(adapter);
    }

    @BindingAdapter("imageUrl")
    public static void setImageThumb(final ImageView view, final String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }
}
