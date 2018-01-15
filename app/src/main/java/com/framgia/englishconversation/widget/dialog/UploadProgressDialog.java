package com.framgia.englishconversation.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.framgia.englishconversation.R;

/**
 * Created by doan.van.toan on 1/12/18.
 */

public class UploadProgressDialog extends ProgressDialog {
    private final static int DEF_MAX_PROGRESS = 100;

    public UploadProgressDialog(Context context) {
        super(context);
        initDialog();
    }

    private void initDialog(int title, String message, int style) {
        setTitle(title);
        setMessage(message);
        setIndeterminate(true);
        setProgressStyle(style);
        setCancelable(false);
        setMax(DEF_MAX_PROGRESS);
    }

    public void setProgressPercent(int max, int progress) {
        setIndeterminate(false);
        setMax(max);
        setProgress(progress);
    }

    public void setProgressPercent(int progress) {
        setProgressPercent(DEF_MAX_PROGRESS, progress);
    }

    private void initDialog() {
        initDialog(R.string.title_upload_file,
                getContext().getString(R.string.msg_uploading),
                ProgressDialog.STYLE_HORIZONTAL);
    }
}
