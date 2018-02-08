package com.framgia.englishconversation.data.source.remote.comment;

import com.framgia.englishconversation.data.model.Comment;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by VinhTL on 20/12/2017.
 */

public interface CommentDataSource {
    Observable<Comment> createNewComment(Comment comment);

    Observable<List<Comment>> getComment(Comment lastComment);

    Observable<HashMap<Integer, Comment>> registerModifyTimelines(Comment lastComment);

    Observable<Comment> updateComment(Comment comment);

    void removeListener();

    Observable<Comment> saveRevisionComment(Comment comment);

    Observable<Comment> deleteComment(Comment comment);

    Observable<Comment> saveCommentDelete(Comment comment);

}
