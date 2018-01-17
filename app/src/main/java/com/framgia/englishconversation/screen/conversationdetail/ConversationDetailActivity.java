package com.framgia.englishconversation.screen.conversationdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.databinding.ActivityConversationDetailBinding;
import com.framgia.englishconversation.utils.Constant;

import static com.framgia.englishconversation.utils.Constant.EXTRA_USER;

/**
 * Created by fs-sournary.
 * Date on 12/27/17.
 * Description: audio detail screen
 */
public class ConversationDetailActivity extends BaseActivity {

    private ConversationDetailContract.ViewModel mViewModel;

    /**
     * @param timelineModel truyền sang detail khi người dùng click vào 1 bài post trong timeline
     * @param timelineUser của activity profile truyền vào để so sánh nếu user bài post trùng với
     * timelineUser thì sẽ không điều hướng đến activity profile nữa
     */
    public static Intent getInstance(Context context, TimelineModel timelineModel,
            UserModel timelineUser) {
        Intent intent = new Intent(context, ConversationDetailActivity.class);
        intent.putExtra(Constant.EXTRA_TIMELINE, timelineModel);
        intent.putExtra(EXTRA_USER, timelineUser);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityConversationDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_conversation_detail);
        TimelineModel timelineModel = getIntent().getParcelableExtra(Constant.EXTRA_TIMELINE);
        UserModel userModel = getIntent().getExtras().getParcelable(EXTRA_USER);
        mViewModel =
                new ConversationDetailViewModel(this, getSupportFragmentManager(), timelineModel,
                        userModel);
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
