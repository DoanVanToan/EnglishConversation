package com.framgia.englishconversation.utils.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.bumptech.glide.Glide;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.utils.Blocker;
import com.framgia.englishconversation.utils.Constant;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import java.util.Calendar;

/**
 *
 */

public final class BindingUtils {

    private BindingUtils() {
        // No-op
    }

    /**
     * setMediaAdapter For RecyclerView
     */
    @BindingAdapter({ "recyclerAdapter" })
    public static void setAdapterForRecyclerView(RecyclerView recyclerView,
            RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * setNested scrollenable For RecyclerView
     */
    @BindingAdapter({ "scrollEnabled" })
    public static void setRecyclerViewSrollEnable(RecyclerView recyclerView, boolean isEnabled) {
        recyclerView.setNestedScrollingEnabled(isEnabled);
    }

    @BindingAdapter({ "bind:adapter" })
    public static void setViewPagerAdapter(ViewPager viewPager, PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @BindingAdapter({ "bind:onTabSelected" })
    public static void setOnTabSelectedListener(TabLayout tabLayout,
            TabLayout.OnTabSelectedListener listener) {
        tabLayout.addOnTabSelectedListener(listener);
    }

    @BindingAdapter({ "bind:imageUrl", "bind:imageError" })
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

    @BindingAdapter("bind:imageUrlFullSize")
    public static void loadImageFullSize(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }

    @BindingAdapter({ "bind:videoPath" })
    public static void loadVideoUri(VideoView videoView, String path) {
        videoView.setVideoPath(path);
    }

    @BindingAdapter({ "spinnerAdapter" })
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

    @BindingAdapter("bind:onTouchListen")
    public static void setOnTouchListener(EditText editText, View.OnTouchListener listener) {
        editText.setOnTouchListener(listener);
    }

    @BindingAdapter("videoPlayer")
    public static void setVideoPlayer(SimpleExoPlayerView exoPlayerView,
            SimpleExoPlayer exoPlayer) {
        exoPlayerView.setPlayer(exoPlayer);
    }

    @BindingAdapter("enableAudioPlayerMode")
    public static void setEnableAudioPlayerMode(SimpleExoPlayerView exoPlayerView, boolean enable) {
        if (enable) {
            exoPlayerView.setControllerShowTimeoutMs(0);
            exoPlayerView.setControllerHideOnTouch(false);
        }
    }

    @BindingAdapter({ "onClickSafely" })
    public static void setOnClickSafely(View view, final View.OnClickListener listener) {
        view.setOnClickListener(new View.OnClickListener() {
            private Blocker mBlocker = new Blocker();

            @Override
            public void onClick(View v) {
                if (!mBlocker.block()) listener.onClick(v);
            }
        });
    }

    @BindingAdapter({ "errorTextInputLayout" })
    public static void setErrorTextInputLayout(final TextInputLayout textInputLayout,
            final String text) {
        textInputLayout.setError(text);
        final EditText editText = textInputLayout.getEditText();
        if (editText == null) {
            return;
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //No-op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < Constant.MIN_CHARACTERS) {
                    textInputLayout.setError(text);
                }
                if (charSequence.length() > Constant.ZERO_PERCENT) {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textInputLayout.setError("");
            }
        });
    }

    @BindingAdapter({ "bind:player" })
    public static void setUpPlayer(SimpleExoPlayerView playerView, SimpleExoPlayer player) {
        playerView.setPlayer(player);
    }

    @BindingAdapter({ "bind:manager", "bind:fragment" })
    public static void setFragmentManager(FrameLayout layout, FragmentManager manager,
            Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(layout.getId(), fragment).commit();
    }

    @BindingAdapter({ "scrollListener" })
    public static void setScrollListener(RecyclerView recyclerView,
            RecyclerView.OnScrollListener onScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener);
    }
}
