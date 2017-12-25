package com.framgia.englishconversation.screen.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import com.facebook.AccessToken;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.screen.editProfile.EditProfileActivity;
import com.framgia.englishconversation.screen.forgotPassword.ForgotPasswordActivity;
import com.framgia.englishconversation.screen.main.MainActivity;
import com.framgia.englishconversation.screen.register.RegisterActivity;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseUser;

/**
 * Exposes the data to be used in the Login screen.
 */
public class LoginViewModel extends BaseObservable implements LoginContract.ViewModel {
    private static final int GOOGLE_SIGNIN_REQUEST = 1;
    private LoginContract.Presenter mPresenter;
    private Context mContext;
    private Navigator mNavigator;
    private ProgressDialog mDialog;
    private String mEmail;
    private String mPassword;
    private String mEmailError;
    private String mPasswordError;
    private boolean mIsRememberAccount;
    private LoginActivity mActivity;

    LoginViewModel(Context context, Navigator navigator) {
        mContext = context;
        mActivity = (LoginActivity) context;
        mNavigator = navigator;
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(context.getString(R.string.msg_loading));
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
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onRegisterClick() {
        mNavigator.startActivity(RegisterActivity.getInstance(mContext));
    }

    @Override
    public void showDialog() {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onGetCurrentUserError(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onGetUserSuccessful(FirebaseUser firebaseUser) {
        mNavigator.finishActivity();
        if (!TextUtils.isEmpty(firebaseUser.getDisplayName())) {
            mNavigator.startActivity(MainActivity.getInstance(mContext));
        } else {
            mNavigator.startActivity(EditProfileActivity.getInstance(mContext));
        }
    }

    @Override
    public void onLoginError(String message) {
        mNavigator.showToast(message);
    }

    @Override
    public void onLoginClick() {
        if (!mPresenter.validateInput(mEmail, mPassword)) {
            return;
        }
        mPresenter.login(mEmail, mPassword, mIsRememberAccount);
    }

    @Override
    public void onLoginGoogleClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mActivity.getGoogleApiClient());
        mActivity.startActivityForResult(signInIntent, GOOGLE_SIGNIN_REQUEST);
    }

    @Override
    public void onForgotPasswordClick() {
        mNavigator.startActivity(ForgotPasswordActivity.getInstance(mContext));
    }

    @Override
    public void onInputEmailError(int message) {
        setEmailError(mContext.getString(message));
    }

    @Override
    public void onInputPasswordError(int message) {
        setPasswordError(mContext.getString(message));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_SIGNIN_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                mPresenter.login(account);
            } else {
                mNavigator.showToast(result.getStatus().getStatusMessage());
            }
        }
    }

    @Override
    public void onLoginFacebookSuccess(AccessToken accessToken) {
        mPresenter.login(accessToken);
    }

    @Override
    public void onGetLastEmail(String s) {
        if (TextUtils.isEmpty(s)) return;
        setEmail(s);
        setRememberAccount(true);
    }

    @Override
    public void onGetLastPassword(String s) {
        if (TextUtils.isEmpty(s)) return;
        setPassword(s);
        setRememberAccount(true);
    }

    @Bindable
    public String getEmail() {
        return mEmail;
    }

    @Bindable
    public String getPassword() {
        return mPassword;
    }

    @Bindable
    public boolean isRememberAccount() {
        return mIsRememberAccount;
    }

    @Bindable
    public String getEmailError() {
        return mEmailError;
    }

    @Bindable
    public String getPasswordError() {
        return mPasswordError;
    }

    public void setEmail(String email) {
        mEmail = email;
        notifyPropertyChanged(BR.email);
    }

    public void setPassword(String password) {
        mPassword = password;
        notifyPropertyChanged(BR.password);
    }

    public void setRememberAccount(boolean rememberAccount) {
        mIsRememberAccount = rememberAccount;
        notifyPropertyChanged(BR.rememberAccount);
    }

    public void setEmailError(String emailError) {
        mEmailError = emailError;
        notifyPropertyChanged(BR.emailError);
    }

    public void setPasswordError(String passwordError) {
        mPasswordError = passwordError;
        notifyPropertyChanged(BR.passwordError);
    }
}
