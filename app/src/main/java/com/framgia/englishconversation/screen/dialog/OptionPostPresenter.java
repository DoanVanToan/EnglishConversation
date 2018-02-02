package com.framgia.englishconversation.screen.dialog;

import com.framgia.englishconversation.data.model.Status;
import com.framgia.englishconversation.data.model.StatusModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.utils.Utils;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sony on 1/25/2018.
 */

public class OptionPostPresenter implements OptionPostContract.Presenter {

    private final OptionPostContract.ViewModel mViewModel;
    private TimelineRepository mTimelineRepository;
    private CompositeDisposable mDisposable;

    public OptionPostPresenter(OptionPostContract.ViewModel viewModel,
                               TimelineRepository timelineRepository) {
        mViewModel = viewModel;
        mTimelineRepository = timelineRepository;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void deletePost(TimelineModel timelineModel) {
        StatusModel statusModel = new StatusModel(timelineModel.getCreatedUser(),
                Utils.generateOppositeNumber(Calendar.getInstance().getTimeInMillis()),
                Status.DELETE);
        timelineModel.setCreatedAt(Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
        timelineModel.setStatusModel(statusModel);
        mDisposable.add(mTimelineRepository.updateTimeline(timelineModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TimelineModel>() {
                    @Override
                    public void onNext(TimelineModel timelineModel) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    @Override
    public void editPost(TimelineModel timelineModel) {

    }
}
