package com.framgia.englishconversation.service;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.screen.main.MainActivity;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by framgia on 11/05/2017.
 */

public class FirebaseUploadService extends BaseStorageService {
    /**
     * Action
     **/
    public final static String ACTION_UPLOAD = "com.toandoan.action.ACTION_UPLOAD";
    public final static String ACTION_UPLOAD_MULTI_FILE =
            "com.toandoan.action.ACTION_UPLOAD_MULTI_FILE";

    public final static String UPLOAD_FINNISH = "upload_finish";
    public final static String UPLOAD_ERROR = "upload_error";
    public final static String UPLOAD_PROGRESS = "upload_progress";
    public final static String UPLOAD_COMPLETE = "upload_complete";
    /**
     * Intent Extra
     **/
    public final static String EXTRA_URI = "EXTRA_URI";
    public final static String EXTRA_FILES = "EXTRA_FILES";
    public final static String EXTRA_DOWNLOAD_URL = "EXTRA_DOWNLOAD_URL";
    public final static String EXTRA_FOLDER = "EXTRA_FOLDER";
    public final static String EXTRA_MEDIA_MODEL = "EXTRA_MEDIA_MODEL";
    public final static String EXTRA_UPLOADED_PERCENT = "EXTRA_UPLOADED_PERCENT";

    public final static String PROGRESS_UPLOAD = "Uploading...";
    public final static String UPLOAD_SUCCESS = "Upload successful";
    public final static String UPLOAD_FAILED = "Upload failed";
    private static final String TAG = "FirebaseUploadService";
    private StorageReference mStorageRef;

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_FINNISH);
        filter.addAction(UPLOAD_ERROR);
        filter.addAction(UPLOAD_PROGRESS);
        filter.addAction(UPLOAD_COMPLETE);
        return filter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_UPLOAD:
                Uri uri = intent.getParcelableExtra(EXTRA_URI);
                String folder = intent.getStringExtra(EXTRA_FOLDER);
                uploadFromUri(uri, folder);
                break;
            case ACTION_UPLOAD_MULTI_FILE:
                List<MediaModel> medias = intent.getExtras().getParcelableArrayList(EXTRA_FILES);
                folder = intent.getStringExtra(EXTRA_FOLDER);
                if (medias != null && medias.size() > 0) {
                    for (MediaModel mediaModel : medias) {
                        uploadFromMediaModel(mediaModel, folder);
                    }
                }
                break;
        }
        return START_REDELIVER_INTENT;
    }

    private void uploadFromMediaModel(final MediaModel mediaModel, String folder) {
        if (mediaModel == null){
            return;
        }
        final Uri fileUri = Uri.fromFile(new File(mediaModel.getUrl()));

        taskStarted();
        showProgressNotification(PROGRESS_UPLOAD, 0, 0);

        final StorageReference photoRef = mStorageRef.child(folder)
                .child(UUID.randomUUID().toString() + fileUri.getLastPathSegment());

        photoRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgressNotification(PROGRESS_UPLOAD,
                                taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());

                        broadcastUploadUpdate(mediaModel, taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                broadcastUploadFinished(mediaModel, downloadUri);

                showUploadFinishedNotification(downloadUri, fileUri);

                taskCompleted();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                broadcastUploadError(mediaModel);
                showUploadFinishedNotification(null, fileUri);
                taskCompleted();
            }
        });
    }

    private boolean broadcastUploadUpdate(MediaModel mediaModel, long uploaded, long total) {
        if (uploaded == 0 || total == 0) return false;
        int percent = (int) ((100 * uploaded) / total);

        Intent broadcast = new Intent(UPLOAD_PROGRESS).putExtra(EXTRA_MEDIA_MODEL, mediaModel)
                .putExtra(EXTRA_UPLOADED_PERCENT, percent);
        return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
    }

    private boolean broadcastUploadFinished(MediaModel mediaModel, @Nullable Uri downloadUrl) {
        if (downloadUrl != null) {
            Intent broadcast = new Intent(UPLOAD_FINNISH).putExtra(EXTRA_MEDIA_MODEL, mediaModel)
                    .putExtra(EXTRA_URI, downloadUrl);
            return LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(broadcast);
        }
        return broadcastUploadError(mediaModel);
    }

    private boolean broadcastUploadError(MediaModel mediaModel) {
        Intent broadcast = new Intent(UPLOAD_ERROR).putExtra(EXTRA_MEDIA_MODEL, mediaModel);
        return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
    }

    private void uploadFromUri(final Uri fileUri, String folder) {

        taskStarted();
        showProgressNotification(PROGRESS_UPLOAD, 0, 0);

        final StorageReference photoRef =
                mStorageRef.child(folder).child(fileUri.getLastPathSegment());

        Log.d(TAG, "uploadFromUri:dst:" + photoRef.getPath());

        photoRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgressNotification(PROGRESS_UPLOAD,
                                taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.d(TAG, "uploadFromUri:onSuccess");

                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();

                broadcastUploadFinished(downloadUri, fileUri);
                showUploadFinishedNotification(downloadUri, fileUri);
                taskCompleted();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Uri uri = null;
                broadcastUploadFinished(uri, fileUri);
                showUploadFinishedNotification(null, fileUri);
                taskCompleted();
            }
        });
    }

    /**
     * Broadcast finished upload (success or failure).
     *
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_FINNISH : UPLOAD_ERROR;

        Intent broadcast = new Intent(action).putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        // Hide the progress notification
        dismissProgressNotification();

        // Make Intent to MainActivity
        Intent intent =
                new Intent(this, MainActivity.class).putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                        .putExtra(EXTRA_URI, fileUri)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? UPLOAD_SUCCESS : UPLOAD_FAILED;
        showFinishedNotification(caption, intent, success);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
