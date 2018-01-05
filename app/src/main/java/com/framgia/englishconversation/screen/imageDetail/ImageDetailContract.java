package com.framgia.englishconversation.screen.imagedetail;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.UserModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ImageDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetCurrentUserSuccess(UserModel data);

        void onGetCurrentUserFailed(String msg);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
    }
}
