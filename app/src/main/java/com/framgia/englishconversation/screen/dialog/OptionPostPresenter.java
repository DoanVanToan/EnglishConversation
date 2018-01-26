package com.framgia.englishconversation.screen.dialog;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;

/**
 * Created by Sony on 1/25/2018.
 */

public class OptionPostPresenter implements OptionPostContract.Presenter {

    private final OptionPostContract.ViewModel mViewModel;
    private TimelineRepository mTimelineRepository;

    public OptionPostPresenter(OptionPostContract.ViewModel viewModel,
                               TimelineRepository timelineRepository) {
        mViewModel = viewModel;
        mTimelineRepository = timelineRepository;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void updateViewTypePost(TimelineModel timelineModel) {

    }

    @Override
    public void editPost(TimelineModel timelineModel) {

    }
}
