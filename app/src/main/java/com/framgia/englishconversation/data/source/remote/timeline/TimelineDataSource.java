package com.framgia.englishconversation.data.source.remote.timeline;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 * Created by toand on 5/13/2017.
 */

public interface TimelineDataSource {

    void createNewPost(TimelineModel timelineModel, DataCallback callback);

    void getTimeline(TimelineRemoteDataSource.TimelineCallback callback);

    interface TimelineCallback{

        void onChildAdded(TimelineModel timeline);

        void onChildChanged(TimelineModel timeline, String commentKey);

        void onChildRemoved(TimelineModel timeline, String commentKey);

        void onChildMoved(TimelineModel timeline, String commentKey);

        void onCancelled(String message);
    }
}
