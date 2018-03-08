package com.framgia.englishconversation.screen.basePost;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.framgia.audioselector.data.model.Audio;
import com.framgia.audioselector.screen.audioselector.AudioSelectorActivity;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.ConversationModel;
import com.framgia.englishconversation.data.model.GravityType;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.screen.createPost.AdapterType;
import com.framgia.englishconversation.screen.createPost.ConversationAdapter;
import com.framgia.englishconversation.screen.createPost.MediaAdapter;
import com.framgia.englishconversation.screen.createPost.UploadBroadcastReceiver;
import com.framgia.englishconversation.service.FirebaseUploadService;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.Utils;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.framgia.englishconversation.widget.dialog.UploadProgressDialog;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioBuilder;
import com.framgia.englishconversation.widget.dialog.recordingAudio.RecordingAudioDialog;
import com.framgia.videoselector.data.model.VideoModel;
import com.framgia.videoselector.screen.VideoSelectorActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;
import static com.framgia.englishconversation.service.BaseStorageService.POST_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService.ACTION_UPLOAD_MULTI_FILE;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FILES;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FOLDER;
import static com.framgia.englishconversation.utils.Constant.RequestCode.REQUEST_PERMISSION;
import static com.framgia.videoselector.screen.VideoSelectorActivity.EXTRA_DATA;

/**
 * Created by Sony on 2/2/2018.
 */

