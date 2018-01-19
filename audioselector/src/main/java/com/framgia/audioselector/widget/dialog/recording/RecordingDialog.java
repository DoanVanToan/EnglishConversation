package com.framgia.audioselector.widget.dialog.recording;

import android.app.Dialog;
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

    private RecordingContract.ViewModel mViewModel;

    public static RecordingDialog getInstance() {
        return new RecordingDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new RecordingViewModel();
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
}
