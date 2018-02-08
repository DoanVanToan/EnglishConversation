package com.framgia.englishconversation.screen.profile;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.timeline.usertimeline.UserTimelineFragment;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the Profile screen.
 */

public class ProfileViewModel extends BaseObservable implements ProfileContract.ViewModel {

    private ProfileContract.Presenter mPresenter;
    private boolean mIsEditing = true;
    private boolean mIsAllowChangePassword = true;
    private Navigator mNavigator;
    private UserModel mUserModel;
    private Fragment mFragment;
    private FragmentManager mManager;

    ProfileViewModel(Navigator navigator, FragmentManager manager, UserModel userModel) {
        mNavigator = navigator;
        mManager = manager;
        mUserModel = userModel;
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
    public void setPresenter(ProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetUserSuccesss(UserModel data) {
        if (mUserModel == null) {
            setUserModel(data);
        }
        mFragment = UserTimelineFragment.newInstance(mUserModel);
    }

    @Override
    public void onChangeAvtClick() {
        // TODO: 15/05/2017  onChangeAvtClick
    }

    @Override
    public void onEditUserClick() {
        // TODO: 15/05/2017  onEditUserClick
    }

    @Override
    public void showChangePasswordDialog() {
        // TODO: 15/05/2017  showChangePasswordDialog
    }

    @Bindable
    public UserModel getUserModel() {
        return mUserModel;
    }

    public void setUserModel(UserModel userModel) {
        mUserModel = userModel;
        notifyPropertyChanged(BR.userModel);
    }

    @Bindable
    public boolean isEditing() {
        return mIsEditing;
    }

    public void setEditing(boolean editing) {
        mIsEditing = editing;
        notifyPropertyChanged(BR.editing);
    }

    @Bindable
    public boolean isAllowChangePassword() {
        return mIsAllowChangePassword;
    }

    public void setAllowChangePassword(boolean allowChangePassword) {
        mIsAllowChangePassword = allowChangePassword;
        notifyPropertyChanged(BR.allowChangePassword);
    }

    @Bindable
    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
        notifyPropertyChanged(BR.fragment);
    }

    @Bindable
    public FragmentManager getManager() {
        return mManager;
    }

    public void setManager(FragmentManager manager) {
        mManager = manager;
        notifyPropertyChanged(BR.manager);
    }
}
