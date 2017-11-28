package com.framgia.englishconversation.data.source.remote.timeline;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.utils.Constant;

/**
 * Created by toand on 5/13/2017.
 */

public class TimelineRemoteDataSource extends BaseFirebaseDataBase implements TimelineDataSource {
    private static final String TAG = "TimelineRemote";

    public TimelineRemoteDataSource() {
        super(Constant.Timeline.POST);
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
    public void getTimeline(final TimelineRemoteDataSource.TimelineCallback callback) {
        mReference
            .addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                    timeline.setId(dataSnapshot.getKey());
                    callback.onChildAdded(timeline);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                    String commentKey = dataSnapshot.getKey();
                    timeline.setId(dataSnapshot.getKey());
                    callback.onChildChanged(timeline, commentKey);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                    String commentKey = dataSnapshot.getKey();
                    timeline.setId(dataSnapshot.getKey());
                    callback.onChildRemoved(timeline, commentKey);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                    String commentKey = dataSnapshot.getKey();
                    timeline.setId(dataSnapshot.getKey());
                    callback.onChildMoved(timeline, commentKey);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onCancelled(databaseError.getMessage());
                }
            });
    }
}
