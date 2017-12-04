package com.framgia.englishconversation.widget.dialog.recordingAudio;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.databinding.DialogRecordingAudioBinding;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.widget.dialog.BaseDialog;

import java.io.IOException;

/**
 * Created by fs-sournary.
 * Date on 12/1/17.
 * Description:
 */

public class RecordingAudioDialog extends BaseDialog implements View.OnClickListener {

    private MediaRecorder mMediaRecorder;

    private RecordingAudioViewModel mRecordingAudioViewModel;
    private String mFilePath;
    private String mFileName;
    private AudioSource mAudioSource;
    private boolean mIsRecording;

    public RecordingAudioDialog() {
        mIsRecording = false;
    }

    public static RecordingAudioDialog newInstance() {
        return new RecordingAudioDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        DialogRecordingAudioBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_recording_audio,
                null,
                false
        );
        mRecordingAudioViewModel = new RecordingAudioViewModel(this);
        binding.setViewModel(mRecordingAudioViewModel);
        View view = binding.getRoot();
        builder.setView(view);
        Bundle bundle = getArguments();
        mFileName = bundle.getString(AudioRecorder.EXTRA_FILE_NAME);
        mFilePath = bundle.getString(AudioRecorder.EXTRA_FILE_PATH);
        mAudioSource = (AudioSource) bundle.getSerializable(AudioRecorder.EXTRA_AUDIO_SOURCE);
        binding.textCancel.setOnClickListener(this);
        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() == null) {
            return alertDialog;
        }
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    private void startRecordingAudio() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioEncoder(mAudioSource.getSource());
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setOutputFile(mFilePath);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(
                    getActivity(),
                    "Sorry, you can not start recording audio mode. Please try again later!",
                    Toast.LENGTH_SHORT
            ).show();
        }
        mMediaRecorder.start();
    }

    private void stopRecordingAudio() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    public void onRecordingAudio(boolean isRecording) {
        if (isRecording) {
            stopRecordingAudio();
        } else {
            startRecordingAudio();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_cancel:
                mRecordingAudioViewModel.dismissDialog();
                break;
            case R.id.image_mic:
                onRecordingAudio(mIsRecording);
                mIsRecording = !mIsRecording;
                break;
            default:
                break;
        }
    }
}
