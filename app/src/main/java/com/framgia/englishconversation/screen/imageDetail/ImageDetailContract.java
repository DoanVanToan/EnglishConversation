package com.framgia.englishconversation.screen.imagedetail;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ImageDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
    }
}
