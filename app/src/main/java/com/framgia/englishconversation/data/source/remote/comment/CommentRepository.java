package com.framgia.englishconversation.data.source.remote.comment;

import com.framgia.englishconversation.data.model.Comment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by VinhTL on 20/12/2017.
 */

public class CommentRepository implements CommentDataSource {
    private CommentRemoteDataSource mDataSource;

    public CommentRepository(CommentRemoteDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public Observable<Comment> createNewComment(Comment comment) {
        return mDataSource.createNewComment(comment);
    }

    @Override
    public Observable<List<Comment>> getComment(Comment comment) {
        return mDataSource.getComment(comment);
    }

    public Observable<Comment> registerModifyTimelines(Comment lastComment) {
        return mDataSource.registerModifyTimelines(lastComment);
    }

    public Observable<Comment> updateComment(Comment comment) {
        return mDataSource.updateComment(comment);
    }

    @Override
    public void removeListener() {
        mDataSource.removeListener();
    }

    @Override
    public void saveRevisionComment(Comment comment) {
        mDataSource.saveRevisionComment(comment);
    }
}
