package com.framgia.englishconversation.screen.conversationdetail;

import com.framgia.englishconversation.data.model.ConversationModel;
import com.framgia.englishconversation.data.model.TimelineModel;

/**
 * Created by fs-sournary.
 * Date on 12/27/17.
 * Description:
 */

public class ConversationDetailViewModel implements ConversationDetailContract.ViewModel {

    private ConversationDetailAdapter mAdapter;
    private ConversationDetailContract.Presenter mPresenter;
    private ConversationDetailActivity mActivitiy;
    private TimelineModel mTimelineModel;

    public ConversationDetailViewModel(ConversationDetailActivity activitiy,
                                       TimelineModel timelineModel) {
        mActivitiy = activitiy;
        mTimelineModel = timelineModel;
        mAdapter = new ConversationDetailAdapter(
                activitiy, timelineModel.getConversations(), this);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(ConversationDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onAudioConversationClick(ConversationModel conversationModel) {

    }

    public ConversationDetailAdapter getAdapter() {
        return mAdapter;
    }

    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

}
