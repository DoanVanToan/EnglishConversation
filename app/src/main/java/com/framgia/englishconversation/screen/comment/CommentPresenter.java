package com.framgia.englishconversation.screen.comment;

import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.comment.CommentRepository;
import com.google.firebase.auth.FirebaseUser;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

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
    private AuthenicationRepository mAuthenicationRepository;

    CommentPresenter(CommentContract.ViewModel viewModel, CommentRepository commentRepository,
            AuthenicationRepository authenicationRepository) {
        mViewModel = viewModel;
        mRepository = commentRepository;
        mDisposable = new CompositeDisposable();
        mAuthenicationRepository = authenicationRepository;
    }

    @Override
    public void onStart() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetCurrentUserSuccess(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetCurrentUserFailed(msg);
            }
        });
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
                            registerModifyComment(mLastComment);
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
