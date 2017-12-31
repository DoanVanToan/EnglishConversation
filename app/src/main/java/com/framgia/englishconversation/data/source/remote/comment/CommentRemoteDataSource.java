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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VinhTL on 20/12/2017.
 */

public class CommentRemoteDataSource extends BaseFirebaseDataBase implements CommentDataSource {
    private static final int NUM_OF_COMMENT_PER_PAGE = 10;

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
    public void getComment(final CommentCallback callback, final Comment lastComment) {
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
                    comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
                    comment.setId(snapshot.getKey());
                    if (lastComment == null || !lastComment.getId().equals(snapshot.getKey())) {
                        comments.add(comment);
                    }
                }
                callback.onChildAdded(comments, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
