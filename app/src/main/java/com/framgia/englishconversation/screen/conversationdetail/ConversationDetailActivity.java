package com.framgia.englishconversation.screen.conversationdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.databinding.ActivityConversationDetailBinding;
import com.framgia.englishconversation.utils.Constant;

/**
 * Created by fs-sournary.
 * Date on 12/27/17.
 * Description: audio detail screen
 */
public class ConversationDetailActivity extends BaseActivity {

    private ConversationDetailContract.ViewModel mViewModel;

    public static Intent getInstance(Context context, TimelineModel timelineModel) {
        Intent intent = new Intent(context, ConversationDetailActivity.class);
        intent.putExtra(Constant.EXTRA_TIMELINE, timelineModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConversationDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_conversation_detail);
        TimelineModel timelineModel = getIntent().getParcelableExtra(Constant.EXTRA_TIMELINE);
        mViewModel = new ConversationDetailViewModel(this, timelineModel);
        ConversationDetailContract.Presenter presenter = new ConversationDetailPresenter();
        mViewModel.setPresenter(presenter);
        binding.setViewModel((ConversationDetailViewModel) mViewModel);
        getSupportActionBar(getString(R.string.title_post_detail,
                timelineModel.getCreatedUser().getUserName()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    protected void onPause() {
        mViewModel.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
