package com.framgia.englishconversation.screen.forgotPassword;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the ForgotPassword screen.
 */

public class ForgotPasswordViewModel extends BaseObservable
        implements ForgotPasswordContract.ViewModel {

    private Context mContext;
    private ForgotPasswordContract.Presenter mPresenter;
    private String mEmail;
    private Navigator mNavigator;
    private String mEmailError;

    ForgotPasswordViewModel(Context context, Navigator navigator) {
        mContext = context;
        mNavigator = navigator;
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
    public void setPresenter(ForgotPasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResetPasswordClick() {
        if (!mPresenter.validateInput(mEmail)) {
            return;
        }
        mPresenter.resetPassword(mEmail);
    }

    @Override
    public void onInputEmailError(int message) {
        setEmailError(mContext.getString(message));
    }

    @Override
    public void onResetPasswordSuccess() {
        mNavigator.showToast(mContext.getString(R.string.msg_reset_password_success));
    }

    @Override
    public void onResetPasswordFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Bindable
    public String getEmail() {
        return mEmail;
    }

    @Bindable
    public String getEmailError() {
        return mEmailError;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setEmailError(String emailError) {
        mEmailError = emailError;
        notifyPropertyChanged(BR.emailError);
    }
}
