package com.framgia.englishconversation.screen.imagedetail;

/**
 * Listens to user actions from the UI ({@link ImageDetailActivity}), retrieves the data and updates
 * the UI as required.
 */
final class ImageDetailPresenter implements ImageDetailContract.Presenter {

    private final ImageDetailContract.ViewModel mViewModel;

    public ImageDetailPresenter(ImageDetailContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
