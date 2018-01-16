package com.framgia.englishconversation.data.source.remote.timeline;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import io.reactivex.Observable;
import java.util.List;

/**
 * Created by toand on 5/13/2017.
 */

public class TimelineRepository {
    private TimelineRemoteDataSource mDataSource;

    public TimelineRepository(TimelineRemoteDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Observable<TimelineModel> createNewPost(TimelineModel timelineModel) {
        return mDataSource.createNewPost(timelineModel);
    }

    public Observable<List<TimelineModel>> getTimeline(TimelineModel timelineModel) {
        return mDataSource.getTimeline(timelineModel);
    }

    public Observable<TimelineModel> registerModifyTimelines(TimelineModel timelineModel) {
        return mDataSource.updateTimeline(timelineModel);
    }

    public Observable<List<TimelineModel>> getTimeline(TimelineModel timelineModel,
            UserModel userModel) {
        return mDataSource.getTimeline(timelineModel, userModel);
    }

    public Observable<TimelineModel> registerModifyTimelines(TimelineModel timelineModel,
            UserModel userModel) {
        return mDataSource.updateTimeline(timelineModel, userModel);
    }
}
