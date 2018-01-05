package com.framgia.englishconversation.data.source.remote.timeline;

import android.support.annotation.NonNull;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.remote.BaseFirebaseDataBase;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.ArrayList;
import java.util.Calendar;
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
    public Observable<TimelineModel> createNewPost(final TimelineModel timelineModel) {
        return Observable.create(new ObservableOnSubscribe<TimelineModel>() {
            @Override
            public void subscribe(final ObservableEmitter<TimelineModel> observableEmitter)
                    throws Exception {
                mReference.push()
                        .setValue(timelineModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    observableEmitter.onNext(timelineModel);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                observableEmitter.onError(e);
                            }
                        });
            }
        });
    }

    @Override
    public Observable<List<TimelineModel>> getTimeline(final TimelineModel lastTimeline) {
        return Observable.create(new ObservableOnSubscribe<List<TimelineModel>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<TimelineModel>> e) throws Exception {
                final Query query;
                if (lastTimeline == null) {
                    query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                            .limitToFirst(NUM_OF_TIMELINE_PER_PAGE);
                } else {
                    query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                            .startAt(-lastTimeline.getCreatedAt(), lastTimeline.getId())
                            .limitToFirst(NUM_OF_TIMELINE_PER_PAGE);
                }

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<TimelineModel> timelineModels = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            TimelineModel timelineModel = snapshot.getValue(TimelineModel.class);
                            timelineModel.setCreatedAt(
                                    Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
                            timelineModel.setId(snapshot.getKey());
                            if (lastTimeline == null || !lastTimeline.getId()
                                    .equals(snapshot.getKey())) {
                                timelineModels.add(timelineModel);
                            }
                        }
                        e.onNext(timelineModels);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(databaseError.toException());
                    }
                });
            }
        });
    }

    @Override
    public Observable<TimelineModel> updateTimeline(final TimelineModel lastTimeline) {
        return Observable.create(new ObservableOnSubscribe<TimelineModel>() {
            @Override
            public void subscribe(final ObservableEmitter<TimelineModel> e) throws Exception {
                final Query query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                        .endAt(lastTimeline != null ? -lastTimeline.getCreatedAt()
                                : -Calendar.getInstance().getTimeInMillis());

                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (lastTimeline != null && lastTimeline.getId()
                                .equals(dataSnapshot.getKey())) {
                            return;
                        }
                        TimelineModel timelineModel = dataSnapshot.getValue(TimelineModel.class);
                        timelineModel.setCreatedAt(
                                Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
                        timelineModel.setId(dataSnapshot.getKey());
                        e.onNext(timelineModel);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        TimelineModel timelineModel = dataSnapshot.getValue(TimelineModel.class);
                        timelineModel.setCreatedAt(
                                Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
                        timelineModel.setModifiedAt(
                                Utils.generateOppositeNumber(timelineModel.getModifiedAt()));
                        timelineModel.setId(dataSnapshot.getKey());
                        e.onNext(timelineModel);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        TimelineModel timelineModel = dataSnapshot.getValue(TimelineModel.class);
                        timelineModel.setCreatedAt(
                                Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
                        timelineModel.setId(dataSnapshot.getKey());
                        e.onNext(timelineModel);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        TimelineModel timelineModel = dataSnapshot.getValue(TimelineModel.class);
                        timelineModel.setCreatedAt(
                                Utils.generateOppositeNumber(timelineModel.getCreatedAt()));
                        timelineModel.setId(dataSnapshot.getKey());
                        e.onNext(timelineModel);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(databaseError.toException());
                    }
                });
            }
        });
    }
}
