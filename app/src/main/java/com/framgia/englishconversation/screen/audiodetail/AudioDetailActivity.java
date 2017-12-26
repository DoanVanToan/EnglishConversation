package com.framgia.englishconversation.screen.audiodetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.databinding.ActivityAudioDetailBinding;
import com.framgia.englishconversation.utils.Constant;

/**
 * Created by fs-sournary.
 * Date on 12/19/17.
 * Description: audio detail screen
 */

public class AudioDetailActivity extends BaseActivity {

    private AudioDetailContract.View mView;

    public static Intent getInstance(Context context, TimelineModel timelineModel) {
        Intent intent = new Intent(context, AudioDetailActivity.class);
        intent.putExtra(Constant.EXTRA_TIMELINE, timelineModel);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAudioDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_audio_detail);
        TimelineModel timelineModel = getIntent().getParcelableExtra(Constant.EXTRA_TIMELINE);
        mView = new AudioDetailViewModel(this, timelineModel);
        AudioDetailPresenter presenter = new AudioDetailPresenter(mView);
        mView.setPresenter(presenter);
        binding.setViewModel((AudioDetailViewModel) mView);
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().hide();
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
