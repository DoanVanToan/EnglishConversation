package com.framgia.englishconversation.screen.imagedetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.databinding.ActivityImageDetailBinding;
import com.framgia.englishconversation.utils.Constant;

import static com.framgia.englishconversation.utils.Constant.EXTRA_USER;

/**
 * ImageDetail Screen.
 */
public class ImageDetailActivity extends BaseActivity {

    private ImageDetailContract.ViewModel mViewModel;

    /**
     * @param timelineModel truyền sang detail khi người dùng click vào 1 bài post trong timeline
     * @param timelineUser của activity profile truyền vào để so sánh nếu user bài post trùng với
     * timelineUser thì sẽ không điều hướng đến activity profile nữa
     */
    public static Intent getInstance(Context context, TimelineModel timelineModel,
            UserModel timelineUser) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(Constant.EXTRA_TIMELINE, timelineModel);
        intent.putExtra(EXTRA_USER, timelineUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimelineModel timelineModel =
                getIntent().getExtras().getParcelable(Constant.EXTRA_TIMELINE);
        UserModel userModel = getIntent().getExtras().getParcelable(EXTRA_USER);
        mViewModel = new ImageDetailViewModel(this, timelineModel, getSupportFragmentManager(),
                userModel);

        ImageDetailContract.Presenter presenter = new ImageDetailPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivityImageDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_image_detail);
        binding.setViewModel((ImageDetailViewModel) mViewModel);
        getSupportActionBar(getString(R.string.title_post_detail,
                timelineModel.getCreatedUser().getUserName()));
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
