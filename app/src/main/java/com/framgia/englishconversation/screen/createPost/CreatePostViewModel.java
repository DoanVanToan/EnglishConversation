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
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.ConversationModel;
import com.framgia.englishconversation.data.model.GravityType;
import com.framgia.englishconversation.data.model.LocationModel;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.record.Util;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.service.FirebaseUploadService;
import com.framgia.englishconversation.utils.Constant;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
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
    private static final int REQUEST_RECORD_AUDIO = 3;
    private static final int REQUEST_RECORD_VIDEO = 4;
    private static final int LIMIT_IMAGES = 10;
    private static final int PERIOD_TIME = 1;
    private final static String[] PERMISSION = new String[]{
            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int SIZE_CONVERSATION_DEFAULT = 0;

    @AdapterType
    private int mAdapterType;
    private String mAddress;
    private boolean mIsUploading;
    private boolean mIsPlaying;
    private int mProgressAudio;
    private String mTimeInProgressAudio;
    private long mDurationMedia;
    private long mCurrentDuration;
    private UserModel mUser;
    private TimelineModel mTimelineModel;
    private MediaPlayer mMediaPlayer;
    private RecordingAudioDialog mRecordingAudioDialog;
    private CreatePostContract.Presenter mPresenter;
    private CreatePostActivity mActivity;
    private Navigator mNavigator;
    private ProgressDialog mProgressDialog;
    private BroadcastReceiver mReceiver;
    private CompositeDisposable mCompositeDisposable;
    private MediaAdapter mMediaAdapter;
    private ConversationAdapter mConversationAdapter;

    CreatePostViewModel(CreatePostActivity activity, Navigator navigator) {
        mActivity = activity;
        mNavigator = navigator;
        mAdapterType = AdapterType.CONVERSATION;
        mTimelineModel = new TimelineModel();
        mProgressDialog = new ProgressDialog(mActivity);
        mRecordingAudioDialog = RecordingAudioDialog.newInstance();
        mCompositeDisposable = new CompositeDisposable();
        mMediaAdapter = new MediaAdapter(this);
        mConversationAdapter = new ConversationAdapter(mActivity, this);
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
                        int percent = intent.getExtras().getInt(
                                FirebaseUploadService.EXTRA_UPLOADED_PERCENT);
                        MediaModel mediaModel = intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
                        if (mediaModel == null) {
                            return;
                        }
                        mediaModel.setUploadPercent(percent);
                        String message = mActivity.getString(R.string.prefix_uploading) + percent;
                        mProgressDialog.setMessage(message);
                        mProgressDialog.show();
                        updateUploadProgress(mediaModel);
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
                        handleFinish(mediaModel);
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
                                String.format(mActivity.getString(R.string.msg_upload_error),
                                        mediaModel.getName()));
                        break;
                }
            }
        };
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mActivity);
        manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
        if (mMediaPlayer != null) {
            onPlayAudioClick();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
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

    private void setAddress(String address) {
        mAddress = address;
        notifyPropertyChanged(BR.address);
    }

    @Override
    public void onGetCurrentUserSuccess(UserModel data) {
        mUser = data;
    }

    @Override
    public void onGetCurrentUserFailed(String msg) {
        String errorMessage = msg + ": " + mActivity.getString(R.string.msg_get_user_info_failed);
        mNavigator.showToast(errorMessage);
    }

    @Override
    public void onCreatePost() {
        updateTimelineModel();
        if (mAdapterType == AdapterType.CONVERSATION) {
            mTimelineModel.setConversations(mConversationAdapter.getValidatedData());
        }
        switch (mTimelineModel.getPostType()) {
            case MediaModel.MediaType.CONVERSATION:
                List<MediaModel> conversationMedias =
                        getConversationMedias(mTimelineModel.getConversations());
                if (conversationMedias == null || conversationMedias.isEmpty()) {
                    mPresenter.createPost(mTimelineModel);
                } else {
                    uploadFiles(conversationMedias);
                }
                break;
            case MediaModel.MediaType.ONLY_TEXT:
                mPresenter.createPost(mTimelineModel);
                break;
            default:
                uploadFiles(mTimelineModel.getMedias());
                break;
        }
    }

    private List<MediaModel> getConversationMedias(List<ConversationModel> conversations) {
        List<MediaModel> result = new ArrayList<>();
        for (ConversationModel conversationModel : conversations) {
            if (conversationModel != null && conversationModel.getMediaModel() != null) {
                result.add(conversationModel.getMediaModel());
            }
        }
        return result;
    }

    @Override
    public void uploadFiles(List<MediaModel> mediaModels) {
        if (mIsUploading) {
            return;
        }
        mIsUploading = true;
        mActivity.startService(
                new Intent(mActivity, FirebaseUploadService.class).putParcelableArrayListExtra(
                        EXTRA_FILES, (ArrayList<? extends Parcelable>) mediaModels)
                        .putExtra(EXTRA_FOLDER, POST_FOLDER)
                        .setAction(ACTION_UPLOAD_MULTI_FILE));
    }

    @Override
    public void onCreatePostSuccess() {
        releaseMedia();
        mNavigator.finishActivity();
    }

    @Override
    public void onCreatePostFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && isEnablePermission(permissions, grantResults)) {
            onAudioRecordingClick();
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
        onPlayAudioClick();
    }

    @Override
    public void setPresenter(CreatePostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private boolean isEnablePermission(String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void updateVideoMedia(Uri uri) {
        mTimelineModel.getMedias().clear();
        MediaModel mediaModel = new MediaModel(MediaModel.MediaType.VIDEO);
        mediaModel.setUrl(FileUtils.getFilePath(mActivity, uri));
        mediaModel.setId(UUID.randomUUID().toString());
        mTimelineModel.getMedias().add(mediaModel);
        mMediaAdapter.setData(mTimelineModel.getMedias());
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
        mMediaAdapter.setData(mTimelineModel.getMedias());
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
        mMediaAdapter.setData(mTimelineModel.getMedias());
    }

    private void updateTimelineModel() {
        mTimelineModel.setCreatedUser(mUser);
        mTimelineModel.setCreatedAt(Utils.generateOppositeNumber(System.currentTimeMillis()));
    }

    private boolean updatePercentUpload(MediaModel mediaTimeLine, MediaModel model) {
        if (mediaTimeLine != null && mediaTimeLine.getId().equals(model.getId())) {
            mediaTimeLine.setUploadPercent(model.getUploadPercent());
            return true;
        }
        return false;
    }

    private void updateUploadProgress(MediaModel model) {
        switch (mAdapterType) {
            case AdapterType.CONVERSATION:
                for (ConversationModel conversationModel : mTimelineModel.getConversations()) {
                    MediaModel mediaTimeLine = conversationModel.getMediaModel();
                    if (updatePercentUpload(mediaTimeLine, model)) {
                        return;
                    }
                }
                break;
            case AdapterType.MEDIA:
                for (MediaModel mediaTimeLine : mTimelineModel.getMedias()) {
                    if (updatePercentUpload(mediaTimeLine, model)) {
                        return;
                    }
                }
            default:
                break;
        }
    }

    private boolean updateMediaAfterUploadSuccessful(MediaModel mediaTimeLine, MediaModel model) {
        if (mediaTimeLine != null && mediaTimeLine.getId().equals(model.getId())) {
            mediaTimeLine.setUrl(model.getUrl());
            return true;
        }
        return false;
    }

    private void handleFinish(MediaModel model) {
        switch (mAdapterType) {
            case AdapterType.CONVERSATION:
                for (ConversationModel conversationModel : mTimelineModel.getConversations()) {
                    MediaModel mediaTimeLine = conversationModel.getMediaModel();
                    if (updateMediaAfterUploadSuccessful(mediaTimeLine, model)) {
                        return;
                    }
                }
                break;
            case AdapterType.MEDIA:
                for (MediaModel mediaTimeLine : mTimelineModel.getMedias()) {
                    if (updateMediaAfterUploadSuccessful(mediaTimeLine, model)) {
                        return;
                    }
                }
            default:
                break;
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

    private void selectImage() {
        Intent intent = new Intent(mActivity, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, LIMIT_IMAGES);
        mNavigator.startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    public void onDeleteItemMediaClicked(MediaModel mediaModel) {
        mTimelineModel.getMedias().remove(mediaModel);
        mMediaAdapter.removeItem(mediaModel);
    }

    private void initMedia(String filePath) {
        setPlaying(false);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mDurationMedia = mMediaPlayer.getDuration() / Constant.MILLISECONDS_PER_MINUTE;
            setTimeInProgressAudio(Utils.updateDuration(mDurationMedia));
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    setPlaying(false);
                }
            });
        } catch (IOException e) {
            mNavigator.showToast(mActivity.getString(R.string.error_init_media));
        }
    }

    private void showAudioDialog() {
        if (mActivity.getExternalCacheDir() == null) {
            return;
        }
        String fileName = mActivity.getString(R.string.prefix_audio_file_name)
                + System.currentTimeMillis() + Constant.DEFAULT_FORMAT_AUDIO;
        String filePath = mActivity.getExternalCacheDir().getAbsolutePath() + "/" + fileName;
        RecordingAudioBuilder.with(mActivity, mRecordingAudioDialog)
                .setFileName(fileName)
                .setFilePath(filePath)
                .setAudioSource(AudioSource.MIC)
                .showRecordingAudioFromActivity();
    }

    private void showRecordCancelMessage() {
        mNavigator.showToast(mActivity.getString(R.string.message_record_cancel));
    }

    public void onAudioConversationClick(final ConversationModel conversations) {
        showAudioDialog();
        RecordingAudioDialog.OnRecordingAudioListener recordingAudioClickListener =
                new RecordingAudioDialog.OnRecordingAudioListener() {
                    @Override
                    public void onRecordingAudioClick(String filePath, String fileName) {
                        MediaModel record = new MediaModel(MediaModel.MediaType.AUDIO);
                        record.setId(UUID.randomUUID().toString());
                        record.setUrl(filePath);
                        record.setName(fileName);
                        initMedia(filePath);
                        conversations.setMediaModel(record);
                    }

                    @Override
                    public void onRecordCancel() {
                        showRecordCancelMessage();
                    }
                };
        mRecordingAudioDialog.setOnRecordingAudioClickListener(recordingAudioClickListener);
    }

    private void onAudioRecordingClick() {
        showAudioDialog();
        RecordingAudioDialog.OnRecordingAudioListener recordingAudioClickListener =
                new RecordingAudioDialog.OnRecordingAudioListener() {
                    @Override
                    public void onRecordingAudioClick(String filePath, String fileName) {
                        MediaModel record = new MediaModel(MediaModel.MediaType.AUDIO);
                        record.setId(UUID.randomUUID().toString());
                        record.setUrl(filePath);
                        record.setName(fileName);
                        initMedia(filePath);
                        addPostAudioRecord(record);
                    }

                    @Override
                    public void onRecordCancel() {
                        showRecordCancelMessage();
                    }
                };
        mRecordingAudioDialog.setOnRecordingAudioClickListener(recordingAudioClickListener);
    }

    public void onRecordAudioClick() {
        if (Utils.isAllowPermision(mActivity, PERMISSION)) {
            setAdapterType(AdapterType.MEDIA);
            mActivity.fillColorSelectedButton(CreatePostActivity.AUDIO_RECORD_POSITION);
            onAudioRecordingClick();
        }
    }

    public void onVideoPickerClick() {
        setAdapterType(AdapterType.MEDIA);
        mActivity.fillColorSelectedButton(CreatePostActivity.VIDEO_RECORD_POSITION);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            mNavigator.startActivityForResult(intent, REQUEST_RECORD_VIDEO);
        }
    }

    public void onCreateConventionClick() {
        mActivity.fillColorSelectedButton(CreatePostActivity.CONVERSATION_POSITION);
        setAdapterType(AdapterType.CONVERSATION);
    }

    @Override
    public void onImagePickerClick() {
        mActivity.fillColorSelectedButton(CreatePostActivity.PHOTO_POSITION);
        setAdapterType(AdapterType.MEDIA);
        selectImage();
    }

    @Override
    public void onPlacePickerClick() {
        mActivity.fillColorSelectedButton(CreatePostActivity.LOCATION_POSITION);
        setAdapterType(AdapterType.MEDIA);
        openPlacePicker();
    }

    public void onPlayAudioClick() {
        if (mMediaPlayer == null) {
            return;
        }
        if (!mIsPlaying) {
            mMediaPlayer.start();
            updateAudioUi();
        } else {
            mMediaPlayer.pause();
        }
        setPlaying(!mIsPlaying);
    }

    private void updateAudioUi() {
        Disposable disposable = Observable.interval(PERIOD_TIME, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long time) {
                        if ((mCurrentDuration + time) >= mDurationMedia) {
                            setProgressAudio(Constant.ZERO_PERCENT);
                            setTimeInProgressAudio(Utils.updateDuration(mDurationMedia));
                            mCurrentDuration = Constant.ZERO_PERCENT;
                            mCompositeDisposable.clear();
                        } else {
                            if (!mIsPlaying) {
                                mCurrentDuration += time + 1;
                                long timeInProgress = mDurationMedia - mCurrentDuration;
                                int progress = (int) (mCurrentDuration
                                        * Constant.ONE_HUNDRED_PERCENT / mDurationMedia);
                                setTimeInProgressAudio(Utils.updateDuration(timeInProgress));
                                setProgressAudio(progress);
                                mCompositeDisposable.clear();
                            } else {
                                long timeInProgress = mDurationMedia - mCurrentDuration - (time - 1);
                                int progress =
                                        (int) ((mDurationMedia - timeInProgress)
                                                * Constant.ONE_HUNDRED_PERCENT / mDurationMedia);
                                setTimeInProgressAudio(Utils.updateDuration(timeInProgress));
                                setProgressAudio(progress);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mNavigator.showToast(
                                mActivity.getString(R.string.message_error_audio_update));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void releaseMedia() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
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
    public int getProgressAudio() {
        return mProgressAudio;
    }

    public void setProgressAudio(int progressAudio) {
        mProgressAudio = progressAudio;
        notifyPropertyChanged(BR.progressAudio);
    }

    @Bindable
    public String getTimeInProgressAudio() {
        return mTimeInProgressAudio;
    }

    public void setTimeInProgressAudio(String timeInProgressAudio) {
        mTimeInProgressAudio = timeInProgressAudio;
        notifyPropertyChanged(BR.timeInProgressAudio);
    }

    public UserModel getUser() {
        return mUser;
    }

    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }

    @Bindable
    public int getAdapterType() {
        return mAdapterType;
    }

    public void setAdapterType(@AdapterType int adapterType) {
        mAdapterType = adapterType;
        notifyPropertyChanged(BR.adapterType);
    }

    public ConversationAdapter getConversationAdapter() {
        return mConversationAdapter;
    }

    public MediaAdapter getMediaAdapter() {
        return mMediaAdapter;
    }

    public void onGravityChangeClick(ConversationModel conversationModel, int position) {
        mConversationAdapter.changeGravity(conversationModel, position);
    }

    public void onDeleteConversationClick(int position) {
        mConversationAdapter.deleteConversation(position);
    }

    public View.OnTouchListener getTouchListener(final int position) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!view.performClick() && motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int itemCount = mConversationAdapter.getItemCount();
                    if (position == (itemCount - 1)) {
                        int currentGravity = mConversationAdapter.getData()
                                .get(position).getGravity();
                        int newGravity = (currentGravity == GravityType.LEFT)
                                ? GravityType.RIGHT : GravityType.LEFT;
                        ConversationModel conversation = new ConversationModel(newGravity);
                        mConversationAdapter.addData(conversation);
                    }
                }
                return false;
            }
        };
    }

}
