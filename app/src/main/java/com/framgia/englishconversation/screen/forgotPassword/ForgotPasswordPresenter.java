package com.framgia.englishconversation.screen.forgotPassword;

import android.text.TextUtils;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.utils.Constant;

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

    @Override
    public boolean validateInput(String email) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            isValid = false;
            mViewModel.onInputEmailError(R.string.is_empty);
        }
        if (!TextUtils.isEmpty(email) && !email.matches(Constant.EMAIL_FORMAT)) {
            isValid = false;
            mViewModel.onInputEmailError(R.string.invalid_email_format);
        }
        return isValid;
    }
}
