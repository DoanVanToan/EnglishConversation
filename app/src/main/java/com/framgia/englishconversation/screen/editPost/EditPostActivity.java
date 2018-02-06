package com.framgia.englishconversation.screen.editPost;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.databinding.ActivityEditPostBinding;
import com.framgia.englishconversation.utils.Blocker;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.navigator.Navigator;

public class EditPostActivity extends BaseActivity {

    private EditPostContract.ViewModel mViewModel;
    private Blocker mBlocker;

    public static Intent getInstance(Context context, TimelineModel timelineModel) {
        Intent intent = new Intent(context, EditPostActivity.class);
        intent.putExtra(Constant.PARCEL_TIMELINE, timelineModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new EditPostViewModel(this,
                new Navigator(this),
                (TimelineModel) getIntent().getParcelableExtra(Constant.PARCEL_TIMELINE));
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        TimelineRepository timelineRepository =
                new TimelineRepository(new TimelineRemoteDataSource());
        EditPostContract.Presenter presenter =
                new EditPostPresenter(mViewModel, repository, timelineRepository,
                        (TimelineModel) getIntent().getParcelableExtra(Constant.PARCEL_TIMELINE));
        mViewModel.setPresenter(presenter);

        ActivityEditPostBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_edit_post);
        binding.setViewModel((EditPostViewModel) mViewModel);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_post);
            getSupportActionBar().setSubtitle(R.string.subtitle_create_post);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mBlocker = new Blocker();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
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

    @Override
    protected void onDestroy() {
        mViewModel.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_edit:
                if (!mBlocker.block()) {
                    mViewModel.onSubmitPost();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
