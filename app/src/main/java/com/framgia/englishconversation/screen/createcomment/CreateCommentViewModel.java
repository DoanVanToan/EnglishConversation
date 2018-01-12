package com.framgia.englishconversation.screen.createcomment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupMenu;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.screen.createPost.UploadBroadcastReceiver;
import com.framgia.englishconversation.service.BaseStorageService;
import com.framgia.englishconversation.service.FirebaseUploadService;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.FileUtils;
import com.framgia.englishconversation.utils.Utils;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.framgia.englishconversation.widget.dialog.UploadProgressDialog;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioBuilder;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioDialog;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.framgia.englishconversation.service.FirebaseUploadService.ACTION_UPLOAD_MULTI_FILE;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FILES;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FOLDER;


/**
 * Exposes the data to be used in the CreateComment screen.
 */

public class CreateCommentViewModel extends BaseObservable
        implements CreateCommentContract.ViewModel, PopupMenu.OnMenuItemClickListener {

    private CreateCommentContract.Presenter mPresenter;
    private String mInputtedComment;
    private Context mContext;
    private RecordingAudioDialog mRecordingAudioDialog;
    private boolean mIsPlaying;
    private String mTimeInProgressAudio;
    private Navigator mNavigator;
    private MultimediaAdapter mMultimediaAdapter;
    private String mTimelineModelId;
    private SimpleExoPlayer mSimpleExoPlayer;
    private long mCurrentPlaybackPosition = 0;
    private String mCurrentMultimediaFileUrl;
    private boolean mIsUploading = false;
    private BroadcastReceiver mReceiver;
    private UploadProgressDialog mProgressDialog;
    private PopupMenu mPopupMenu;
    private CreateCommentFragment mCommentFragment;
    private UserModel mUserModel;

    public CreateCommentViewModel(CreateCommentFragment commentFragment, String timelineModelId) {
        mCommentFragment = commentFragment;
        mContext = commentFragment.getActivity();
        mTimelineModelId = timelineModelId;
        mProgressDialog = new UploadProgressDialog(mContext);
        mNavigator = new Navigator((Activity) mContext);
        mRecordingAudioDialog = RecordingAudioDialog.newInstance();
        mMultimediaAdapter = new MultimediaAdapter(this);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        registerBroadcastReceiver();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
        if ((Util.SDK_INT <= Build.VERSION_CODES.M) || mSimpleExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        mPresenter.onPause();
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        unregisterBroadcastReceiver();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void setPresenter(CreateCommentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.type_audio:
                mPresenter.onMultimediaMenuItemClick(MediaModel.MediaType.AUDIO);
                showAudioRecordDialog();
                return true;
            case R.id.type_image:
                showAlbumPicker();
                return true;
            case R.id.type_video:
                mPresenter.onMultimediaMenuItemClick(MediaModel.MediaType.VIDEO);
                showVideoRecordScreen();
                return true;
        }
        return false;
    }

    @Bindable
    public String getInputtedComment() {
        return mInputtedComment;
    }

    public void setInputtedComment(String inputtedComment) {
        mInputtedComment = inputtedComment;
        notifyPropertyChanged(BR.inputtedComment);
    }

    public void onSubmitComment(View view) {
        Comment comment = new Comment();
        InputMethodManager imm =
                (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (mMultimediaAdapter.getMediaModel() != null) {
            uploadFiles(mMultimediaAdapter.getMediaModel());
        } else {
            comment.setPostId(mTimelineModelId);
            comment.setCreatedAt(Utils.generateOppositeNumber(System.currentTimeMillis()));
            comment.setContent(mInputtedComment);
            comment.setCreateUser(mUserModel);
            mPresenter.postLiteralComment(comment);
        }
    }

    public void onMultimediaIconTouch() {
        mPopupMenu.show();
    }

    private void showAudioRecordDialog() {
        if (mContext.getExternalCacheDir() == null) {
            return;
        }
        String fileName =
                mTimelineModelId + "-" + System.currentTimeMillis() + Constant.DEFAULT_FORMAT_AUDIO;
        String filePath = mContext.getExternalCacheDir().getAbsolutePath() + "/" + fileName;
        RecordingAudioBuilder.with((AppCompatActivity) mContext, mRecordingAudioDialog)
                .setFileName(fileName)
                .setFilePath(filePath)
                .setAudioSource(AudioSource.MIC)
                .showRecordingAudioFromActivity();
        mRecordingAudioDialog.setOnRecordingAudioClickListener((CreateCommentPresenter) mPresenter);
    }

    @Bindable
    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void setPlaying(boolean playing) {
        mIsPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    @Bindable
    public String getTimeInProgressAudio() {
        return mTimeInProgressAudio;
    }

    public void setTimeInProgressAudio(String timeInProgressAudio) {
        mTimeInProgressAudio = timeInProgressAudio;
        notifyPropertyChanged(BR.timeInProgressAudio);
    }

    @Override
    public void showToast(int stringId) {
        switch (stringId) {
            case R.string.error_init_media:
                mNavigator.showToast(R.string.error_init_media);
                break;
        }
    }

    @Override
    public void onGetMultimediaDataDone(Intent intent, int type) {
        switch (type) {
            case MediaModel.MediaType.VIDEO:
                mPresenter.onRecordVideoDone(FileUtils.getFilePath(mContext, intent.getData()),
                        type);
                break;
            case MediaModel.MediaType.IMAGE:
                List<Image> images =
                        intent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                if (images == null || images.isEmpty()) {
                    return;
                }
                mPresenter.onImageSelectDone(images.get(0));
        }
    }

    @Override
    public void onMultimediaFileAttached(MediaModel mediaModel) {
        releasePlayer();
        mCurrentPlaybackPosition = 0;
        if (mediaModel == null) {
            mCurrentMultimediaFileUrl = null;
            return;
        }
        mCurrentMultimediaFileUrl = mediaModel.getUrl();
        initializePlayer();
        mMultimediaAdapter.setData(Arrays.asList(mediaModel));
    }

    @Override
    public void onPostLiteralCommentSuccess(Comment comment) {
        mMultimediaAdapter.setData(new ArrayList<MediaModel>());
        setInputtedComment(null);
    }

    @Override
    public void onPostLiteralCommentFailure(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
    }

    @Override
    public void setPopUpMenu(PopupMenu popUpMenu) {
        mPopupMenu = popUpMenu;
    }

    @Bindable
    public SimpleExoPlayer getSimpleExoPlayer() {
        return mSimpleExoPlayer;
    }

    public void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer) {
        mSimpleExoPlayer = simpleExoPlayer;
        notifyPropertyChanged(BR.simpleExoPlayer);
    }

    private void initializePlayer() {
        if (mCurrentMultimediaFileUrl == null) {
            return;
        }
        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(mContext, new DefaultTrackSelector());
        setSimpleExoPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(mCurrentPlaybackPosition);
        Uri uri = Uri.parse(mCurrentMultimediaFileUrl);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        DefaultDataSourceFactory dataSourceFactory =
                new DefaultDataSourceFactory(mContext, Constant.USER_AGENT);
        ExtractorMediaSource mediaSource =
                new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
        player.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mCurrentPlaybackPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    public MultimediaAdapter getMultimediaAdapter() {
        return mMultimediaAdapter;
    }

    public void onDeleteItemMediaClicked(MediaModel mediaModel) {
        mMultimediaAdapter.removeItem(mediaModel);
        mPresenter.onDeleteItemMediaClicked();
        new File(mediaModel.getUrl()).getAbsoluteFile().delete();
    }

    public void showVideoRecordScreen() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mCommentFragment.startActivityForResult(intent, Constant.RequestCode.RECORD_VIDEO);
    }

    public void showAlbumPicker() {
        Intent intent = new Intent(mContext, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Constant.Timeline.ONE_IMAGE);
        mCommentFragment.startActivityForResult(intent, Constant.RequestCode.SELECT_IMAGE);
    }

    public void uploadFiles(MediaModel mediaModel) {
        if (mIsUploading) {
            return;
        }
        mProgressDialog.show();
        mIsUploading = true;
        ArrayList<MediaModel> mediaModels = new ArrayList<>();
        mediaModels.add(mediaModel);
        mContext.startService(
                new Intent(mContext, FirebaseUploadService.class).putParcelableArrayListExtra(
                        EXTRA_FILES, mediaModels)
                        .putExtra(EXTRA_FOLDER, BaseStorageService.POST_FOLDER)
                        .setAction(ACTION_UPLOAD_MULTI_FILE));
    }

    private void postLiteralComment(MediaModel model) {
        Comment comment = new Comment();
        comment.setPostId(mTimelineModelId);
        comment.setCreatedAt(Utils.generateOppositeNumber(System.currentTimeMillis()));
        comment.setMediaModel(model);
        comment.setContent(mInputtedComment);
        comment.setCreateUser(mUserModel);
        mPresenter.postLiteralComment(comment);
    }

    private void registerBroadcastReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        if (mReceiver != null) {
            manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
            return;
        }
        mReceiver = new UploadBroadcastReceiver(new UploadBroadcastReceiver.OnReceiverListenner() {
            @Override
            public void onUploadProgress(MediaModel mediaModel) {
                mProgressDialog.setProgressPercent(mediaModel.getUploadPercent());
            }

            @Override
            public void onUploadFinnish(MediaModel mediaModel) {
                mProgressDialog.dismiss();
                postLiteralComment(mediaModel);
                mIsUploading = false;
            }

            @Override
            public void onUploadComplete() {

            }

            @Override
            public void onUploadError(MediaModel mediaModel) {
                String errorMsg = String.format(mContext.getString(R.string.msg_upload_error),
                        mediaModel.getName());
                mNavigator.showToast(errorMsg);
                mProgressDialog.dismiss();
            }
        });
        manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        manager.unregisterReceiver(mReceiver);
    }

    @Override
    public void onGetCurrentUserSuccess(UserModel data) {
        setUserModel(data);
    }

    @Override
    public void onGetCurrentUserFailed(String msg) {

    }

    @Bindable
    public UserModel getUserModel() {
        return mUserModel;
    }

    public void setUserModel(UserModel userModel) {
        mUserModel = userModel;
        notifyPropertyChanged(BR.userModel);
    }
}
