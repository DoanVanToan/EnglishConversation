package com.framgia.englishconversation.screen.selectedimagedetail;

/**
 * Listens to user actions from the UI ({@link SelectedImageDetailActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class SelectedImageDetailPresenter implements SelectedImageDetailContract.Presenter {
    private static final String TAG = SelectedImageDetailPresenter.class.getName();

    private final SelectedImageDetailContract.ViewModel mViewModel;

    SelectedImageDetailPresenter(SelectedImageDetailContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
