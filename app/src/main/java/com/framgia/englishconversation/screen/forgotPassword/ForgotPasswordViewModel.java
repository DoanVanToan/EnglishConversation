package com.framgia.englishconversation.screen.forgotPassword;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.framgia.englishconversation.BR;
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
        mPresenter.resetPassword(mEmail);
    }

    @Override
    public void onEmailEmpty() {
        mNavigator.showToast(R.string.empty_email);
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

    public void setEmail(String email) {
        mEmail = email;
        notifyPropertyChanged(BR.email);
    }
}
