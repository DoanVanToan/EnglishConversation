package com.framgia.englishconversation.widget.dialog.recordingAudio;

import android.app.Dialog;
import android.content.DialogInterface;
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
import java.util.Locale;

/**
 * Created by fs-sournary.
 * Date on 12/1/17.
 * Description:
 */

public class RecordingAudioDialog extends BaseDialog {

    private static final int SECOND_PER_MINUTE = 60;

    private MediaRecorder mMediaRecorder;

    private RecordingAudioViewModel mRecordingAudioViewModel;
    private String mFilePath;
    private String mFileName;
    private AudioSource mAudioSource;
    private OnRecordingAudioListener mOnRecordingAudioListener;
    private DialogRecordingAudioBinding mBinding;

    public RecordingAudioDialog() {
    }

    public static RecordingAudioDialog newInstance() {
        return new RecordingAudioDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_recording_audio,
                null,
                false
        );
        mRecordingAudioViewModel = new RecordingAudioViewModel(this);
        mBinding.setViewModel(mRecordingAudioViewModel);
        View view = mBinding.getRoot();
        builder.setView(view);
        Bundle bundle = getArguments();
        mFileName = bundle.getString(RecordingAudioBuilder.EXTRA_FILE_NAME);
        mFilePath = bundle.getString(RecordingAudioBuilder.EXTRA_FILE_PATH);
        mAudioSource = (AudioSource) bundle.getSerializable(RecordingAudioBuilder.EXTRA_AUDIO_SOURCE);
        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() == null) {
            return alertDialog;
        }
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!mRecordingAudioViewModel.isCancelClick()) {
            mOnRecordingAudioListener.onRecordingAudioClick(mFilePath, mFileName);
        } else {
            mOnRecordingAudioListener.onRecordCancel();
        }
    }

    private void startRecordingAudio() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(mAudioSource.getSource());
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

    public void releaseRecordingAudio() {
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

    public void updateDuration(long duration) {
        long s = duration % SECOND_PER_MINUTE;
        long m = (duration / SECOND_PER_MINUTE) % SECOND_PER_MINUTE;
        String format = String.format(Locale.getDefault(), "%02d:%02d", m, s);
        mBinding.textRecordingDuration.setText(format);
    }

    public void setOnRecordingAudioClickListener(OnRecordingAudioListener
                                                         onRecordingAudioListener) {
        mOnRecordingAudioListener = onRecordingAudioListener;
    }

    public interface OnRecordingAudioListener {
        void onRecordingAudioClick(String filePath, String fileName);

        void onRecordCancel();
    }

}
