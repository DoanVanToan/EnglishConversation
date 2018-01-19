package com.framgia.audioselector.widget.dialog.recording;

/**
 * Created by fs-sournary.
 * Data: 1/19/18.
 * Description:
 */

public class RecordingViewModel implements RecordingContract.ViewModel {

    private RecordingContract.Presenter mPresenter;

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(RecordingContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
