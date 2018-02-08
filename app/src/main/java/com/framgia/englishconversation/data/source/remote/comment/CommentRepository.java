package com.framgia.englishconversation.data.source.remote.comment;

import com.framgia.englishconversation.data.model.Comment;

import java.util.HashMap;
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

    public Observable<HashMap<Integer, Comment>> registerModifyTimelines(Comment lastComment) {
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
    public Observable<Comment> saveRevisionComment(Comment comment) {
       return mDataSource.saveRevisionComment(comment);
    }

    @Override
    public Observable<Comment> deleteComment(Comment comment) {
        return mDataSource.deleteComment(comment);
    }

    @Override
    public Observable<Comment> saveCommentDelete(Comment comment) {
      return   mDataSource.saveCommentDelete(comment);
    }
}
