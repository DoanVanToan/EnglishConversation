package com.framgia.englishconversation.screen.videoDetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.databinding.ActivityVideoDetailBinding;
import com.framgia.englishconversation.utils.Constant;

/**
 * VideoDetail Screen.
 */
public class VideoDetailActivity extends BaseActivity {

    private VideoDetailContract.ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new VideoDetailViewModel(this,
                (TimelineModel) getIntent().getExtras().getParcelable(Constant.ARGUMENT_TIMELINE));
        VideoDetailContract.Presenter presenter = new VideoDetailPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityVideoDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        binding.setViewModel((VideoDetailViewModel) mViewModel);
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

    @Override
    protected void onPause() {
        mViewModel.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mViewModel.onResume();
        super.onResume();
    }
}
