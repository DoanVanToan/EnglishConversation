package com.framgia.englishconversation.screen.videoDetail;

/**
 * Listens to user actions from the UI ({@link VideoDetailActivity}), retrieves the data and updates
 * the UI as required.
 */
final class VideoDetailPresenter implements VideoDetailContract.Presenter {
    private final VideoDetailContract.ViewModel mViewModel;

    public VideoDetailPresenter(VideoDetailContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
