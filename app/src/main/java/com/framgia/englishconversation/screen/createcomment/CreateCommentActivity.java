package com.framgia.englishconversation.screen.createcomment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.PopupMenu;
import com.framgia.englishconversation.BaseActivity;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsImpl;
import com.framgia.englishconversation.databinding.ActivityCreateCommentBinding;
import com.framgia.englishconversation.utils.Constant;

/**
 * CreateComment Screen.
 */
public class CreateCommentActivity extends BaseActivity {

    private CreateCommentContract.ViewModel mViewModel;
    private PopupMenu mPopupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String timelineModelId = getIntent().getExtras().getString(Constant.EXTRA_TIMELINE);
        mViewModel = new CreateCommentViewModel(this, timelineModelId);
        UserModel userModel = getIntent().getExtras().getParcelable(Constant.EXTRA_USER);
        CreateCommentContract.Presenter presenter =
                new CreateCommentPresenter(userModel, mViewModel, timelineModelId,
                        new SharedPrefsImpl(this));
        mViewModel.setPresenter(presenter);

        ActivityCreateCommentBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_create_comment);
        binding.setViewModel((CreateCommentViewModel) mViewModel);
        mPopupMenu = new PopupMenu(this, binding.ivMultimedia);
        mPopupMenu.inflate(R.menu.multimedia_popup);
        mPopupMenu.setOnMenuItemClickListener((CreateCommentViewModel) mViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onResume() {
        mViewModel.onResume();
        super.onResume();
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

    public void onMultimediaIconTouch() {
        mPopupMenu.show();
    }

    /**
     * Dispatch incoming result to the correct fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.RequestCode.RECORD_VIDEO:
                if (resultCode != RESULT_OK) {
                    //TODO: Handle exception
                    return;
                }
                mViewModel.onGetMultimediaDataDone(data, MediaModel.MediaType.VIDEO);
                break;
            case Constant.RequestCode.SELECT_IMAGE:
                if (resultCode != RESULT_OK) {
                    //TODO: Handle exception
                    return;
                }
                mViewModel.onGetMultimediaDataDone(data, MediaModel.MediaType.IMAGE);
                break;
        }
    }
}
