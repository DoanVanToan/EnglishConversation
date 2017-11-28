package com.framgia.englishconversation.screen.timeline;

import android.util.Log;
import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;

/**
 * Listens to user actions from the UI ({@link TimelineFragment}), retrieves the data and updates
 * the UI as required.
 */
final class TimelinePresenter implements TimelineContract.Presenter,
        TimelineDataSource.TimelineCallback {
    private static final String TAG = TimelinePresenter.class.getName();

    private final TimelineContract.ViewModel mViewModel;
    private AuthenicationRepository mAuthenicationRepository;
    private TimelineRepository mTimelineRepository;

    public TimelinePresenter(TimelineContract.ViewModel viewModel,
            AuthenicationRepository authenicationRepository,
            TimelineRepository timelineRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
        mTimelineRepository = timelineRepository;
        mTimelineRepository.getTimeline(this);
    }

    @Override
    public void onStart() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccess(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onChildAdded(TimelineModel timeline) {
        mViewModel.onChildAdded(timeline);
        Log.d(TAG, "onChildAdded: "+timeline.toString());
    }

    @Override
    public void onChildChanged(TimelineModel timeline, String commentKey) {

    }

    @Override
    public void onChildRemoved(TimelineModel timeline, String commentKey) {

    }

    @Override
    public void onChildMoved(TimelineModel timeline, String commentKey) {

    }

    @Override
    public void onCancelled(String message) {

    }
}
