package com.framgia.englishconversation.screen.timeline.usertimeline;

import android.text.TextUtils;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.SettingRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.screen.timeline.TimelineContract;
import com.framgia.englishconversation.screen.timeline.TimelinePresenter;
import com.framgia.englishconversation.screen.timeline.TimelineViewModel;
import com.google.firebase.auth.FirebaseUser;

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
        initAllowCreatePost();
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
                        if (mLastTimelineModel == null) {
                            mLastTimelineModel =
                                    timelineModels.isEmpty() ? null : timelineModels.get(0);
                            registerModifyTimelines(mLastTimelineModel, mTimelineUser);
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

    public void registerModifyTimelines(TimelineModel timelineModel, UserModel userModel) {
        Observable<TimelineModel> observable = mTimelineRepository
                .registerModifyTimelines(timelineModel, userModel);

        Disposable disposable = observable.observeOn(AndroidSchedulers.mainThread())
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
                });
        mDisposable.add(disposable);
    }

    @Override
    protected void initAllowCreatePost() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                ((TimelineViewModel) mViewModel)
                        .setAllowCreatePost(allowCreatePost(new UserModel(data)));
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });
    }


    /**
     * allow show create post button
     * if this screen is timeline main screen, or this screen is current user profile
     */
    public boolean allowCreatePost(UserModel currentUser) {
        if (mTimelineUser == null
                || currentUser == null
                || TextUtils.isEmpty(currentUser.getEmail())) {
            return false;
        }
        return currentUser.getId().equals(mTimelineUser.getId());
    }

}
