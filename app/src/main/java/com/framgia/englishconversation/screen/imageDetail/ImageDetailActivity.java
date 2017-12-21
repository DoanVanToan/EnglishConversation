package com.framgia.englishconversation.screen.imageDetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.databinding.ActivityImageDetailBinding;

/**
 * ImageDetail Screen.
 */
public class ImageDetailActivity extends BaseActivity {

    private ImageDetailContract.ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ImageDetailViewModel();

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
