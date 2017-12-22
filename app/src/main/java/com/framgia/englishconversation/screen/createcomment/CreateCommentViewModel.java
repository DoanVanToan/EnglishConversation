package com.framgia.englishconversation.screen.createcomment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.PopupMenu;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.service.BaseStorageService;
import com.framgia.englishconversation.service.FirebaseUploadService;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.FileUtils;
import com.framgia.englishconversation.utils.navigator.Navigator;
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

import static com.framgia.englishconversation.service.FirebaseUploadService
        .ACTION_UPLOAD_MULTI_FILE;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FILES;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_MEDIA_MODEL;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_URI;

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
    private TimelineModel mTimelineModel;
    private SimpleExoPlayer mSimpleExoPlayer;
    private long mCurrentPlaybackPosition = 0;
    private String mCurrentMultimediaFileUrl;
    private boolean mIsUploading = false;
    private BroadcastReceiver mReceiver;
    private ProgressDialog mProgressDialog;

    public CreateCommentViewModel(Context context, TimelineModel timelineModel) {
        mContext = context;
        mTimelineModel = timelineModel;
        mProgressDialog = new ProgressDialog(mContext);
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
    }

    public void onSubmitComment() {
        if (mPresenter.getComment().getMediaModel() != null) {
            uploadFiles(mPresenter.getComment().getMediaModel());
        }
    }

    public void onMultimediaIconTouch() {
        ((CreateCommentActivity) mContext).onMultimediaIconTouch();
    }

    private void showAudioRecordDialog() {
        if (mContext.getExternalCacheDir() == null) {
            return;
        }
        String fileName = mTimelineModel.getCreatedUser().getId()
                + "-"
                + System.currentTimeMillis()
                + Constant.DEFAULT_FORMAT_AUDIO;
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

    @Override
    public void setPlaying(boolean playing) {
        mIsPlaying = playing;
        notifyPropertyChanged(BR.playing);
    }

    @Bindable
    public String getTimeInProgressAudio() {
        return mTimeInProgressAudio;
    }

    @Override
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
    public void onPostLiteralCommentResult(boolean isSuccess, Intent resultData) {
        if (isSuccess) {
            ((AppCompatActivity) mContext).setResult(Activity.RESULT_OK, resultData);
        } else {
            ((AppCompatActivity) mContext).setResult(Activity.RESULT_CANCELED, resultData);
        }
        ((AppCompatActivity) mContext).finish();
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
        mNavigator.startActivityForResult(intent, Constant.RequestCode.RECORD_VIDEO);
    }

    public void showAlbumPicker() {
        Intent intent = new Intent(mContext, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Constant.Timeline.ONE_IMAGE);
        mNavigator.startActivityForResult(intent, Constant.RequestCode.SELECT_IMAGE);
    }

    public void uploadFiles(MediaModel mediaModel) {
        if (mIsUploading) {
            return;
        }
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
        mPresenter.getComment().setMediaModel(model);
        mPresenter.getComment().setContent(mInputtedComment);
        mPresenter.postLiteralComment();
    }

    private void registerBroadcastReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        if (mReceiver != null) {
            manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
            return;
        }
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == null) {
                    return;
                }
                switch (intent.getAction()) {
                    case FirebaseUploadService.UPLOAD_PROGRESS:
                        if (intent.getExtras() == null) {
                            return;
                        }
                        int percent = intent.getExtras()
                                .getInt(FirebaseUploadService.EXTRA_UPLOADED_PERCENT);
                        MediaModel mediaModel = intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
                        if (mediaModel == null) {
                            return;
                        }
                        mediaModel.setUploadPercent(percent);
                        String message = mContext.getString(R.string.prefix_uploading) + percent;
                        mProgressDialog.setMessage(message);
                        mProgressDialog.show();
                        break;
                    case FirebaseUploadService.UPLOAD_COMPLETE:
                        if (intent.getExtras() == null) {
                            return;
                        }
                        mediaModel = intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
                        if (mediaModel == null) {
                            return;
                        }
                        Uri downloadUri = (Uri) intent.getExtras().get(EXTRA_URI);
                        if (downloadUri == null) {
                            return;
                        }
                        mediaModel.setUrl(downloadUri.toString());
                        mProgressDialog.dismiss();
                        postLiteralComment(mediaModel);
                        mIsUploading = false;
                        break;
                    case FirebaseUploadService.UPLOAD_FINNISH_ALL:

                        break;
                    case FirebaseUploadService.UPLOAD_ERROR:
                        if (intent.getExtras() == null) {
                            return;
                        }
                        mediaModel = intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
                        if (mediaModel == null) {
                            return;
                        }
                        mNavigator.showToast(
                                String.format(mContext.getString(R.string.msg_upload_error),
                                        mediaModel.getName()));
                        break;
                }
            }
        };
        manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        manager.unregisterReceiver(mReceiver);
    }
}
