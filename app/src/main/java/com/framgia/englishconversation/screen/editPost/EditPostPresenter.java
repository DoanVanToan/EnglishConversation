package com.framgia.englishconversation.screen.editPost;

import android.text.TextUtils;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.utils.Utils;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sony on 2/2/2018.
 */

public class EditPostPresenter implements EditPostContract.Presenter {
    private final EditPostContract.ViewModel mViewModel;
    private AuthenicationRepository mAuthenicationRepository;
    private TimelineRepository mTimelineRepository;
    private CompositeDisposable mDisposable;
    private TimelineModel mTimelineModel;
    private TimelineModel mTimelineModelRevision;

    public EditPostPresenter(EditPostContract.ViewModel viewModel,
                               AuthenicationRepository authenicationRepository,
                               TimelineRepository timelineRepository,
                             TimelineModel timelineModel) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
        mTimelineRepository = timelineRepository;
        mDisposable = new CompositeDisposable();
        mTimelineModel = timelineModel;
        try {
            mTimelineModelRevision = (TimelineModel) timelineModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUser() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetCurrentUserSuccess(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetCurrentUserFailed(msg);
            }
        });
    }

    @Override
    public void submitPost(TimelineModel timelineModel) {
        if ((timelineModel.getContent() == null || TextUtils.isEmpty(
                timelineModel.getContent().trim()))
                && (timelineModel.getMedias() == null
                || timelineModel.getMedias().size() == 0)
                && timelineModel.getConversations() == null) {
            return;
        }
        timelineModel.setCreatedUser(mTimelineModelRevision.getCreatedUser());
        timelineModel.setCreatedAt(Utils.generateOppositeNumber(
                mTimelineModelRevision.getCreatedAt()));
        mDisposable.add(mTimelineRepository.addRevisionTimeline(mTimelineModelRevision)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe());
        mDisposable.add(mTimelineRepository.updateTimeline(timelineModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TimelineModel>() {
                    @Override
                    public void onNext(@NonNull TimelineModel timelineModel) {
                        mViewModel.onSubmitPostSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mViewModel.onSubmitPostFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }

    @Override
    public void onDestroy() {
        mDisposable.dispose();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }
}
