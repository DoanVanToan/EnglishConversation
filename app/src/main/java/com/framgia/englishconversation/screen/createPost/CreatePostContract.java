package com.framgia.englishconversation.screen.createPost;

import android.content.Intent;
import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface CreatePostContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {

        void onPause();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onGetCurrentUserSuccess(UserModel data);

        void onGetCurrentUserFailed(String msg);

        void onImagePickerClick();

        void onPlacePickerClick();

        void onCreatePost();

        void uploadFiles(List<MediaModel> mediaModels);

        void onCreatePostSuccess();

        void onCreatePostFailed(String msg);

        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

        void onDestroy();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void getUser();

        void createPost(TimelineModel timelineModel);

        void onDestroy();
    }
}
