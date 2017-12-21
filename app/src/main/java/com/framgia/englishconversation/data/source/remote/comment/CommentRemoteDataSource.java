package com.framgia.englishconversation.data.source.remote.comment;

import android.support.annotation.NonNull;
import com.framgia.englishconversation.data.model.Comment;
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
 * Created by VinhTL on 20/12/2017.
 */

public class CommentRemoteDataSource extends BaseFirebaseDataBase implements CommentDataSource {

    public CommentRemoteDataSource() {
        super(Constant.DatabaseTree.COMMENT);
    }

    @Override
    public void createNewComment(Comment comment, final DataCallback callback) {
        mReference.push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onGetDataSuccess(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onGetDataFailed(e.getMessage());
            }
        });
    }

    @Override
    public void getComment(final CommentCallback callback) {
        mReference.orderByChild(Constant.DatabaseTree.CREATED_AT)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setId(dataSnapshot.getKey());
                        callback.onChildAdded(comment);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                        comment.setId(dataSnapshot.getKey());
                        callback.onChildChanged(comment);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setId(dataSnapshot.getKey());
                        callback.onChildRemoved(comment);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comment.setId(dataSnapshot.getKey());
                        callback.onChildMoved(comment);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onCancelled(databaseError.getMessage());
                    }
                });
    }
}
