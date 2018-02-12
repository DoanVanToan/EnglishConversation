package com.framgia.englishconversation.screen.changepassword;

import android.app.ProgressDialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;
import android.content.Context;
import android.widget.Toast;

import com.framgia.englishconversation.R;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Sony on 2/7/2018.
 */

public class ChangePasswordViewModel extends BaseObservable implements
        ChangePasswordContract.ViewModel {
    private Context mContext;
    private String mCurrentPassword;
    private String mNewPassword;
    private String mConfirmPassword;
    private String mPasswordError;
    private String mPasswordConfirmError;
    private FirebaseUser mUser;
    private ChangePasswordContract.Presenter mPresenter;
    private DissmissDialogListener mDissmissDialogListener;
    private ProgressDialog mDialog;

    public ChangePasswordViewModel(Context context, DissmissDialogListener dissmissDialogListener) {
        mContext = context;
        mDissmissDialogListener = dissmissDialogListener;
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.msg_loading));
    }

    @Bindable
    public FirebaseUser getUser() {
        return mUser;
    }

    public void setUser(FirebaseUser user) {
        mUser = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public String getPasswordError() {
        return mPasswordError;
    }

    public void setPasswordError(String passwordError) {
        mPasswordError = passwordError;
        notifyPropertyChanged(BR.passwordError);
    }

    @Bindable
    public String getPasswordConfirmError() {
        return mPasswordConfirmError;
    }

    public void setPasswordConfirmError(String passwordConfirmError) {
        mPasswordConfirmError = passwordConfirmError;
        notifyPropertyChanged(BR.passwordConfirmError);
    }

    @Bindable
    public String getCurrentPassword() {
        return mCurrentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        mCurrentPassword = currentPassword;
        notifyPropertyChanged(BR.currentPassword);
    }

    @Bindable
    public String getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(String newPassword) {
        mNewPassword = newPassword;
        notifyPropertyChanged(BR.newPassword);
    }

    @Bindable
    public String getConfirmPassword() {
        return mConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        mConfirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStart();
    }

    @Override
    public void onGetCurrentUserSuccess(FirebaseUser firebaseUser) {
        setUser(firebaseUser);
    }

    @Override
    public void setPresenter(ChangePasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onChangPasswordSuccess() {
        onDismissDialogProcess();
        Toast.makeText(mContext, R.string.msg_change_password_success, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onChangePasswordFailed() {
        onDismissDialogProcess();
        Toast.makeText(mContext, R.string.msg_change_password_failed, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPassWordError(int message) {
        setPasswordError(mContext.getString(message));
    }

    @Override
    public void onConfirmPasswordError(int message) {
        setPasswordConfirmError(mContext.getString(message));

    }

    @Override
    public void onChangePasswordClick() {
        if (!mPresenter.validateInput(mCurrentPassword, mNewPassword, mConfirmPassword)) {
            return;
        }
        dismissDialog();
        if (mUser != null) {
            mPresenter.changePassword(mUser, mCurrentPassword, mNewPassword);
            onShowDialogProcess();
        }
    }

    @Override
    public void onCancelClick() {
        dismissDialog();
    }

    @Override
    public void dismissDialog() {
        mDissmissDialogListener.onDismissDialog();
    }

    @Override
    public void onShowDialogProcess() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void onDismissDialogProcess() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

}
