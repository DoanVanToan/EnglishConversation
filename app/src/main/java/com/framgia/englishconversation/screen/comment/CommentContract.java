package com.framgia.englishconversation.screen.comment;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.Comment;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface CommentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onPause();

        void onResume();

        void onDestroy();

        void onGetCommentsSuccess(List<Comment> comments);

        void onGetCommentsFailure(String message);

        void onGetCommentSuccess(Comment comment);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onPause();

        void onResume();

        void onDestroy();

        void fetchCommentData(Comment lastComments);
    }
}
