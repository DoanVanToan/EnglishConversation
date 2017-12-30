package com.framgia.englishconversation.screen.conversationdetail;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;

/**
 * Created by fs-sournary.
 * Date on 12/27/17.
 * Description:
 */

public interface ConversationDetailContract {

    /**
     * base presenter for ConversationDetailPresenter
     */
    interface Presenter extends BasePresenter {

    }

    /**
     * base presenter for ConversationDetailViewModel
     */
    interface ViewModel extends BaseViewModel<Presenter> {

        void onResume();

        void onPause();
    }
}
