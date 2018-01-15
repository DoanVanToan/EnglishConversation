package com.framgia.englishconversation.screen.createPost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.service.FirebaseUploadService;

import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_MEDIA_MODEL;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_URI;

/**
 * Created by doan.van.toan on 1/12/18.
 */

public class UploadBroadcastReceiver extends BroadcastReceiver {

    private OnReceiverListenner mListenner;

    public UploadBroadcastReceiver(OnReceiverListenner listenner) {
        mListenner = listenner;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null || mListenner == null) {
            return;
        }
        switch (intent.getAction()) {
            case FirebaseUploadService.UPLOAD_PROGRESS:
                MediaModel mediaModel = getMediaOnUploadProgress(intent);
                mListenner.onUploadProgress(mediaModel);
                break;
            case FirebaseUploadService.UPLOAD_FINNISH:
                mediaModel = getMediaOnUploadFinish(intent);
                mListenner.onUploadFinnish(mediaModel);
                break;
            case FirebaseUploadService.UPLOAD_COMPLETE:
                mListenner.onUploadComplete();
                break;
            case FirebaseUploadService.UPLOAD_ERROR:
                mediaModel = getMediaOnUploadError(intent);
                mListenner.onUploadError(mediaModel);
                break;
        }
    }

    /**
     * getMedia model from intent receive from service when upload in progress
     * @param intent from service
     * @return
     */
    private MediaModel getMediaOnUploadProgress(Intent intent) {
        if (intent.getExtras() == null) {
            return null;
        }
        int percent = intent.getExtras()
                .getInt(FirebaseUploadService.EXTRA_UPLOADED_PERCENT);
        MediaModel mediaModel = intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
        if (mediaModel == null) {
            return null;
        }
        mediaModel.setUploadPercent(percent);
        return mediaModel;
    }

    /**
     * get media model from intent when a media upload finished
     * @param intent from service
     * @return
     */
    private MediaModel getMediaOnUploadFinish(Intent intent) {
        if (intent.getExtras() == null) {
            return null;
        }
        MediaModel mediaModel = intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
        if (mediaModel == null) {
            return null;
        }
        Uri downloadUri = (Uri) intent.getExtras().get(EXTRA_URI);
        if (downloadUri == null) {
            return null;
        }
        mediaModel.setUrl(downloadUri.toString());
        return mediaModel;
    }

    /**
     * get media model from intent when a media upload error
     * @param intent from service
     * @return
     */
    private MediaModel getMediaOnUploadError(Intent intent) {
        if (intent.getExtras() == null) {
            return null;
        }
        return intent.getExtras().getParcelable(EXTRA_MEDIA_MODEL);
    }

    /**
     * Listener of broacast receive when upload file
     */
    public interface OnReceiverListenner {
        /**
         * Return media model when receive progress from service
         *
         * @param mediaModel
         */
        void onUploadProgress(MediaModel mediaModel);

        /**
         * return media model when a media was uploaded successfull
         *
         * @param mediaModel
         */
        void onUploadFinnish(MediaModel mediaModel);

        /**
         * called when all files was uploaded
         */
        void onUploadComplete();

        /**
         * r eturn media model when media file upload error
         *
         * @param mediaModel
         */
        void onUploadError(MediaModel mediaModel);
    }
}
