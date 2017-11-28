package com.framgia.englishconversation.screen.register;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;

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
        if (TextUtils.isEmpty(email)) {
            mViewModel.onEmptyEmail();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mViewModel.onEmptyPassword();
            return;
        }

        if (TextUtils.isEmpty(passwordConfirm)) {
            mViewModel.onEmptyPasswordConfirm();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            mViewModel.onPasswordConfirmNotCorrect();
            return;
        }

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
}

