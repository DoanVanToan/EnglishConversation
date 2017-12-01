package com.framgia.englishconversation.utils.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.framgia.englishconversation.R;

import java.util.Calendar;

/**
 *
 */

public final class BindingUtils {

    private BindingUtils() {
        // No-op
    }

    /**
     * setAdapter For RecyclerView
     */
    @BindingAdapter({"recyclerAdapter"})
    public static void setAdapterForRecyclerView(RecyclerView recyclerView,
                                                 RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter({"bind:adapter"})
    public static void setViewPagerAdapter(ViewPager viewPager, PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @BindingAdapter({"bind:onTabSelected"})
    public static void setOnTabSelectedListener(TabLayout tabLayout,
                                                TabLayout.OnTabSelectedListener listener) {
        tabLayout.addOnTabSelectedListener(listener);
    }

    @BindingAdapter({"bind:imageUrl", "bind:imageError"})
    public static void loadImage(ImageView imageView, String url, Drawable error) {
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .error(error)
                .placeholder(error)
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter("bind:imageUri")
    public static void loadImage(final ImageView imageView, Uri uri) {
        if (uri == null) return;
        Glide.with(imageView.getContext())
                .load(uri.toString())
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }


    @BindingAdapter({"spinnerAdapter"})
    public static void setAdapterForSpinner(AppCompatSpinner spinner,
                                            ArrayAdapter<String> adapter) {
        spinner.setAdapter(adapter);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView,
                                        LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingAdapter("errorText")
    public static void setErrorText(EditText editText, String text) {
        editText.setError(text);
    }

    @BindingAdapter("bind:milisecond")
    public static void setDate(TextView view, long milisecond) {
        String niceDateStr = String.valueOf(DateUtils.getRelativeTimeSpanString(milisecond,
                Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));
        view.setText(niceDateStr);
    }
}
