package com.framgia.englishconversation.screen.createPost;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.PostType;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.databinding.ActivityCreatePostBinding;
import com.framgia.englishconversation.databinding.ItemPostFirstTypeBinding;
import com.framgia.englishconversation.databinding.ItemPostFourTypeBinding;
import com.framgia.englishconversation.databinding.ItemPostSecondTypeBinding;
import com.framgia.englishconversation.databinding.ItemPostThridTypeBinding;
import com.framgia.englishconversation.databinding.ItemUploadProgressBinding;
import com.framgia.englishconversation.databinding.RecordItemBinding;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.navigator.Navigator;
import java.util.List;

/**
 * CreatePost Screen.
 */
public class CreatePostActivity extends BaseActivity {

    private static final String EXTRA_CREATE_TYPE = "EXTRA_CREATE_TYPE";

    private CreatePostContract.ViewModel mViewModel;
    @PostType
    private int mCreateType;
    private LinearLayout mLinearImages;
    private LinearLayout mLinearRecords;
    private LinearLayout mLinearProgress;
    private View mBottomSheetView;
    private BottomSheetBehavior mBottomSheetBehavior;

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

        getSupportActionBar().setTitle(R.string.title_create_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLinearImages = binding.linearImages;
        mLinearRecords = binding.linearRecords;
        mLinearProgress = binding.linearProgress;
        mBottomSheetView = binding.bottomSheet;
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetView);
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent == null) return;
        mCreateType = intent.getExtras().getInt(EXTRA_CREATE_TYPE);
    }

    public void addImagePost(List<MediaModel> models) {
        mLinearImages.removeAllViews();
        switch (models.size()) {
            case Constant.Timeline.ONE_IMAGE:
                ItemPostFirstTypeBinding firstTypeBinding =
                    ItemPostFirstTypeBinding.inflate(getLayoutInflater());
                firstTypeBinding.setListData(models);
                mLinearImages.addView(firstTypeBinding.getRoot());
                break;
            case Constant.Timeline.TWO_IMAGE:
                ItemPostSecondTypeBinding secondTypeBinding =
                    ItemPostSecondTypeBinding.inflate(getLayoutInflater());
                secondTypeBinding.setListData(models);
                mLinearImages.addView(secondTypeBinding.getRoot());
                break;
            case Constant.Timeline.THREE_IMAGE:
                ItemPostThridTypeBinding thirdTypeBinding =
                    ItemPostThridTypeBinding.inflate(getLayoutInflater());
                thirdTypeBinding.setListData(models);
                mLinearImages.addView(thirdTypeBinding.getRoot());
                break;
            case Constant.Timeline.FOUR_IMAGE:
            default:
                ItemPostFourTypeBinding fourTypeBinding =
                    ItemPostFourTypeBinding.inflate(getLayoutInflater());
                fourTypeBinding.setListData(models);
                mLinearImages.addView(fourTypeBinding.getRoot());
                break;
        }
    }

    public void addPostRecord(List<MediaModel> records) {
        mLinearRecords.removeAllViews();
        for (MediaModel record : records){
            RecordItemBinding recordItemBinding = RecordItemBinding.inflate(getLayoutInflater());
            recordItemBinding.setRecord(record);
            mLinearRecords.addView(recordItemBinding.getRoot());
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

    public void showUploadProgressView(List<MediaModel> mediaModels) {
        if (mediaModels == null || mediaModels.size() == 0) return;
        mLinearProgress.removeAllViews();
        for (MediaModel mediaModel : mediaModels) {
            ItemUploadProgressBinding itemUpload =
                ItemUploadProgressBinding.inflate(getLayoutInflater());
            itemUpload.setMediaModel(mediaModel);
            mLinearProgress.addView(itemUpload.getRoot());
        }
        mBottomSheetBehavior.setPeekHeight(300);
    }

    public void hideBottomSheet() {
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
