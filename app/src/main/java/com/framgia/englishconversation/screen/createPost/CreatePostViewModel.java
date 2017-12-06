package com.framgia.englishconversation.screen.createPost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import android.media.MediaPlayer;

import android.media.MediaRecorder;

import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.LocationModel;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.PostType;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.record.Util;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.service.FirebaseUploadService;
import com.framgia.englishconversation.utils.FileUtils;
import com.framgia.englishconversation.utils.Utils;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioBuilder;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.framgia.englishconversation.data.model.PostType.IMAGE;
import static com.framgia.englishconversation.service.BaseStorageService.POST_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService.ACTION_UPLOAD_MULTI_FILE;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FILES;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_MEDIA_MODEL;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_URI;

/**
 * Exposes the data to be used in the CreatePost screen.
 */

public class CreatePostViewModel extends BaseObservable implements CreatePostContract.ViewModel {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int SELECT_IMAGE_REQUEST = 2;
    private static final int LIMIT_IMAGES = 10;
    private static final int REQUEST_RECORD_AUDIO = 3;
    private static final int REQUEST_RECORD_VIDEO = 4;

    private final static String[] PERMISSION = new String[]{
            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final String UPLOADING = "Uploading: ";
    private static final String TOAST_MESSAGE =
            "Sorry, you can not start recording audio mode. Please try again later!";
    private static final String TOAST_CANCEL =
            "You have just cancel the recording audio without saving!";

    private RecordingAudioDialog mRecordingAudioDialog;
    private CreatePostContract.Presenter mPresenter;
    private UserModel mUser;
    private String mUserUrl;
    private String mUserName;
    private String mAddress;
    @PostType
    private int mCreateType;
    private CreatePostActivity mActivity;
    private Navigator mNavigator;
    private ProgressDialog mProgressDialog;
    private TimelineModel mTimelineModel;
    private BroadcastReceiver mReceiver;
    private boolean mIsUploading;
    private String mFileName;
    private String mFilePath;
    private MediaPlayer mMediaPlayer;
    private boolean mIsPlaying;

    private MediaAdapter mAdapter;

    CreatePostViewModel(CreatePostActivity activity, Navigator navigator,
                        @PostType int createType) {
        mActivity = activity;
        mNavigator = navigator;
        mCreateType = createType;
        mTimelineModel = new TimelineModel();
        mProgressDialog = new ProgressDialog(mActivity);
        mRecordingAudioDialog = RecordingAudioDialog.newInstance();
        mAdapter = new MediaAdapter(this);
        getData();

    }

    @Override
    public void onStart() {
        mPresenter.onStart();
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
                        int percent =
                                intent.getExtras().getInt(
                                        FirebaseUploadService.EXTRA_UPLOADED_PERCENT
                                );
                        MediaModel mediaModel = intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
                        if (mediaModel == null) {
                            return;
                        }
                        mediaModel.setUploadPercent(percent);
                        String message = UPLOADING + percent;
                        mProgressDialog.setMessage(message);
                        mProgressDialog.show();
                        handleProgress(mediaModel);
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
                        handleFinnish(mediaModel);
                        break;

                    case FirebaseUploadService.UPLOAD_FINNISH_ALL:
                        mIsUploading = false;
                        mPresenter.createPost(mTimelineModel);
                        mProgressDialog.dismiss();
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
                                String.format(mActivity.getString(R.string.msg_upload_error), mediaModel.getName())
                        );
                        break;
                }
            }
        };
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mActivity);
        manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                Place place = PlacePicker.getPlace(data, mActivity);
                LocationModel locationModel = new LocationModel();
                locationModel.setAddress(place.getAddress().toString());
                locationModel.setLat(place.getLatLng().latitude);
                locationModel.setLng(place.getLatLng().longitude);
                setAddress(place.getAddress().toString());
                mTimelineModel.setLocation(locationModel);
                break;
            case SELECT_IMAGE_REQUEST:
                List<Image> images =
                        data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                if (images == null || images.size() == 0) break;
                addPostImage(images);
                break;
            case REQUEST_RECORD_AUDIO:
                String filePath = Util.getResultFilePath(data);
                String fileName = Util.getResultFileName(data);
                MediaModel record = new MediaModel(MediaModel.MediaType.AUDIO);
                record.setId(UUID.randomUUID().toString());
                record.setUrl(filePath);
                record.setName(fileName);
                addPostAudioRecord(record);
                break;
            case REQUEST_RECORD_VIDEO:
                updateVideoMedia(data.getData());
                break;
            default:
                break;
        }
    }

    private void updateVideoMedia(Uri uri) {
        mTimelineModel.getMedias().clear();
        MediaModel mediaModel = new MediaModel(MediaModel.MediaType.VIDEO);
        mediaModel.setUrl(FileUtils.getFilePath(mActivity, uri));
        mediaModel.setId(UUID.randomUUID().toString());
        mTimelineModel.getMedias().add(mediaModel);
        mAdapter.setData(mTimelineModel.getMedias());
    }

    @Override
    public void onGetCurrentUserSuccess(UserModel data) {
        mUser = data;
        setUserName(data.getUserName());
        setUserUrl(data.getPhotoUrl());
    }

    @Override
    public void onGetCurrentUserFailed(String msg) {

    }

    @Override
    public void onImagePickerClick() {
        mActivity.fillColorSelectedButton(CreatePostActivity.PHOTO_POSITION);
        selectImage();
    }

    @Override
    public void onPlacePickerClick() {
        mActivity.fillColorSelectedButton(CreatePostActivity.LOCATION_POSITION);
        openPlacePicker();
    }

    @Override
    public void onCreatePost() {
        updateTimelineModel();
        if (mTimelineModel.getMedias() != null && mTimelineModel.getMedias().size() != 0) {
            uploadFiles(mTimelineModel.getMedias());
        } else {
            mPresenter.createPost(mTimelineModel);
        }
    }

    @Override
    public void uploadFiles(List<MediaModel> mediaModels) {
        if (mIsUploading) return;
        mIsUploading = true;
        mActivity.startService(
                new Intent(mActivity, FirebaseUploadService.class).putParcelableArrayListExtra(
                        EXTRA_FILES, (ArrayList<? extends Parcelable>) mediaModels)
                        .putExtra(EXTRA_FOLDER, POST_FOLDER)
                        .setAction(ACTION_UPLOAD_MULTI_FILE));
    }

    @Override
    public void onCreatePostSuccess() {
        mNavigator.finishActivity();
    }

    @Override
    public void onCreatePostFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && isEnablePermision(permissions, grantResults)) {
            onRecordingAudio();
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
    }

    @Override
    public void setPresenter(CreatePostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void addPostAudioRecord(MediaModel record) {
        if (record == null) {
            return;
        }
        if (mTimelineModel.getMedias() == null) {
            mTimelineModel.setMedias(new ArrayList<MediaModel>());
        }
        mTimelineModel.getMedias().clear();
        mTimelineModel.getMedias().add(record);
        mAdapter.setData(mTimelineModel.getMedias());
    }

    private void addPostImage(List<Image> images) {
        if (images == null) {
            return;
        }

        if (mTimelineModel.getMedias() == null) {
            mTimelineModel.setMedias(new ArrayList<MediaModel>());
        }

        for (Image image : images) {
            MediaModel mediaModel = new MediaModel(MediaModel.MediaType.IMAGE);
            mediaModel.setId(String.valueOf(image.id));
            mediaModel.setUrl(image.path);
            mediaModel.setName(image.name);
            mTimelineModel.getMedias().add(mediaModel);
        }
        mAdapter.setData(mTimelineModel.getMedias());
    }

    private void updateTimelineModel() {
        mTimelineModel.setCreatedUser(mUser);
        mTimelineModel.setCreatedAt(System.currentTimeMillis());
    }

    private void handleProgress(MediaModel mediaModel) {
        for (MediaModel model : mTimelineModel.getMedias()) {
            if (model.getId().equals(mediaModel.getId())) {
                model.setUploadPercent(mediaModel.getUploadPercent());
                return;
            }
        }
    }

    private void handleFinnish(MediaModel mediaModel) {
        for (MediaModel model : mTimelineModel.getMedias()) {
            if (model.getId().equals(mediaModel.getId())) {
                model.setUrl(mediaModel.getUrl());
                return;
            }
        }
    }

    private void openPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            mNavigator.startActivityForResult(builder.build(mActivity), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onDeleteItemMediaClicked(MediaModel mediaModel) {
        mTimelineModel.getMedias().remove(mediaModel);
        mAdapter.removeItem(mediaModel);
    }

    private void selectImage() {
        Intent intent = new Intent(mActivity, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, LIMIT_IMAGES);
        mNavigator.startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    private void getData() {
        switch (mCreateType) {
            case PostType.LOCATION:
                openPlacePicker();
                break;
            case PostType.IMAGE:
                selectImage();
                break;
            case PostType.VIDEO:
                break;
            case PostType.RECORD:
                //TODO: action of recording audio
                break;
            case PostType.TEXT_CONTENT:
                //TODO: action of conversation
                break;
            default:
                break;
        }
    }

    public void onVideoPickerClicked() {
        mActivity.fillColorSelectedButton(CreatePostActivity.VIDEO_RECORD_POSITION);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mNavigator.startActivityForResult(intent, REQUEST_RECORD_VIDEO);
        }
    }

    public void onStartRecordClicked() {
        if (Utils.isAllowPermision(mActivity, PERMISSION)) {
            mActivity.fillColorSelectedButton(CreatePostActivity.AUDIO_RECORD_POSITION);
            onRecordingAudio();
        }
    }

    private void onRecordingAudio() {
        if (mActivity.getExternalCacheDir() == null) {
            return;
        }
        mFileName = "RecordingAudio_" + System.currentTimeMillis() + ".3gp";
        mFilePath = mActivity.getExternalCacheDir().getAbsolutePath() + "/" + mFileName;
        RecordingAudioBuilder.with(mActivity, mRecordingAudioDialog)
                .setFileName(mFileName)
                .setFilePath(mFilePath)
                .setAudioSource(AudioSource.MIC)
                .showRecordingAudioFromActivity();
        RecordingAudioDialog.OnRecordingAudioListener recordingAudioClickListener =
                new RecordingAudioDialog.OnRecordingAudioListener() {
                    @Override
                    public void onRecordingAudioClick(String filePath, String fileName) {
                        MediaModel record = new MediaModel(MediaModel.MediaType.AUDIO);
                        record.setId(UUID.randomUUID().toString());
                        record.setUrl(filePath);
                        record.setName(fileName);
                        addPostAudioRecord(record);
                        initMedia(filePath);
                    }

                    @Override
                    public void onRecordCancel() {
                        Toast.makeText(mActivity, TOAST_CANCEL, Toast.LENGTH_SHORT).show();
                    }
                };
        mRecordingAudioDialog.setOnRecordingAudioClickListener(recordingAudioClickListener);
    }

    private void initMedia(String filePath) {
        setPlaying(false);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    setPlaying(false);
                }
            });
        } catch (IOException e) {
            Toast.makeText(mActivity, TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    private void releaseMedia() {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    private boolean isEnablePermision(String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Bindable
    public String getUserUrl() {
        return mUserUrl;
    }

    public void setUserUrl(String userUrl) {
        mUserUrl = userUrl;
        notifyPropertyChanged(BR.userUrl);
    }

    @Bindable
    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
        notifyPropertyChanged(BR.userName);
    }

    public UserModel getUser() {
        return mUser;
    }

    public void setUser(UserModel user) {
        mUser = user;
    }

    @Bindable
    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
        notifyPropertyChanged(BR.address);
    }

    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

    public void setTimelineModel(TimelineModel timelineModel) {
        mTimelineModel = timelineModel;
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
    public MediaAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(MediaAdapter adapter) {
        mAdapter = adapter;
        notifyPropertyChanged(BR.adapter);
    }

    public void onPlayingRecordingAudioClick() {
        if (!mIsPlaying) {
            mMediaPlayer.start();
        } else {
            mMediaPlayer.pause();
        }
        setPlaying(!mIsPlaying);
    }

    public void onDismissPlayingRecordAudioClick() {
        // TODO: 12/6/17
        mTimelineModel.getMedias().clear();
    }

}
