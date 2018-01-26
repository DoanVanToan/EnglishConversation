package com.framgia.englishconversation.data.source.remote.comment;

import com.framgia.englishconversation.data.model.Comment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by VinhTL on 20/12/2017.
 */

public interface CommentDataSource {
    Observable<Comment> createNewComment(Comment comment);

    Observable<List<Comment>> getComment(Comment lastComment);

    Observable<Comment> registerModifyTimelines(Comment lastComment);

    Observable<Comment> updateComment(Comment comment);

    void removeListener();
}
