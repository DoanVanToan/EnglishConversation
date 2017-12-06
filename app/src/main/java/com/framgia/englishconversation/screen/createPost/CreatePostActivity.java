package com.framgia.englishconversation.screen.createPost;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.PostType;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.databinding.ActivityCreatePostBinding;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * CreatePost Screen.
 */
public class CreatePostActivity extends BaseActivity {

    public static final int CONVERSATION_POSITION = 0;
    public static final int AUDIO_RECORD_POSITION = 1;
    public static final int VIDEO_RECORD_POSITION = 2;
    public static final int PHOTO_POSITION = 3;
    public static final int LOCATION_POSITION = 4;
    private static final String EXTRA_CREATE_TYPE = "EXTRA_CREATE_TYPE";

    @PostType
    private int mCreateType;

    private CreatePostContract.ViewModel mViewModel;

    private ImageView[] mImageViews;

    public static Intent getInstance(Context context, @PostType int createType) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        intent.putExtra(EXTRA_CREATE_TYPE, createType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

        mViewModel = new CreatePostViewModel(this, new Navigator(this), mCreateType);
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        TimelineRepository timelineRepository =
                new TimelineRepository(new TimelineRemoteDataSource());
        CreatePostContract.Presenter presenter =
                new CreatePostPresenter(mViewModel, repository, timelineRepository);
        mViewModel.setPresenter(presenter);

        ActivityCreatePostBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_create_post);
        binding.setViewModel((CreatePostViewModel) mViewModel);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_create_post);
            getSupportActionBar().setSubtitle(R.string.subtitle_create_post);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mImageViews = new ImageView[]{
                binding.imageConversation, binding.imageRecordAudio, binding.imageVideo,
                binding.imagePhoto, binding.imageLocation
        };
        int selectedColor = ContextCompat.getColor(this, R.color.color_indogo_acceent_700);
        binding.imageConversation.getDrawable()
                .setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent == null) return;
        if (intent.getExtras() != null) {
            mCreateType = intent.getExtras().getInt(EXTRA_CREATE_TYPE);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_save:
                mViewModel.onCreatePost();
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

    public void fillColorSelectedButton(int position) {
        int selectedColor = ContextCompat.getColor(this, R.color.color_indogo_acceent_700);
        int unSelectedColor = ContextCompat.getColor(this, android.R.color.black);
        for (int i = 0; i < mImageViews.length; i++) {
            if (i == position) {
                mImageViews[i].setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                mImageViews[i].setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
