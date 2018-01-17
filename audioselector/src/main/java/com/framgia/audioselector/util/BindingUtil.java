package com.framgia.audioselector.util;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class BindingUtil {

    @BindingAdapter({"bind:adapter"})
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }
}
