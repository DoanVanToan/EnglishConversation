package com.framgia.englishconversation.screen.changepassword;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by ADMIN on 2/8/2018.
 */

public class ChangePasswordContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<ChangePasswordContract.Presenter> {
        void onGetCurrentUserSuccess(FirebaseUser firebaseUser);

        void onChangPasswordSuccess();

        void onChangePasswordFailed();

        void onPassWordError(int message);

        void onConfirmPasswordError(int message);

        void onChangePasswordClick();

        void onCancelClick();

        void dismissDialog();

        void onShowDialogProcess();

        void onDismissDialogProcess();

    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {

        void changePassword(FirebaseUser firebaseUser, String currentPassword,
                            String confirmPassword);

        boolean validateInput(String currentPassword, String newPassword, String confirmPassword);
    }
}

