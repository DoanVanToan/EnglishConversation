package com.framgia.englishconversation.screen.profileuser;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.screen.profile.ProfileFragment;

/**
 * Exposes the data to be used in the Profileuser screen.
 */

public class ProfileUserViewModel extends BaseObservable implements ProfileUserContract.ViewModel {

    private ProfileUserContract.Presenter mPresenter;
    private Fragment mFragment;
    private FragmentManager mManager;

    public ProfileUserViewModel(UserModel userModel, FragmentManager manager) {
        mManager = manager;
        mFragment = ProfileFragment.newInstance(userModel);
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
    public void setPresenter(ProfileUserContract.Presenter presenter) {
        mPresenter = presenter;
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
