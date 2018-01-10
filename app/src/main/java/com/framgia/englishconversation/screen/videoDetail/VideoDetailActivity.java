package com.framgia.englishconversation.screen.videoDetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
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
        TimelineModel model = getIntent().getExtras().getParcelable(Constant.EXTRA_TIMELINE);
        mViewModel = new VideoDetailViewModel(this, getSupportFragmentManager(), model);
        VideoDetailContract.Presenter presenter = new VideoDetailPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityVideoDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        binding.setViewModel((VideoDetailViewModel) mViewModel);
        getSupportActionBar(
                getString(R.string.title_post_detail, model.getCreatedUser().getUserName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mViewModel.finishActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.RequestCode.POST_COMMENT:
                if (resultCode != RESULT_OK) {
                    //TODO: Handle exception later
                    return;
                }
                //TODO:Handle the main story
                break;
        }
    }
}
