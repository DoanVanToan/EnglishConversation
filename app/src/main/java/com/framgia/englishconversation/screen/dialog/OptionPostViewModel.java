package com.framgia.englishconversation.screen.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;

import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.TimelineModel;

/**
 * Created by Sony on 1/24/2018.
 */

public class OptionPostViewModel extends BaseObservable implements OptionPostContract.ViewModel {

    private OptionPostPresenter mPresenter;
    private TimelineModel mTimelineModel;
    private Context mContext;
    private DialogListener mDialogListener;

    public OptionPostViewModel(Context context, TimelineModel timelineModel,
                               DialogListener dialogListener) {
        mContext = context;
        mTimelineModel = timelineModel;
        mDialogListener = dialogListener;
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(OptionPostContract.Presenter presenter) {
        mPresenter = (OptionPostPresenter) presenter;
    }

    @Override
    public void onClickEditPost() {
        mDialogListener.onClickEditPost();
    }

    @Override
    public void onClickDeletePost() {
        mDialogListener.onClickDeletePost();
        showConfirmDeleteDialog();
    }

    @Override
    public void showConfirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.title_confirm_delete_timeline)
                .setCancelable(true)
                .setNegativeButton(R.string.title_cancel, null)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deletePost(mTimelineModel);
                            }
                        }).show();

    }

}
