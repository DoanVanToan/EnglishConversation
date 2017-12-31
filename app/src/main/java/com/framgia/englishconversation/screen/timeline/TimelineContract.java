package com.framgia.englishconversation.screen.timeline;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface TimelineContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetUserSuccess(UserModel data);

        void onCreateNewPostClick();

        void onChildAdded(List<TimelineModel> timelines);

        void onCancelled(String message);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void fetchTimelineData(TimelineModel timelineModel);
    }
}
