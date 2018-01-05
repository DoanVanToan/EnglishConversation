package com.framgia.englishconversation.screen.imagedetail;

import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.google.firebase.auth.FirebaseUser;

/**
 * Listens to user actions from the UI ({@link ImageDetailActivity}), retrieves the data and updates
 * the UI as required.
 */
final class ImageDetailPresenter implements ImageDetailContract.Presenter {

    private final ImageDetailContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;

    public ImageDetailPresenter(AuthenicationRepository repository,
            ImageDetailContract.ViewModel viewModel) {
        mViewModel = viewModel;
        mRepository = repository;
    }

    @Override
    public void onStart() {
        mRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetCurrentUserSuccess(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetCurrentUserFailed(msg);
            }
        });
    }

    @Override
    public void onStop() {
    }
}
