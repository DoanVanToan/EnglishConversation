package com.framgia.englishconversation.data.source.remote.timeline;

import android.support.annotation.NonNull;
import android.util.Log;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.BaseFirebaseDataBase;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.OnEndScrollListener;
import com.framgia.englishconversation.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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
    private long mCurrentLastTimelineCreatedDate;
    private String mCurrentLastTimelineId;
    private static final int NUM_OF_TIMELINE_PER_PAGE = 10;
    private OnEndScrollListener mOnEndScrollListener;

    public TimelineRemoteDataSource() {
        super(Constant.DatabaseTree.POST);
    }

    public void setOnEndScrollListener(OnEndScrollListener onEndScrollListener) {
        mOnEndScrollListener = onEndScrollListener;
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
        final List<TimelineModel> timelineModels = new ArrayList<>();
        final Query query;
        //Sự kiện lần đầu get data từ timeline
        if (mCurrentLastTimelineId == null) {
            query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                    .limitToFirst(NUM_OF_TIMELINE_PER_PAGE);
        } else {
            //Sự kiện get data từ lần thứ hai trở đi, trường hợp nếu nhiều timeline có cùng
            //thời gian timelineId sẽ được sử dụng để tìm kiếm timeline.
            query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                    .startAt(mCurrentLastTimelineCreatedDate, mCurrentLastTimelineId)
                    .limitToFirst(NUM_OF_TIMELINE_PER_PAGE);
        }
        //listener lắng nghe sự kiện đã lấy được hết các data từ lần query hiện tại.
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                query.removeEventListener(this);
                callback.onChildAdded(timelineModels);
                mOnEndScrollListener.setIsFetchingData(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (mCurrentLastTimelineId != null && dataSnapshot.getKey()
                        .equals(mCurrentLastTimelineId)) {
                    return;
                }
                Log.d("wtf", dataSnapshot.getKey());
                TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                timeline.setCreatedAt(Utils.generateOppositeNumber(timeline.getCreatedAt()));
                timeline.setId(dataSnapshot.getKey());
                timelineModels.add(timeline);
                mCurrentLastTimelineId = dataSnapshot.getKey();
                mCurrentLastTimelineCreatedDate =
                        dataSnapshot.getValue(TimelineModel.class).getCreatedAt();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TimelineModel timeline = dataSnapshot.getValue(TimelineModel.class);
                String commentKey = dataSnapshot.getKey();
                timeline.setId(dataSnapshot.getKey());
                timeline.setCreatedAt(Utils.generateOppositeNumber(timeline.getCreatedAt()));
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
