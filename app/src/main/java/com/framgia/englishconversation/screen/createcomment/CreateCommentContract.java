package com.framgia.englishconversation.screen.createcomment;

import android.content.Intent;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface CreateCommentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onPause();

        void onResume();

        void setPlaying(boolean playing);

        void setTimeInProgressAudio(String timeInProgressAudio);

        void showToast(int stringId);

        void onGetMultimediaDataDone(Intent intent, @MediaModel.MediaType int type);

        void onMultimediaFileAttached(MediaModel mediaModel);

        void onPostLiteralCommentResult(boolean isSuccess, Intent intent);

        void onDestroy();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onPause();

        void onResume();

        void onMultimediaMenuItemClick(int type);

        void onRecordVideoDone(String uri, @MediaModel.MediaType int type);

        void onImageSelectDone(Image image);

        Comment getComment();

        void onDeleteItemMediaClicked();

        void postLiteralComment();

        void onDestroy();
    }
}
