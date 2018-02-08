package com.framgia.englishconversation.screen.timeline;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.Setting;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface TimelineContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetUserSuccess(UserModel data);

        void onCreateNewPostClick();

        void onGetTimelinesSuccess(List<TimelineModel> timelines);

        void onGetTimelinesFailure(String message);

        void onGetTimelineSuccess(TimelineModel timelineModel);

        void onDestroy();

        void onGetSettingSuccess(Setting setting);

        void setTimelineUser(UserModel timelineUser);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onDestroy();

        void getTimelineData(TimelineModel lastTimelineModel);
    }
}
