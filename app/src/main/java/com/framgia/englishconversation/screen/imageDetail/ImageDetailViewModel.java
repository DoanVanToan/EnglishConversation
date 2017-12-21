package com.framgia.englishconversation.screen.imageDetail;

/**
 * Exposes the data to be used in the ImageDetail screen.
 */

public class ImageDetailViewModel implements ImageDetailContract.ViewModel {

    private ImageDetailContract.Presenter mPresenter;

    public ImageDetailViewModel() {
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(ImageDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
