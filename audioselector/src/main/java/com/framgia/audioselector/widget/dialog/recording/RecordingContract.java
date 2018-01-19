package com.framgia.audioselector.widget.dialog.recording;

import com.framgia.audioselector.BasePresenter;
import com.framgia.audioselector.BaseViewModel;

/**
 * Created by fs-sournary.
 * Data: 1/19/18.
 * Description:
 */

public interface RecordingContract {

    /**
     * Presenter for RecordingPresenter
     */
    interface Presenter extends BasePresenter {

    }

    /**
     * ViewModel for RecordingViewModel
     */
    interface ViewModel extends BaseViewModel<Presenter> {

    }
}
