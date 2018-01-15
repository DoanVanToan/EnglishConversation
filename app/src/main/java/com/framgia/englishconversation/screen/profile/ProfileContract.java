package com.framgia.englishconversation.screen.profile;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.UserModel;

;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ProfileContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetUserSuccesss(UserModel data);

        void onChangeAvtClick();

        void onEditUserClick();

        void showChangePasswordDialog();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
    }
}
