package com.framgia.englishconversation.data.source.remote.timeline;

import android.support.annotation.NonNull;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.BaseFirebaseDataBase;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toand on 5/13/2017.
 */

public class TimelineRemoteDataSource extends BaseFirebaseDataBase implements TimelineDataSource {
    private static final int NUM_OF_TIMELINE_PER_PAGE = 10;

    public TimelineRemoteDataSource() {
        super(Constant.DatabaseTree.POST);
    }

    @Override
    public void createNewPost(TimelineModel timelineModel, final DataCallback callback) {
        mReference.push()
                .setValue(timelineModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onGetDataSuccess(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetDataFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void getTimeline(final TimelineCallback callback, final TimelineModel lastTimeline) {

        final Query query;
        if (lastTimeline == null) {
            query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                    .limitToFirst(NUM_OF_TIMELINE_PER_PAGE);
        } else {
            query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                    .startAt(-lastTimeline.getCreatedAt(), lastTimeline.getId())
                    .limitToFirst(NUM_OF_TIMELINE_PER_PAGE);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TimelineModel> timelineModels = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TimelineModel timelineModel = snapshot.getValue(TimelineModel.class);
                    timelineModel.setCreatedAt(
                            Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
                    timelineModel.setId(snapshot.getKey());
                    if (lastTimeline == null || !lastTimeline.getId().equals(snapshot.getKey())) {
                        timelineModels.add(timelineModel);
                    }
                }
                callback.onChildAdded(timelineModels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
