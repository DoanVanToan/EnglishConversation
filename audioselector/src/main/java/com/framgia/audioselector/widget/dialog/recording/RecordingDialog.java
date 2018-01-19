package com.framgia.audioselector.widget.dialog.recording;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.framgia.audioselector.R;
import com.framgia.audioselector.databinding.DialogRecordingBinding;
import com.framgia.audioselector.widget.dialog.BaseDialog;

/**
 * Created by fs-sournary.
 * Data: 1/19/18.
 * Description:
 */

public class RecordingDialog extends BaseDialog {

    public static final String EXTRA_FILE_PATH =
            "com.framgia.audioselector.bundle.extra.EXTRA_FILE_PATH";
    public static final String EXTRA_FILE_NAME =
            "com.framgia.audioselector.bundle.extra.EXTRA_FILE_NAME";
    private String mFileName;
    private String mFilePath;

    private RecordingContract.ViewModel mViewModel;
    private OnDismissRecordingClickListener mListener;

    public static RecordingDialog getInstance(String filePath, String fileName) {
        RecordingDialog dialog = new RecordingDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FILE_PATH, filePath);
        bundle.putString(EXTRA_FILE_NAME, fileName);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mViewModel = new RecordingViewModel(this);
        mFilePath = bundle.getString(EXTRA_FILE_PATH);
        mFileName = bundle.getString(EXTRA_FILE_NAME);
        if (mFilePath != null) {
            mViewModel.initRecord(mFilePath);
        }
        if (mFileName != null) {
            // TODO: Handle when fileName != null
        }
        RecordingContract.Presenter presenter = new RecordingPresenter(mViewModel);
        mViewModel.setPresenter(presenter);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        DialogRecordingBinding binding =
                DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.dialog_recording,
                        null, false);
        binding.setViewModel((RecordingViewModel) mViewModel);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog = builder.create();
        dialog.setView(binding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() == null) {
            return dialog;
        }
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mViewModel.isCancelClick()) {
            mListener.onCancel();
        } else {
            mListener.onStore(mFileName, mFilePath);
        }
    }

    public void setListener(OnDismissRecordingClickListener listener) {
        mListener = listener;
    }

    public interface OnDismissRecordingClickListener {
        void onStore(String fileName, String filePath);

        void onCancel();
    }
}
