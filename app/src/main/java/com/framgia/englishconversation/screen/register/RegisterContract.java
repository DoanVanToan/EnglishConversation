package com.framgia.englishconversation.screen.register;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

/**
 * This specifies the contract between the view and the presenter.
 */
interface RegisterContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onRegisterSuccess(FirebaseUser user);

        void onRegisterFailed(String message);

        void showProgressDialog();

        void onEmailError(int message);

        void onPassWordError(int message);

        void onConfirmPasswordError(int message);

        void onPasswordConfirmNotCorrect();

        void onRegisterClick();

        void dismissDialog();

        void onForgotPwClick();

        void onLoginClick();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void registerAccount(String email, String password, String passwordConfirm);

        boolean validateInput(String email, String password, String passwordConfirm);
    }
}
