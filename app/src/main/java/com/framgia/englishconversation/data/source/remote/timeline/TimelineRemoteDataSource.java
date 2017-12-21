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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by toand on 5/13/2017.
 */

public class TimelineRemoteDataSource extends BaseFirebaseDataBase implements TimelineDataSource {
    private static final String TAG = "TimelineRemote";

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

    /**
     * Do firebase db chỉ có sắp xếp các record theo thứ tự thời gian tăng dần (milisecond)
     * mà các bài post phải xếp theo thứ tự giảm dần nên sẽ nhân thêm -1 để đảo ngược lại
     * danh sách các bản ghi.
     */
    @Override
    public void getTimeline(final TimelineRemoteDataSource.TimelineCallback callback) {
        mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                .addChildEventListener(new ChildEventListener() {
                    @Override

                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                        timeline.setCreatedAt(
                                Utils.generateOppositeNumber(timeline.getCreatedAt()));
                        timeline.setId(dataSnapshot.getKey());
                        callback.onChildAdded(timeline);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                        String commentKey = dataSnapshot.getKey();
                        timeline.setId(dataSnapshot.getKey());
                        timeline.setCreatedAt(
                                Utils.generateOppositeNumber(timeline.getCreatedAt()));
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
