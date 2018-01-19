package com.framgia.audioselector.screen.audioselector;

import com.framgia.audioselector.BasePresenter;
import com.framgia.audioselector.BaseViewModel;

/**
 * Created by fs-sournary.
 * Data: 1/19/18.
 * Description:
 */

public interface AudioSelectorContract {

    /**
     * Presenter for AudioSelectorPresenter
     */
    interface Presenter extends BasePresenter {

    }

    /**
     * ViewModel for AudioSelectorViewModel
     */
    interface ViewModel extends BaseViewModel<Presenter> {

    }
}
