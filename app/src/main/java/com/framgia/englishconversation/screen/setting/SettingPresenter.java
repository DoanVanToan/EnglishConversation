package com.framgia.englishconversation.screen.setting;

import com.facebook.login.LoginManager;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

/**
 * Created by doan.van.toan on 1/16/18.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private AuthenicationRepository mAuthenicationRepository;
    private SettingContract.ViewModel mViewModel;
    private DataCallback mSignOutCallback;
    private FirebaseUser mUser;

    public SettingPresenter(SettingContract.ViewModel viewModel,
                            AuthenicationRepository authenicationRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
        getUser();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    private void getUser() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mUser = data;
                mViewModel.onGetCurrentUserSuccess(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetUserFailed(msg);
            }
        });
    }

    private void initSignOutCallback() {
        mSignOutCallback = new DataCallback() {
            @Override
            public void onGetDataSuccess(Object data) {
                mViewModel.onSignOutSuccess();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onSignOutFailed(msg);
            }
        };
    }

    @Override
    public void signOut() {
        if (mUser == null) {
            return;
        }
        if (mSignOutCallback == null) {
            initSignOutCallback();
        }
        List<String> provinders = mUser.getProviders();
        if (provinders != null && provinders.size() != 0) {
            for (String provinder : provinders) {
                switch (provinder) {
                    case GoogleAuthProvider.PROVIDER_ID:
                        mAuthenicationRepository.signOut(
                                mViewModel.getGoogleApiCliennt(),
                                mSignOutCallback);
                        break;
                    case FacebookAuthProvider.PROVIDER_ID:
                        mAuthenicationRepository.signOut(
                                LoginManager.getInstance(),
                                mSignOutCallback);
                        break;
                    default:
                        mAuthenicationRepository.signOut(mSignOutCallback);
                        break;
                }
            }
        }
    }
}
