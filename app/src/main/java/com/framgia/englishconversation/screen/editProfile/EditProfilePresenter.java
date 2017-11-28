package com.framgia.englishconversation.screen.editProfile;

import android.net.Uri;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 * Listens to user actions from the UI ({@link EditProfileActivity}), retrieves the data and updates
 * the UI as required.
 */
final class EditProfilePresenter implements EditProfileContract.Presenter {

    private final EditProfileContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;

    public EditProfilePresenter(EditProfileContract.ViewModel viewModel,
                                AuthenicationRepository repository) {
        mViewModel = viewModel;
        mRepository = repository;
    }

    @Override
    public void onStart() {
        mRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccess(data);
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });
    }

    @Override
    public void onStop() {
    }

    @Override
    public void saveUser(String userName, Uri uri) {
        if (TextUtils.isEmpty(userName)) {
            mViewModel.onEmptyUserName();
            return;
        }
        mViewModel.showProgressDialog();
        mRepository.updateProfile(userName, uri, new DataCallback() {
            @Override
            public void onGetDataSuccess(Object data) {
                mViewModel.onSaveUserSuccess();
                mViewModel.hideProgressDialog();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onSaveUserFailed(msg);
                mViewModel.hideProgressDialog();
            }
        });
    }
}
