package com.framgia.englishconversation.screen.imagedetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.databinding.ActivityImageDetailBinding;
import com.framgia.englishconversation.utils.Constant;

/**
 * ImageDetail Screen.
 */
public class ImageDetailActivity extends BaseActivity {

    private ImageDetailContract.ViewModel mViewModel;

    public static Intent getInstance(Context context, TimelineModel timelineModel) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(Constant.EXTRA_TIMELINE, timelineModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimelineModel timelineModel =
                getIntent().getExtras().getParcelable(Constant.EXTRA_TIMELINE);
        mViewModel = new ImageDetailViewModel(this, timelineModel, getSupportFragmentManager());

        ImageDetailContract.Presenter presenter = new ImageDetailPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityImageDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_image_detail);
        binding.setViewModel((ImageDetailViewModel) mViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
