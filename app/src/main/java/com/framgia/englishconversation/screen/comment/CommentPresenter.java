package com.framgia.englishconversation.screen.comment;

import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.Status;
import com.framgia.englishconversation.data.model.StatusModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.comment.CommentRepository;
import com.framgia.englishconversation.utils.Utils;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link CommentFragment}), retrieves the data and updates
 * the UI as required.
 */
final class CommentPresenter implements CommentContract.Presenter {
    private static final String TAG = CommentPresenter.class.getName();
    private CommentRepository mRepository;
    private final CommentContract.ViewModel mViewModel;
    private CompositeDisposable mDisposable;
    private Comment mLastComment;

    CommentPresenter(CommentContract.ViewModel viewModel, CommentRepository commentRepository) {
        mViewModel = viewModel;
        mRepository = commentRepository;
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        registerModifyComment(mLastComment);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        mRepository.removeListener();
        mDisposable.dispose();
    }

    @Override
    public void fetchCommentData(Comment comment) {
        mDisposable.add(mRepository.getComment(comment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<List<Comment>>() {
                    @Override
                    public void onNext(List<Comment> comments) {
                        mViewModel.onGetCommentsSuccess(comments);
                        if (mLastComment == null) {
                            mLastComment = comments.isEmpty() ? null : comments.get(0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetCommentsFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void deleteComment(Comment comment) {
        //// TODO: 1/2/2018  handle save history update comment
        // show dialog handle delete comment
        mViewModel.showDialogComment();
        // check commentStatus null with database old
        if (comment.getStatusModel() == null) {
            comment.setStatusModel(new StatusModel());
        }
        comment.setCreatedAt(Utils.generateOppositeNumber(comment.getCreatedAt()));
        comment.getStatusModel().setStatus(Status.DELETE);
        comment.getStatusModel().setCreatedAt(Utils.generateOppositeNumber(
                System.currentTimeMillis()));
        comment.getStatusModel().setUserUpdate(comment.getCreateUser());
        mDisposable.add(mRepository.updateComment(comment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Comment>() {
                    @Override
                    public void onNext(Comment comment) {
                        mViewModel.deleComentSuccess(comment);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetCommentsFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public void initDialogItem(final Comment comment) {
        final String idCreateUser = comment.getCreateUser().getId();
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        repository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser firebaseUser) {
                if (idCreateUser.equals(firebaseUser.getUid())) {
                    mViewModel.showPopupMenuComment(comment);
                }
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });
    }

    private void registerModifyComment(Comment lastComment) {
        mDisposable.add(mRepository.registerModifyTimelines(lastComment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Comment>() {
                    @Override
                    public void onNext(Comment comment) {
                        mViewModel.onGetCommentSuccess(comment);
                        if (mLastComment == null) {
                            mLastComment = comment;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewModel.onGetCommentsFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
