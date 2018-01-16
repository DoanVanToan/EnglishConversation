package com.framgia.englishconversation.screen.audiodetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.databinding.ActivityAudioDetailBinding;
import com.framgia.englishconversation.utils.Constant;

import static com.framgia.englishconversation.utils.Constant.EXTRA_USER;

/**
 * Created by fs-sournary.
 * Date on 12/19/17.
 * Description: audio detail screen
 */

public class AudioDetailActivity extends BaseActivity {

    private AudioDetailContract.View mView;

    /**
     * @param timelineModel truyền sang detail khi người dùng click vào 1 bài post trong timeline
     * @param timelineUser của activity profile truyền vào để so sánh nếu user bài post trùng với
     * timelineUser thì sẽ không điều hướng đến activity profile nữa
     */
    public static Intent getInstance(Context context, TimelineModel timelineModel,
            UserModel timelineUser) {
        Intent intent = new Intent(context, AudioDetailActivity.class);
        intent.putExtra(Constant.EXTRA_TIMELINE, timelineModel);
        intent.putExtra(EXTRA_USER, timelineUser);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAudioDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_audio_detail);
        TimelineModel timelineModel = getIntent().getParcelableExtra(Constant.EXTRA_TIMELINE);
        UserModel userModel = getIntent().getExtras().getParcelable(EXTRA_USER);
        mView = new AudioDetailViewModel(this, timelineModel, getSupportFragmentManager(),
                userModel);
        AudioDetailPresenter presenter = new AudioDetailPresenter(mView);
        mView.setPresenter(presenter);
        binding.setViewModel((AudioDetailViewModel) mView);
        getSupportActionBar(getString(R.string.title_post_detail,
                timelineModel.getCreatedUser().getUserName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mView.onFinishActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause() {
        mView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mView.onStop();
        super.onStop();
    }
}
