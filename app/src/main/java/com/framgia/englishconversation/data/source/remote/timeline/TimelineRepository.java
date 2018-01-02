package com.framgia.englishconversation.data.source.remote.timeline;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 * Created by toand on 5/13/2017.
 */

public class TimelineRepository {
    private TimelineRemoteDataSource mDataSource;

    public TimelineRepository(TimelineRemoteDataSource dataSource) {
        mDataSource = dataSource;
    }

    public void createNewPost(TimelineModel timelineModel, DataCallback callback) {
        mDataSource.createNewPost(timelineModel, callback);
    }

    public void getTimeline(TimelineRemoteDataSource.TimelineCallback callback,
            TimelineModel timelineModel) {
        mDataSource.getTimeline(callback, timelineModel);
    }
}
