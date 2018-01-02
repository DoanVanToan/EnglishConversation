package com.framgia.englishconversation.data.source.remote.comment;

import com.framgia.englishconversation.data.model.Comment;
import io.reactivex.Observable;
import java.util.List;

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
}
