package com.framgia.englishconversation.data.source.remote.comment;

import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import java.util.List;

/**
 * Created by VinhTL on 20/12/2017.
 */

public interface CommentDataSource {
    void createNewComment(Comment comment, DataCallback callback);

    void getComment(CommentCallback callback, Comment lastComment);

    interface CommentCallback {

        void onChildAdded(List<Comment> comments, boolean isLoadingMore);

        void onChildChanged(Comment comment);

        void onChildRemoved(Comment comment);

        void onChildMoved(Comment comment);

        void onCancelled(String message);
    }
}
