package com.framgia.englishconversation.screen.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.screen.login.LoginActivity;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Exposes the data to be used in the Main screen.
 */

public class MainViewModel extends BaseObservable implements MainContract.ViewModel {

    private MainContract.Presenter mPresenter;
    private Navigator mNavigator;
    private Context mContext;
    private MainActivity mActivity;
    private MainPagerAdapter mPagerAdapter;

    public MainViewModel(Context context, Navigator navigator) {
        mContext = context;
        mActivity = (MainActivity) context;
        mNavigator = navigator;
        mPagerAdapter = new MainPagerAdapter(mActivity.getSupportFragmentManager());
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
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGetCurrentUserSuccess(FirebaseUser data) {

    }

    @Override
    public void onSignOutClick() {
        mPresenter.signOut();
    }

    @Override
    public void onSignOutSuccess() {
        mNavigator.startActivity(LoginActivity.getInstance(mContext));
    }

    @Override
    public void onSignOutFailed(String msg) {

    }

    @Override
    public GoogleApiClient getGoogleApiCliennt() {
        return mActivity.getGoogleApiClient();
    }

    @Bindable
    public MainPagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    public void setPagerAdapter(MainPagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        notifyPropertyChanged(BR.pagerAdapter);
    }
}
