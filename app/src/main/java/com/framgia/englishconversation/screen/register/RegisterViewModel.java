package com.framgia.englishconversation.screen.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.screen.editProfile.EditProfileActivity;
import com.framgia.englishconversation.screen.forgotPassword.ForgotPasswordActivity;
import com.framgia.englishconversation.screen.main.MainActivity;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the Register screen.
 */

public class RegisterViewModel extends BaseObservable implements RegisterContract.ViewModel {

    private RegisterContract.Presenter mPresenter;
    private Context mContext;
    private RegisterActivity mActivity;
    private Navigator mNavigator;
    private ProgressDialog mDialog;

    private String mEmail;
    private String mPassword;
    private String mPasswordConfirm;

    public RegisterViewModel(Context context, Navigator navigator) {
        mContext = context;
        mActivity = (RegisterActivity) context;
        mNavigator = navigator;
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(mContext.getString(R.string.msg_pls_register));
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
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRegisterSuccess(FirebaseUser user) {
        if (!TextUtils.isEmpty(user.getDisplayName())) {
            mNavigator.startActivity(MainActivity.getInstance(mContext));
        } else {
            mNavigator.startActivity(EditProfileActivity.getInstance(mContext));
        }
    }

    @Override
    public void onRegisterFailed(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void showProgressDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void onEmptyEmail() {
        mNavigator.showToast(R.string.empty_email);
    }

    @Override
    public void onEmptyPassword() {
        mNavigator.showToast(R.string.empty_password);
    }

    @Override
    public void onEmptyPasswordConfirm() {
        mNavigator.showToast(R.string.empty_password_confirm);
    }

    @Override
    public void onPasswordConfirmNotCorrect() {
        mNavigator.showToast(R.string.empty_password_confirm_incorrect);
    }

    @Override
    public void onRegisterClick() {
        mPresenter.registerAccount(mEmail, mPassword, mPasswordConfirm);
    }

    @Override
    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onForgotPwClick() {
        mNavigator.finishActivity();
        mNavigator.startActivity(ForgotPasswordActivity.getInstance(mContext));
    }

    @Override
    public void onLoginClick() {
        mNavigator.finishActivity();
    }

    @Bindable
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getPasswordConfirm() {
        return mPasswordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        mPasswordConfirm = passwordConfirm;
        notifyPropertyChanged(BR.passwordConfirm);
    }
}
