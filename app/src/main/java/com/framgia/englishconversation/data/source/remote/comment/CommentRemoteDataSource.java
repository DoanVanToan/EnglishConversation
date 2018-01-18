package com.framgia.englishconversation.data.source.remote.comment;

import android.support.annotation.NonNull;
import com.framgia.englishconversation.data.model.Comment;
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
 * Created by VinhTL on 20/12/2017.
 */

public class CommentRemoteDataSource extends BaseFirebaseDataBase implements CommentDataSource {
    private static final int NUM_OF_COMMENT_PER_PAGE = 10;
    private ChildEventListener mUpdateComment;

    public CommentRemoteDataSource(String timelineId) {
        super(Constant.DatabaseTree.COMMENT);
        mReference = mReference.child(timelineId);
    }

    @Override
    public Observable<Comment> createNewComment(final Comment comment) {
        return Observable.create(new ObservableOnSubscribe<Comment>() {
            @Override
            public void subscribe(
                    @io.reactivex.annotations.NonNull final ObservableEmitter<Comment> emitter)
                    throws Exception {
                mReference.push()
                        .setValue(comment)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                emitter.onNext(comment);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                emitter.onError(e);
                            }
                        });
            }
        });
    }

    @Override
    public Observable<List<Comment>> getComment(final Comment lastComment) {
        return Observable.create(new ObservableOnSubscribe<List<Comment>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Comment>> e) throws Exception {
                final Query query;
                if (lastComment == null) {
                    query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                            .limitToFirst(NUM_OF_COMMENT_PER_PAGE);
                } else {
                    query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                            .startAt(-lastComment.getCreatedAt(), lastComment.getId())
                            .limitToFirst(NUM_OF_COMMENT_PER_PAGE);
                }
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Comment> comments = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Comment comment = snapshot.getValue(Comment.class);
                            comment.setCreatedAt(
                                    Utils.generateOppositeNumber(comment.getCreatedAt()));
                            comment.setId(snapshot.getKey());
                            if (lastComment == null || !lastComment.getId()
                                    .equals(snapshot.getKey())) {
                                comments.add(comment);
                            }
                        }
                        e.onNext(comments);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(databaseError.toException());
                    }
                });
            }
        });
    }

    public Observable<Comment> registerModifyTimelines(final Comment lastComment) {
        return Observable.create(new ObservableOnSubscribe<Comment>() {
            @Override
            public void subscribe(final ObservableEmitter<Comment> e) throws Exception {
                final Query query = mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                        .endAt(lastComment != null ? -lastComment.getCreatedAt()
                                : -Calendar.getInstance().getTimeInMillis());
                mUpdateComment = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (lastComment != null && lastComment.getId()
                                .equals(dataSnapshot.getKey())) {
                            return;
                        }
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setId(dataSnapshot.getKey());
                        e.onNext(comment);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setModifiedAt(
                                Utils.generateOppositeNumber(comment.getModifiedAt()));
                        comment.setId(dataSnapshot.getKey());
                        e.onNext(comment);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setId(dataSnapshot.getKey());
                        e.onNext(comment);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setId(dataSnapshot.getKey());
                        e.onNext(comment);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        e.onError(databaseError.toException());
                    }
                };
                query.addChildEventListener(mUpdateComment);
            }
        });
    }

    @Override
    public void removeListener() {
        if (mUpdateComment == null) {
            return;
        }
        mReference.removeEventListener(mUpdateComment);
    }
}
