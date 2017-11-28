package com.framgia.englishconversation.screen.login;

import android.text.TextUtils;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsApi;

import static com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsKey.PREF_EMAIL;
import static com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsKey.PREF_PASSWORD;

/**
 * Listens to user actions from the UI ({@link LoginActivity}), retrieves the data and updates
 * the UI as required.
 */
final class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;
    private DataCallback<FirebaseUser> mSignInCallback;
    private SharedPrefsApi mSharedPrefs;

    public LoginPresenter(LoginContract.ViewModel viewModel, AuthenicationRepository repository,
                          SharedPrefsApi sharedPrefs) {
        mViewModel = viewModel;
        mRepository = repository;
        mSharedPrefs = sharedPrefs;
        initSignInCallback();
    }

    @Override
    public void onStart() {
        mRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccessful(data);
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onGetCurrentUserError(msg);
                mViewModel.onGetLastEmail(mSharedPrefs.get(PREF_EMAIL, String.class));
                mViewModel.onGetLastPassword(mSharedPrefs.get(PREF_PASSWORD, String.class));
            }
        });
    }

    private void initSignInCallback() {
        mSignInCallback = new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccessful(data);
                mViewModel.dismissDialog();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.dismissDialog();
                mViewModel.onLoginError(msg);
            }
        };
    }

    @Override
    public void onStop() {
    }

    @Override
    public void login(final String email, final String password, final boolean isRememberAccount) {
        if (TextUtils.isEmpty(email)) {
            mViewModel.onEmailEmpty();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mViewModel.onPasswordEmpty();
            return;
        }
        mViewModel.showDialog();
        mRepository.signIn(email, password, new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mViewModel.onGetUserSuccessful(data);
                mViewModel.dismissDialog();
                if (isRememberAccount) {
                    mSharedPrefs.put(PREF_EMAIL, email);
                    mSharedPrefs.put(PREF_PASSWORD, password);
                }else {
                    mSharedPrefs.put(PREF_EMAIL, "");
                    mSharedPrefs.put(PREF_PASSWORD, "");
                }
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.dismissDialog();
                mViewModel.onLoginError(msg);
            }
        });
    }

    @Override
    public void login(GoogleSignInAccount account) {
        mViewModel.showDialog();
        mRepository.signIn(account, mSignInCallback);
    }

    @Override
    public void login(AccessToken accessToken) {
        mViewModel.showDialog();
        mRepository.signIn(accessToken, mSignInCallback);
    }
}
