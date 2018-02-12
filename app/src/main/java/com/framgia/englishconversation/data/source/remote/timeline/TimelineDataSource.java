package com.framgia.englishconversation.data.source.remote.timeline;

import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;

import io.reactivex.Observable;

import java.util.List;

/**
 * Created by toand on 5/13/2017.
 */

public interface TimelineDataSource {

    /**
     * Create a new post
     * @param timelineModel     The timeline want to create
     * @return
     */
    Observable<TimelineModel> createNewPost(TimelineModel timelineModel);

    /**
     * Get all post in
     * @param lastTimeline  The last timeline for paging
     * @return
     */
    Observable<List<TimelineModel>> getTimeline(TimelineModel lastTimeline);

    /**
     * Get all post in timeline of user model
     * @param lastTimeline  The last timeline for paging
     * @param userModel     The user want to get post
     * @return
     */

    Observable<List<TimelineModel>> getTimeline(TimelineModel lastTimeline, UserModel userModel);

    /**
     * Register all timeline changed listenner
     * @param lastTimeline  The last timeline want to register
     * @return
     */
    Observable<TimelineModel> registerModifyTimeline(TimelineModel lastTimeline);

    /**
     * Register all timeline changed listenner
     * @param lastTimeline  The last timeline want to register
     * @param userModel     The user profile want to register
     * @return
     */
    Observable<TimelineModel> registerModifyTimeline(TimelineModel lastTimeline,
                                                     UserModel userModel);

    /**
     * Update current timeline
     * @param timelineModel     the timeline need to be updated
     * @return
     */
    Observable<TimelineModel> updateTimeline(TimelineModel timelineModel);

    /**
     * Add a timeline to revision tree to  storage
     * @param timelineModel
     * @return
     */
    Observable<TimelineModel> addRevisionTimeline(TimelineModel timelineModel);

    /**
     * Remove register listenner
     */
    void removeListener();

    /**
     * get post which was choose by editor
     * @param lastTimeline  The last timeline for paging
     * @return
     */
    Observable<List<TimelineModel>> getEditorChoiseTimeline(TimelineModel lastTimeline);


    /**
     * Register post was choose by editor choose changed listenner
     * @param lastTimeline  The last timeline want to register
     * @return
     */
    Observable<TimelineModel> registerModifyEditorChoiseTimeline(TimelineModel lastTimeline);
}
