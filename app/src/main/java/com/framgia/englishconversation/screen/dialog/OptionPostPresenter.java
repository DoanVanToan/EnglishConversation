package com.framgia.englishconversation.screen.dialog;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.utils.Utils;

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
        timelineModel.setCreatedAt(Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
        mDisposable.add(mTimelineRepository.deleteTimeline(timelineModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<TimelineModel>() {
                    @Override
                    public void onNext(TimelineModel timelineModel) {
                        mDisposable.add(mTimelineRepository.addTimelineToRecycleBin(
                                timelineModel).subscribe());

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
