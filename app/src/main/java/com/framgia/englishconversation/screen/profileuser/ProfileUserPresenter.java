package com.framgia.englishconversation.screen.profileuser;

/**
 * Listens to user actions from the UI ({@link ProfileUserActivity}), retrieves the data and updates
 * the UI as required.
 */
final class ProfileUserPresenter implements ProfileUserContract.Presenter {
    private static final String TAG = ProfileUserPresenter.class.getName();

    private final ProfileUserContract.ViewModel mViewModel;

    public ProfileUserPresenter(ProfileUserContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
