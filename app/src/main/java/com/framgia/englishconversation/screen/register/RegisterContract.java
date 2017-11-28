package com.framgia.englishconversation.screen.register;

import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;

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

        void onEmptyEmail();

        void onEmptyPassword();

        void onEmptyPasswordConfirm();

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
    }
}
