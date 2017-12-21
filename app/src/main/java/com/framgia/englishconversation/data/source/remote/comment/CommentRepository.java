package com.framgia.englishconversation.data.source.remote.comment;

import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 * Created by VinhTL on 20/12/2017.
 */

public class CommentRepository implements CommentDataSource {
    private CommentRemoteDataSource mDataSource;

    public CommentRepository(CommentRemoteDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public void createNewComment(Comment comment, DataCallback callback) {
        mDataSource.createNewComment(comment, callback);
    }

    @Override
    public void getComment(CommentCallback callback) {
        mDataSource.getComment(callback);
    }
}
