package com.framgia.englishconversation.screen.forgotPassword;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ForgotPasswordContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onResetPasswordClick();

        void onEmailEmpty();

        void onResetPasswordSuccess();

        void onResetPasswordFailed(String msg);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void resetPassword(String email);
    }
}
