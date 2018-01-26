package com.framgia.englishconversation.screen.timeline;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.SettingRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.google.firebase.auth.FirebaseUser;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link TimelineFragment}), retrieves the data and updates
 * the UI as required.
 */
final class TimelinePresenter implements TimelineContract.Presenter {

    private final TimelineContract.ViewModel mViewModel;
    private AuthenicationRepository mAuthenicationRepository;
    private TimelineRepository mTimelineRepository;
    private TimelineModel mLastTimelineModel;
    private CompositeDisposable mDisposable;
    private SettingRepository mSettingRepository;

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
    public void fetchTimelineData(final TimelineModel timelineModel, final UserModel userModel) {
        Observable<List<TimelineModel>> observable = mTimelineRepository.getTimeline(timelineModel);
        if (userModel != null) {
            observable = mTimelineRepository.getTimeline(timelineModel, userModel);
        }
        mDisposable.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<TimelineModel>>() {
                    @Override
                    public void onNext(List<TimelineModel> timelineModels) {
                        mViewModel.onGetTimelinesSuccess(timelineModels);
                        if (mLastTimelineModel == null) {
                            mLastTimelineModel =
                                    timelineModels.isEmpty() ? null : timelineModels.get(0);
                            registerModifyTimelines(mLastTimelineModel, userModel);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetTimelinesFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void onDestroy() {
        mTimelineRepository.removeListener();
    }

    public void registerModifyTimelines(TimelineModel timelineModel, UserModel userModel) {
        Observable<TimelineModel> observable =
                mTimelineRepository.registerModifyTimelines(timelineModel);
        if (userModel != null) {
            observable = mTimelineRepository.registerModifyTimelines(timelineModel, userModel);
        }
        mDisposable.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TimelineModel>() {
                    @Override
                    public void onNext(TimelineModel timelineModel) {
                        mViewModel.onGetTimelineSuccess(timelineModel);
                        if (mLastTimelineModel == null) {
                            mLastTimelineModel = timelineModel;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetTimelinesFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
