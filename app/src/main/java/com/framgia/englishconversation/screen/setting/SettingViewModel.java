package com.framgia.englishconversation.screen.setting;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.login.LoginActivity;
import com.framgia.englishconversation.screen.main.MainActivity;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by doan.van.toan on 1/16/18.
 */

public class SettingViewModel extends BaseObservable implements SettingContract.ViewModel {
    private SettingContract.Presenter mPresenter;
    private UserModel mCurrentUser;
    private Navigator mNavigator;
    private MainActivity mActivity;

    public SettingViewModel(MainActivity activity) {
        mNavigator = new Navigator(activity);
        mActivity = activity;
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
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void onLogoutClick() {
        mPresenter.signOut();
    }

    @Bindable
    public UserModel getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(UserModel currentUser) {
        mCurrentUser = currentUser;
        notifyPropertyChanged(BR.currentUser);
    }

    @Override
    public void onSignOutSuccess() {
        mNavigator.startActivity(LoginActivity.getInstance(mActivity));
    }

    @Override
    public void onSignOutFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onGetCurrentUserSuccess(UserModel userModel) {
        setCurrentUser(userModel);
    }

    @Override
    public void onGetUserFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public GoogleApiClient getGoogleApiCliennt() {
        return mActivity.getGoogleApiClient();
    }
}
