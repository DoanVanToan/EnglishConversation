package com.framgia.englishconversation.screen.timeline.usertimeline;

import com.framgia.englishconversation.data.model.GenericsModel;
import com.framgia.englishconversation.data.model.Status;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.SettingRepository;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.screen.timeline.TimelineContract;
import com.framgia.englishconversation.screen.timeline.TimelinePresenter;
import com.framgia.englishconversation.screen.timeline.TimelineViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by doan.van.toan on 2/8/18.
 */

public class UserTimelinePresenter extends TimelinePresenter {
    private UserModel mTimelineUser;

    public UserTimelinePresenter(TimelineContract.ViewModel viewModel,
                                 AuthenicationRepository authenicationRepository,
                                 TimelineRepository timelineRepository,
                                 SettingRepository settingRepository,
                                 UserModel userModel) {
        super(viewModel, authenicationRepository, timelineRepository, settingRepository);
        mTimelineUser = userModel;
        mViewModel.setTimelineUser(mTimelineUser);
    }

    @Override
    protected void initAllowCreatePost() {
        ((TimelineViewModel) mViewModel).setAllowCreatePost(true);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getTimelineData(TimelineModel lastTimelineModel) {
        Observable<List<TimelineModel>> observable =
                mTimelineRepository.getTimeline(lastTimelineModel, mTimelineUser);

        mDisposable.add(observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<TimelineModel>>() {
                    @Override
                    public void onNext(List<TimelineModel> timelineModels) {
                        mViewModel.onGetTimelinesSuccess(timelineModels);

                        mLastTimelineModel = timelineModels.isEmpty() ? null
                                : timelineModels.get(timelineModels.size() - 1);
                        mTimelineRepository.removeListener();
                        registerModifyTimelines(mLastTimelineModel, mTimelineUser);

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

    public void registerModifyTimelines(TimelineModel timelineModel, UserModel userModel) {
        Observable<GenericsModel<Integer, TimelineModel>> observable = mTimelineRepository
                .registerModifyTimelines(timelineModel, userModel);

        Disposable disposable = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<GenericsModel<Integer, TimelineModel>>() {
                    @Override
                    public void onNext(GenericsModel<Integer, TimelineModel> genericsModel) {
                        if (mLastTimelineModel == null) {
                            mLastTimelineModel = genericsModel.getValue();
                        }
                        if (genericsModel.getKey() == Status.ADD) {
                            mViewModel.onAddTimeline(genericsModel.getValue());
                        }
                        if (genericsModel.getKey() == Status.EDIT) {
                            mViewModel.onEditTimeline(genericsModel.getValue());
                        }
                        if (genericsModel.getKey() == Status.DELETE) {
                            mViewModel.onDeleteTimeline(genericsModel.getValue());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetTimelinesFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposable.add(disposable);
    }
}