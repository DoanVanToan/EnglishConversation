package com.framgia.englishconversation.screen.selectedimagedetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.databinding.ActivitySelectedImageDetailBinding;
import com.framgia.englishconversation.utils.Constant;
import java.util.ArrayList;
import java.util.List;

/**
 * SelectedImageDetail Screen.
 */
public class SelectedImageDetailActivity extends BaseActivity {

    private SelectedImageDetailContract.ViewModel mViewModel;

    public static Intent getIntent(Context context, List<MediaModel> mediaModels, int position) {
        Intent intent = new Intent(context, SelectedImageDetailActivity.class);
        intent.putParcelableArrayListExtra(Constant.EXTRA_MEDIA_MODEL,
                (ArrayList<MediaModel>) mediaModels);
        intent.putExtra(Constant.EXTRA_POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<MediaModel> mediaModels =
                getIntent().getExtras().getParcelableArrayList(Constant.EXTRA_MEDIA_MODEL);
        int position = getIntent().getExtras().getInt(Constant.EXTRA_POSITION);
        mViewModel = new SelectedImageDetailViewModel(mediaModels, position);

        SelectedImageDetailContract.Presenter presenter =
                new SelectedImageDetailPresenter(mViewModel);
        mViewModel.setPresenter(presenter);

        ActivitySelectedImageDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_selected_image_detail);
        binding.setViewModel((SelectedImageDetailViewModel) mViewModel);
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
}