public abstract class BasePostViewModel extends BaseObservable
        implements BasePostContract.ViewModel {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int SELECT_IMAGE_REQUEST = 2;
    private static final int REQUEST_RECORD_VIDEO = 3;
    private static final int RC_AUDIO_SELECTOR = 10;
    private static final int LIMIT_IMAGES = 10;
    private static final String[] PERMISSION = new String[]{
            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @AdapterType
    private int mAdapterType;
    private int mCurrentTypeClicked;
    private String mAudioFilePath;
    private boolean mIsUploading;
    private UserModel mUser;
    private TimelineModel mTimelineModel;
    private RecordingAudioDialog mRecordingAudioDialog;
    private BasePostContract.Presenter mPresenter;
    private Activity mActivity;
    private Navigator mNavigator;
    private UploadProgressDialog mProgressDialog;
    private UploadBroadcastReceiver mReceiver;
    private MediaAdapter mMediaAdapter;
    private ConversationAdapter mConversationAdapter;
    private SimpleExoPlayer mExoPlayer;
    private ExoPlayerListener mPlayerListener;
    private TimelineModel mTimelineModelRevision;

    public BasePostViewModel(Activity activity, Navigator navigator) {
        mActivity = activity;
        mNavigator = navigator;
        mTimelineModel = new TimelineModel();
        mProgressDialog = new UploadProgressDialog(mActivity);
        mAdapterType = AdapterType.MEDIA;
        mRecordingAudioDialog = RecordingAudioDialog.newInstance();
        mMediaAdapter = new MediaAdapter(this);
        mConversationAdapter = new ConversationAdapter(mActivity, this);
        mPlayerListener = new ExoPlayerListener();
    }

    public BasePostViewModel(Activity activity, Navigator navigator, TimelineModel timelineModel) {
        mActivity = activity;
        mNavigator = navigator;
        mTimelineModel = timelineModel;
        mProgressDialog = new UploadProgressDialog(mActivity);
        mAdapterType = AdapterType.MEDIA;
        mRecordingAudioDialog = RecordingAudioDialog.newInstance();
        mMediaAdapter = new MediaAdapter(this);
        mConversationAdapter = new ConversationAdapter(mActivity, this);
        mPlayerListener = new ExoPlayerListener();
        initView(timelineModel);
        try {
            mTimelineModelRevision = (TimelineModel) timelineModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

    private void initView(TimelineModel timelineModel) {
        if (timelineModel.getMedias() != null) {
            mAdapterType = AdapterType.MEDIA;
            mMediaAdapter.setData(timelineModel.getMedias());
        }
        if (timelineModel.getConversations() != null) {
            mAdapterType = AdapterType.CONVERSATION;
            mConversationAdapter.setData(timelineModel.getConversations());
        }
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        registerBroadcastReceiver();
    }

    private void registerBroadcastReceiver() {
        mReceiver = new UploadBroadcastReceiver(new UploadBroadcastReceiver.OnReceiverListenner() {
            @Override
            public void onUploadProgress(MediaModel mediaModel) {
                handleUploadProgress(mediaModel);
            }

            @Override
            public void onUploadFinnish(MediaModel mediaModel) {
                handleFinish(mediaModel);
            }

            @Override
            public void onUploadComplete() {
                mIsUploading = false;
                mPresenter.submitPost(mTimelineModel);
            }

            @Override
            public void onUploadError(MediaModel mediaModel) {
                String errorMsg = String.format(mActivity.getString(R.string.msg_upload_error),
                        mediaModel.getName());
                mNavigator.showToast(errorMsg);
            }
        });
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mActivity);
        manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
    }

    @Override
    public void onPause() {
        if (SDK_INT <= Build.VERSION_CODES.M && mCurrentTypeClicked != MediaModel.MediaType.AUDIO
                && mCurrentTypeClicked != MediaModel.MediaType.VIDEO) {
            storeDataWhenReleasePlayer();
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
        if (SDK_INT > Build.VERSION_CODES.M && mCurrentTypeClicked != MediaModel.MediaType.AUDIO
                && mCurrentTypeClicked != MediaModel.MediaType.VIDEO) {
            storeDataWhenReleasePlayer();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        setAdapterType(AdapterType.MEDIA);
        switch (requestCode) {
            case SELECT_IMAGE_REQUEST:
                List<Image> images =
                        data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                if (images == null || images.size() == 0) break;
                addPostImage(images);
                break;
            case REQUEST_RECORD_VIDEO:
                updateVideoMedia(data);
                break;
            case RC_AUDIO_SELECTOR:
                ArrayList<Audio> audios =
                        data.getParcelableArrayListExtra(AudioSelectorActivity.EXTRA_AUDIO);
                Audio audio = audios.get(0);
                storeDataWhenReleasePlayer();
                MediaModel record = new MediaModel(MediaModel.MediaType.AUDIO);
                record.setId(audio.getId());
                record.setUrl(audio.getPath());
                record.setName(audio.getName());
                addPostAudioRecord(record);
                initializeAudioPlayer(audio.getPath());
                break;
            default:
                break;
        }
    }

    private void handleUploadProgress(MediaModel model) {
        if (model == null || mTimelineModel == null) {
            return;
        }
        switch (mAdapterType) {
            case AdapterType.CONVERSATION:
                handleUploadConversationFile(model);
                break;
            case AdapterType.MEDIA:
                handleUploadMediaFiles(model);
                break;
            default:
                break;
        }
    }

    private void handleUploadConversationFile(MediaModel model) {
        int totalMediaFile = getTotalMediaFile(mTimelineModel.getConversations());
        if (mTimelineModel.getConversations() == null || totalMediaFile == 0) {
            return;
        }
        int uploadPercent = 0;
        for (ConversationModel conversationModel : mTimelineModel.getConversations()) {
            if (conversationModel.getMediaModel() == null) {
                break;
            }
            MediaModel mediaTimeLine = conversationModel.getMediaModel();
            updateMediaModel(mediaTimeLine, model);
            uploadPercent += mediaTimeLine.getUploadPercent();
        }
        mProgressDialog.setProgressPercent(uploadPercent / totalMediaFile);
    }

    private int getTotalMediaFile(List<ConversationModel> conversations) {
        if (conversations == null) {
            return 0;
        }
        int result = 0;
        for (ConversationModel conversationModel : conversations) {
            if (conversationModel != null && conversationModel.getMediaModel() != null) {
                result++;
            }
        }
        return result;
    }

    private void handleUploadMediaFiles(MediaModel model) {
        if (mTimelineModel.getMedias() == null) {
            return;
        }
        int uploadPercent = 0;
        for (MediaModel mediaTimeLine : mTimelineModel.getMedias()) {
            updateMediaModel(mediaTimeLine, model);
            uploadPercent += mediaTimeLine.getUploadPercent();
        }
        mProgressDialog.setProgressPercent(uploadPercent / mTimelineModel.getMedias().size());
    }

    private void updateMediaModel(MediaModel mediaTimeLine, MediaModel model) {
        if (mediaTimeLine != null && mediaTimeLine.getId().equals(model.getId())) {
            mediaTimeLine.setUploadPercent(model.getUploadPercent());
        }
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
    public void onSubmitPost() {
        updateTimelineModel();
        if (mAdapterType == AdapterType.CONVERSATION) {
            mTimelineModel.setConversations(mConversationAdapter.getValidatedData());
            mTimelineModel.setMedias(null);
        } else {
            mTimelineModel.setConversations(null);
        }
        switch (mTimelineModel.getPostType()) {
            case MediaModel.MediaType.CONVERSATION:
                mTimelineModel.setConversations(mConversationAdapter.getData());
                List<MediaModel> conversationMedias =
                        getConversationMedias(mTimelineModel.getConversations());
                if (conversationMedias == null || conversationMedias.isEmpty()
                        || getListConversationUpload(mTimelineModel).size() == 0) {
                    mPresenter.submitPost(mTimelineModel);
                } else {
                    uploadFiles(getListConversationUpload(mTimelineModel));
                }
                break;
            case MediaModel.MediaType.ONLY_TEXT:
                if (mTimelineModel.getContent() != null) {
                    mPresenter.submitPost(mTimelineModel);
                }
                break;
            default:
                mTimelineModel.setMedias(mMediaAdapter.getData());
                if (getListMediaUpload(mTimelineModel).size() != 0) {
                    uploadFiles(getListMediaUpload(mTimelineModel));
                } else {
                    mPresenter.submitPost(mTimelineModel);
                }
                break;
        }
    }

    private List<MediaModel> getListConversationUpload(TimelineModel timelineModel) {
        List<MediaModel> mediaModels = new ArrayList<>();
        for (ConversationModel conversationModel : timelineModel.getConversations()) {
            if (conversationModel.getMediaModel() != null
                    && conversationModel.getMediaModel().getUrl() != null
                    && !conversationModel.getMediaModel().getUrl().startsWith(Constant.HTTPS)) {
                mediaModels.add(conversationModel.getMediaModel());
            }
        }
        return mediaModels;
    }

    private List<MediaModel> getListMediaUpload(TimelineModel timelineModel) {
        List<MediaModel> mediaModels = new ArrayList<>();
        for (MediaModel mediaModel : timelineModel.getMedias()) {
            if (!mediaModel.getUrl().startsWith(Constant.HTTPS)) {
                mediaModels.add(mediaModel);
            }
        }
        return mediaModels;
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
        mProgressDialog.show();
        mActivity.startService(
                new Intent(mActivity, FirebaseUploadService.class).putParcelableArrayListExtra(
                        EXTRA_FILES, (ArrayList<? extends Parcelable>) mediaModels)
                        .putExtra(EXTRA_FOLDER, POST_FOLDER)
                        .setAction(ACTION_UPLOAD_MULTI_FILE));
    }

    @Override
    public void onSubmitPostSuccess() {
        mNavigator.finishActivity();
    }

    @Override
    public void onSubmitPostFailed(String msg) {
        mNavigator.showToast(msg);
        mProgressDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION && isEnablePermission(permissions, grantResults)) {
            switch (mCurrentTypeClicked) {
                case MediaModel.MediaType.AUDIO:
                    onAudioRecordingClick();
                    break;
                case MediaModel.MediaType.CONVERSATION:
                    onCreateConversationClick();
                    break;
                case MediaModel.MediaType.VIDEO:
                    onVideoPickerClick();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
    }

    @Override
    public void setPresenter(BasePostContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void initializeAudioPlayer(String audioFilePath) {
        if (mExoPlayer != null) {
            storeDataWhenReleasePlayer();
        }
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mActivity),
                new DefaultTrackSelector(), new DefaultLoadControl());
        Uri uri = Uri.parse(audioFilePath);
        mExoPlayer.prepare(getMediaSource(uri), true, false);
        mExoPlayer.addListener(mPlayerListener);
        setExoPlayer(mExoPlayer);
    }

    private MediaSource getMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(mActivity, Constant.USER_AGENT),
                new DefaultExtractorsFactory(), null, null);
    }

    private void storeDataWhenReleasePlayer() {
        if (mExoPlayer == null) {
            return;
        }
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private boolean isEnablePermission(String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void updateVideoMedia(Intent intent) {
        mTimelineModel.getMedias().clear();

        ArrayList<VideoModel> result = intent.getParcelableArrayListExtra(EXTRA_DATA);
        VideoModel videoModel = result.get(0);
        MediaModel mediaModel = new MediaModel(MediaModel.MediaType.VIDEO);
        mediaModel.setUrl(videoModel.getFilePath());
        mediaModel.setId(UUID.randomUUID().toString());

        mTimelineModel.getMedias().add(mediaModel);
        mMediaAdapter.setData(mTimelineModel.getMedias());

        initializeAudioPlayer(videoModel.getFilePath());
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
        storeDataWhenReleasePlayer();
        mTimelineModel.getMedias().remove(mediaModel);
        mMediaAdapter.removeItem(mediaModel);
    }

    private void showAudioDialog() {
        if (mActivity.getExternalCacheDir() == null) {
            return;
        }
        String fileName = mActivity.getString(R.string.prefix_audio_file_name)
                + System.currentTimeMillis()
                + Constant.DEFAULT_FORMAT_AUDIO;
        mAudioFilePath = mActivity.getExternalCacheDir().getAbsolutePath() + "/" + fileName;
        RecordingAudioBuilder.with((AppCompatActivity) mActivity, mRecordingAudioDialog)
                .setFileName(fileName)
                .setFilePath(mAudioFilePath)
                .setAudioSource(AudioSource.MIC)
                .showRecordingAudioFromActivity();
    }

    private void onAudioConversationClick(final ConversationModel conversations) {
        showAudioDialog();
        RecordingAudioDialog.OnRecordingAudioListener recordingAudioClickListener =
                new RecordingAudioDialog.OnRecordingAudioListener() {
                    @Override
                    public void onRecordingAudioClick(String filePath, String fileName) {
                        MediaModel record = new MediaModel(MediaModel.MediaType.AUDIO);
                        record.setId(UUID.randomUUID().toString());
                        record.setUrl(filePath);
                        record.setName(fileName);
                        conversations.setMediaModel(record);
                    }

                    @Override
                    public void onRecordCancel() {
                        // no ops
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
                        storeDataWhenReleasePlayer();
                        MediaModel record = new MediaModel(MediaModel.MediaType.AUDIO);
                        record.setId(UUID.randomUUID().toString());
                        record.setUrl(filePath);
                        record.setName(fileName);
                        addPostAudioRecord(record);
                        initializeAudioPlayer(filePath);
                    }

                    @Override
                    public void onRecordCancel() {
                        // no ops
                    }
                };
        mRecordingAudioDialog.setOnRecordingAudioClickListener(recordingAudioClickListener);
    }

    public void onAudioClick() {
        mNavigator.startActivityForResult(
                AudioSelectorActivity.getInstance(mActivity), RC_AUDIO_SELECTOR);
    }

    public void onVideoPickerClick() {
        mCurrentTypeClicked = MediaModel.MediaType.VIDEO;
        if (!Utils.isAllowPermision(mActivity, PERMISSION)) {
            return;
        }
        Intent intent = VideoSelectorActivity.getInstance(mActivity);
        mNavigator.startActivityForResult(intent, REQUEST_RECORD_VIDEO);
    }

    public void onCreateConversationClick() {
        mCurrentTypeClicked = MediaModel.MediaType.CONVERSATION;
        if (!Utils.isAllowPermision(mActivity, PERMISSION)) {
            return;
        }
        setAdapterType(AdapterType.CONVERSATION);
    }

    @Override
    public void onImagePickerClick() {
        selectImage();
    }

    @Override
    public void onPlacePickerClick() {
        setAdapterType(AdapterType.MEDIA);
        openPlacePicker();
    }

    @Bindable
    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }

    public void setExoPlayer(SimpleExoPlayer exoPlayer) {
        mExoPlayer = exoPlayer;
        notifyPropertyChanged(BR.exoPlayer);
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
                        int currentGravity =
                                mConversationAdapter.getData().get(position).getGravity();
                        int newGravity = (currentGravity == GravityType.LEFT) ? GravityType.RIGHT
                                : GravityType.LEFT;
                        ConversationModel conversation = new ConversationModel(newGravity);
                        mConversationAdapter.addData(conversation);
                    }
                }
                return false;
            }
        };
    }

    public void showMenu(View view, final int position, final ConversationModel conversationModel) {
        Utils.hideKeyBoard(mActivity);
        PopupMenu popupMenu = new PopupMenu(mActivity, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_record:
                        onAudioConversationClick(conversationModel);
                        return true;
                    case R.id.action_preview_audio:
                        onPreviewAudioClick(conversationModel);
                        return true;
                    case R.id.action_remove_audio:
                        conversationModel.setMediaModel(null);
                        return true;
                    case R.id.action_delete_conversation_item:
                        onDeleteConversationClick(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.menu_item_creating_conversation);
        if (conversationModel.getMediaModel() == null) {
            popupMenu.getMenu().findItem(R.id.action_remove_audio).setVisible(false);
            popupMenu.getMenu().findItem(R.id.action_preview_audio).setVisible(false);
        }
        popupMenu.show();
    }

    private void onPreviewAudioClick(ConversationModel conversationModel) {
        if (conversationModel.getMediaModel() == null || mExoPlayer != null) {
            return;
        }
        String path = conversationModel.getMediaModel().getUrl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mActivity),
                new DefaultTrackSelector(), new DefaultLoadControl());
        mExoPlayer.seekTo(0);
        Uri uri = Uri.parse(path);
        mExoPlayer.prepare(getMediaSource(uri), true, false);
        mExoPlayer.setPlayWhenReady(true);
        setExoPlayer(mExoPlayer);
    }

    private class ExoPlayerListener implements ExoPlayer.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups,
                                    TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == ExoPlayer.STATE_ENDED) {
                mExoPlayer.seekToDefaultPosition();
                mExoPlayer.setPlayWhenReady(false);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }
    }
}
