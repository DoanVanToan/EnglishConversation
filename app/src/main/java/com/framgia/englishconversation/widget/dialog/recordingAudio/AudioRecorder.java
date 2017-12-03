package com.framgia.englishconversation.widget.dialog.recordingAudio;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.framgia.englishconversation.record.model.AudioSource;

/**
 * Created by fs-sournary.
 * Date on 12/3/17.
 * Description:
 */

public class AudioRecorder {

    public static final String EXTRA_FILE_NAME =
            "com.framgia.englishconversation.intent.extra.EXTRA_FILE_NAME";
    public static final String EXTRA_FILE_PATH =
            "com.framgia.englishconversation.intent.extra.EXTRA_FILE_PATH";
    public static final String EXTRA_AUDIO_SOURCE =
            "com.framgia.englishconversation.intent.extra.EXTRA_AUDIO_SOURCE";
    public static final String TAG_RECORDING_AUDIO = "RecordingAudioDialog";

    private String mFileName;
    private String mFilePath;
    private AudioSource mAudioSource;

    private AppCompatActivity mActivity;
    private DialogFragment mDialog;

    public AudioRecorder(AppCompatActivity activity, DialogFragment dialog) {
        mActivity = activity;
        mDialog = dialog;
        if (activity.getExternalCacheDir() == null) {
            return;
        }
        mFileName = "RecordingAudio_" + System.currentTimeMillis() + ".3gp";
        mFilePath = activity.getExternalCacheDir().getAbsolutePath() + "/" + mFileName;
    }

    public static AudioRecorder with(AppCompatActivity activity, DialogFragment dialog) {
        return new AudioRecorder(activity, dialog);
    }

    public AudioRecorder setFileName(String fileName) {
        mFileName = fileName;
        return this;
    }

    public AudioRecorder setFilePath(String filePath) {
        mFilePath = filePath;
        return this;
    }

    public AudioRecorder setAudioSource(AudioSource audioSource) {
        mAudioSource = audioSource;
        return this;
    }

    public void showRecordingAudioFromActivity() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FILE_NAME, mFileName);
        bundle.putString(EXTRA_FILE_PATH, mFilePath);
        bundle.putSerializable(EXTRA_AUDIO_SOURCE, mAudioSource);
        mDialog.setArguments(bundle);
        mDialog.show(mActivity.getSupportFragmentManager(), TAG_RECORDING_AUDIO);
    }

}
