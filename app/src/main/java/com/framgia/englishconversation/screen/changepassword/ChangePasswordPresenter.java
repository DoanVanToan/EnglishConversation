package com.framgia.englishconversation.screen.changepassword;

import android.text.TextUtils;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.auth.ChangePasswordCallBack;
import com.framgia.englishconversation.utils.Constant;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by ADMIN on 2/8/2018.
 */

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {

    private ChangePasswordContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;

    public ChangePasswordPresenter(ChangePasswordContract.ViewModel viewModel,
                                   AuthenicationRepository repository) {
        mViewModel = viewModel;
        mRepository = repository;
        getUser();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void changePassword(FirebaseUser firebaseUser, String currentPassword,
                               String confirmPassword) {
        mRepository.changePassword(firebaseUser, currentPassword, confirmPassword,
                new DataCallback() {
            @Override
            public void onGetDataSuccess(Object data) {

            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onChangePasswordFailed();

            }
        }, new ChangePasswordCallBack() {
            @Override
            public void onChangePasswordSuccess() {
                mViewModel.onChangPasswordSuccess();
            }

            @Override
            public void onChangePasswordFailed() {
                mViewModel.onChangePasswordFailed();

            }
        });


    }

    private void getUser() {
        mRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetCurrentUserSuccess(data);
            }

            @Override
            public void onGetDataFailed(String msg) {
            }
        });
    }

    @Override
    public boolean validateInput(
            String currentPasword, String newPassword, String confirmPassword) {
        boolean isValid = true;
        if (TextUtils.isEmpty(newPassword)) {
            isValid = false;
            mViewModel.onPassWordError(R.string.is_empty);
        }
        if (!TextUtils.isEmpty(newPassword)
                && newPassword.length() < Constant.MINIMUM_CHARACTERS_PASSWORD) {
            isValid = false;
            mViewModel.onPassWordError(R.string.least_6_characters);
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            isValid = false;
            mViewModel.onConfirmPasswordError(R.string.is_empty);
        }
        if (!TextUtils.isEmpty(confirmPassword) && !confirmPassword.equals(newPassword)) {
            isValid = false;
            mViewModel.onConfirmPasswordError(R.string.confirm_password_does_not_match);
        }
        return isValid;
    }
}

