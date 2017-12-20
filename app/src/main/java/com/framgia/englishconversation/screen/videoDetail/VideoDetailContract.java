package com.framgia.englishconversation.screen.videoDetail;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface VideoDetailContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onPause();

        void onResume();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onPause();

        void onResume();
    }
}
