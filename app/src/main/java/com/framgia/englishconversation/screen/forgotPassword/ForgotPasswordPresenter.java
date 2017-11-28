package com.framgia.englishconversation.screen.forgotPassword;

import android.text.TextUtils;

import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 * Listens to user actions from the UI ({@link ForgotPasswordActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private static final String TAG = ForgotPasswordPresenter.class.getName();

    private final ForgotPasswordContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;

    public ForgotPasswordPresenter(ForgotPasswordContract.ViewModel viewModel,
                                   AuthenicationRepository repository) {
        mViewModel = viewModel;
        mRepository = repository;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void resetPassword(String email) {
        if (TextUtils.isEmpty(email)) {
            mViewModel.onEmailEmpty();
            return;
        }
        mRepository.resetPassword(email, new DataCallback() {
            @Override
            public void onGetDataSuccess(Object data) {
                mViewModel.onResetPasswordSuccess();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onResetPasswordFailed(msg);
            }
        });
    }
}
