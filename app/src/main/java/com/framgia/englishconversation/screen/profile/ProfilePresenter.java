package com.framgia.englishconversation.screen.profile;

import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.google.firebase.auth.FirebaseUser;

/**
 * Listens to user actions from the UI ({@link ProfileFragment}), retrieves the data and updates
 * the UI as required.
 */
final class ProfilePresenter implements ProfileContract.Presenter {
    private static final String TAG = ProfilePresenter.class.getName();

    private final ProfileContract.ViewModel mViewModel;
    private AuthenicationRepository mAuthenicationRepository;

    public ProfilePresenter(ProfileContract.ViewModel viewModel,
            AuthenicationRepository authenicationRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
    }

    @Override
    public void onStart() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccesss(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });
    }

    @Override
    public void onStop() {
    }
}
