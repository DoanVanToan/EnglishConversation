package com.framgia.englishconversation.screen.register;

import android.text.TextUtils;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.utils.Constant;
import com.google.firebase.auth.FirebaseUser;

/**
 * Listens to user actions from the UI ({@link RegisterActivity}), retrieves the data and updates
 * the UI as required.
 */
final class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;

    public RegisterPresenter(RegisterContract.ViewModel viewModel,
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
    public void registerAccount(String email, String password, String passwordConfirm) {

        mViewModel.showProgressDialog();
        mRepository.register(email, password, new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.dismissDialog();
                mViewModel.onRegisterSuccess(data);
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.dismissDialog();
                mViewModel.onRegisterFailed(msg);
            }
        });
    }

    @Override
    public boolean validateInput(String email, String password, String passwordConfirm) {
        boolean isValid = true;
        if (TextUtils.isEmpty(email)) {
            isValid = false;
            mViewModel.onEmailError(R.string.is_empty);
        }
        if (!TextUtils.isEmpty(email) && !email.matches(Constant.EMAIL_FORMAT)) {
            isValid = false;
            mViewModel.onEmailError(R.string.invalid_email_format);
        }
        if (TextUtils.isEmpty(password)) {
            isValid = false;
            mViewModel.onPassWordError(R.string.is_empty);
        }
        if (!TextUtils.isEmpty(password)
                && password.length() < Constant.MINIMUM_CHARACTERS_PASSWORD) {
            isValid = false;
            mViewModel.onPassWordError(R.string.least_6_characters);
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            isValid = false;
            mViewModel.onConfirmPasswordError(R.string.is_empty);
        }
        if (!TextUtils.isEmpty(passwordConfirm) && !passwordConfirm.equals(password)) {
            isValid = false;
            mViewModel.onConfirmPasswordError(R.string.confirm_password_does_not_match);
        }
        return isValid;
    }
}

