package com.framgia.englishconversation.screen.createcomment;

import android.content.Intent;
import android.widget.PopupMenu;

import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.comment.CallBack;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CreateCommentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onPause();

        void onResume();

        void showToast(int stringId);

        void onGetMultimediaDataDone(Intent intent, @MediaModel.MediaType int type);

        void onMultimediaFileAttached(MediaModel mediaModel);

        void onPostLiteralCommentSuccess(Comment comment);

        void onPostLiteralCommentFailure(String message);

        void onDestroy();

        void setPopUpMenu(PopupMenu popUpMenu);

        void onGetCurrentUserSuccess(UserModel data);

        void onGetCurrentUserFailed(String msg);

        void setListener(CallBack callBack);

        void replaceFragment();
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

        void onDeleteItemMediaClicked();

        void postLiteralComment(Comment comment);

        void updateLiteralComment(Comment commentNew, Comment commentOld);

        void onDestroy();
    }
}
