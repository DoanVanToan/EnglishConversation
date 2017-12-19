package com.framgia.englishconversation.widget.dialog.recordingAudio;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.databinding.DialogRecordingAudioBinding;
import com.framgia.englishconversation.record.model.AudioSource;
import com.framgia.englishconversation.widget.dialog.BaseDialog;

/**
 * Created by fs-sournary.
 * Date on 12/1/17.
 * Description:
 */

public class RecordingAudioDialog extends BaseDialog {

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
        Bundle bundle = getArguments();
        mFileName = bundle.getString(RecordingAudioBuilder.EXTRA_FILE_NAME);
        mFilePath = bundle.getString(RecordingAudioBuilder.EXTRA_FILE_PATH);
        mAudioSource = (AudioSource) bundle.getSerializable(RecordingAudioBuilder.EXTRA_AUDIO_SOURCE);
        mRecordingAudioViewModel = new RecordingAudioViewModel(this);
        mBinding.setViewModel(mRecordingAudioViewModel);
        builder.setView(mBinding.getRoot());
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

    public AudioSource getAudioSource() {
        return mAudioSource;
    }

    public String getFilePath() {
        return mFilePath;
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
