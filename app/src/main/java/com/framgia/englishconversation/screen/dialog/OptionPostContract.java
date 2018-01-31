package com.framgia.englishconversation.screen.dialog;

import android.view.View;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.TimelineModel;

/**
 * Created by Sony on 1/25/2018.
 */

public interface OptionPostContract {
    /**
     * base presenter for ConversationDetailPresenter
     */
    interface Presenter extends BasePresenter {
        void updateViewTypePost(TimelineModel timelineModel);

        void editPost(TimelineModel timelineModel);
    }

    /**
     * base presenter for ConversationDetailViewModel
     */
    interface ViewModel extends BaseViewModel<OptionPostContract.Presenter> {

        void onClickEditPost();

        void showConfirmDeleteDialog();

        void onClickDeletePost();

    }
}
