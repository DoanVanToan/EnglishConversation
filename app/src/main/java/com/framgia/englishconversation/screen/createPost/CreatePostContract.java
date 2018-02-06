package com.framgia.englishconversation.screen.createPost;

import com.framgia.englishconversation.screen.basePost.BasePostContract;

/**
 * This specifies the contract between the view and the presenter.
 */
interface CreatePostContract {
    /**
     * View.
     */
    interface ViewModel extends BasePostContract.ViewModel {

    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePostContract.Presenter {

    }
}
