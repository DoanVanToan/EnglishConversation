package com.framgia.englishconversation.screen.createcomment;

import android.content.Intent;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsApi;
import com.framgia.englishconversation.data.source.remote.comment.CommentRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.comment.CommentRepository;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.Utils;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Listens to user actions from the UI ({@link CreateCommentActivity}), retrieves the data and
 * updates
 * the UI as required.
 */
final class CreateCommentPresenter
        implements CreateCommentContract.Presenter, RecordingAudioDialog.OnRecordingAudioListener {

    private final CreateCommentContract.ViewModel mViewModel;
    private Comment mComment;
    private MediaModel mMediaModel;
    private TimelineModel mTimelineModel;
    private SharedPrefsApi mSharedPrefsApi;
    private CommentRepository mCommentRepository;
    private CompositeDisposable mDisposable;

    CreateCommentPresenter(UserModel userModel, CreateCommentContract.ViewModel viewModel,
            TimelineModel timelineModel, SharedPrefsApi sharedPrefsApi) {
        mViewModel = viewModel;
        mSharedPrefsApi = sharedPrefsApi;
        mComment = new Comment();
        mComment.setCreateUser(userModel);
        mTimelineModel = timelineModel;
        mCommentRepository =
                new CommentRepository(new CommentRemoteDataSource(mTimelineModel.getId()));
        mComment.setPostId(mTimelineModel.getId());
        mComment.setCreatedAt(Utils.generateOppositeNumber(System.currentTimeMillis()));
        mDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {
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
    public void onRecordVideoDone(String uri, int type) {
        if (mMediaModel == null) {
            mMediaModel = new MediaModel(MediaModel.MediaType.VIDEO);
            mComment.setMediaModel(mMediaModel);
        }
        mMediaModel.setUrl(uri);
        mMediaModel.setName(
                mTimelineModel.getCreatedUser().getId() + "-" + System.currentTimeMillis());
        mMediaModel.setId(mMediaModel.getName());
        mViewModel.onMultimediaFileAttached(mMediaModel);
    }

    @Override
    public void onImageSelectDone(Image image) {
        if (mMediaModel == null) {
            mMediaModel = new MediaModel(MediaModel.MediaType.IMAGE);
            mComment.setMediaModel(mMediaModel);
        }
        mMediaModel.setUrl(image.path);
        mMediaModel.setName(
                mTimelineModel.getCreatedUser().getId() + "-" + System.currentTimeMillis());
        mMediaModel.setId(mMediaModel.getName());
        mViewModel.onMultimediaFileAttached(mMediaModel);
    }

    @Override
    public Comment getComment() {
        return mComment;
    }

    @Override
    public void onDeleteItemMediaClicked() {
        resetAttachMultimedia();
    }

    @Override
    public void postLiteralComment() {
        mDisposable.add(mCommentRepository.createNewComment(mComment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Comment>() {
                    @Override
                    public void onNext(Comment comment) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(Constant.EXTRA_COMMENT, mComment);
                        mViewModel.onPostLiteralCommentResult(true, returnIntent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Intent returnIntent = new Intent();
                        mViewModel.onPostLiteralCommentResult(false, returnIntent);
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    public void onDestroy() {
        mDisposable.dispose();
    }

    @Override
    public void onMultimediaMenuItemClick(int type) {
        mMediaModel = new MediaModel(type);
        mComment.setMediaModel(mMediaModel);
    }

    public void onDeleteItemMediaClicked(MediaModel mediaModel) {
        mComment.setMediaModel(null);
    }

    //TODO: Consult with Boruto about this method name, this is an after recording event
    @Override
    public void onRecordingAudioClick(String filePath, String fileName) {
        mMediaModel.setUrl(filePath);
        mMediaModel.setName(fileName);
        mViewModel.onMultimediaFileAttached(mMediaModel);
    }

    @Override
    public void onRecordCancel() {
        resetAttachMultimedia();
        mViewModel.showToast(R.string.message_record_cancel);
    }

    private void resetAttachMultimedia() {
        mMediaModel = null;
        mComment.setMediaModel(null);
    }
}
