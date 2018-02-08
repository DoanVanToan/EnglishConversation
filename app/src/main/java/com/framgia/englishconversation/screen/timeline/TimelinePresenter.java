package com.framgia.englishconversation.screen.timeline;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.SettingRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Listens to user actions from the UI ({@link TimelineFragment}), retrieves the data and updates
 * the UI as required.
 */
public abstract class TimelinePresenter implements TimelineContract.Presenter {

    protected final TimelineContract.ViewModel mViewModel;
    protected AuthenicationRepository mAuthenicationRepository;
    protected TimelineRepository mTimelineRepository;
    protected TimelineModel mLastTimelineModel;
    protected CompositeDisposable mDisposable;
    protected SettingRepository mSettingRepository;

    public TimelinePresenter(TimelineContract.ViewModel viewModel,
            AuthenicationRepository authenicationRepository, TimelineRepository timelineRepository,
            SettingRepository settingRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
        mTimelineRepository = timelineRepository;
        mDisposable = new CompositeDisposable();
        mSettingRepository = settingRepository;
        getSetting();
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

    private void getSetting() {
        mViewModel.onGetSettingSuccess(mSettingRepository.getSetting());
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        mTimelineRepository.removeListener();
        mDisposable.dispose();
    }

}
