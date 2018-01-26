package com.framgia.englishconversation.screen.main;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.screen.login.LoginActivity;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;

/**
 * Exposes the data to be used in the Main screen.
 */

public class MainViewModel extends BaseObservable implements MainContract.ViewModel {

    private MainContract.Presenter mPresenter;
    private Navigator mNavigator;
    private Context mContext;
    private MainActivity mActivity;
    private MainPagerAdapter mPagerAdapter;

    private Uri mUserPhotoUrl;
    private String mUsername;
    private String mUserEmail;

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
        mUserPhotoUrl = data.getPhotoUrl();
        mUserEmail = data.getEmail();
        mUsername = data.getDisplayName();
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

    public String getUserEmail() {
        return mUserEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public Uri getUserPhotoUrl() {
        return mUserPhotoUrl;
    }

    @Override
    public GoogleApiClient getGoogleApiClient() {
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

    public TabLayout.OnTabSelectedListener getTabSelectedListener() {
        final int selectedColor = ContextCompat.getColor(mContext, R.color.light_blue_900);
        final int unSelectedColor = ContextCompat.getColor(mContext, android.R.color.black);
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getIcon() != null) {
                    tab.getIcon().setColorFilter(selectedColor, PorterDuff.Mode.SRC_ATOP);
                }
                switch (tab.getPosition()) {
                    case MainActivity.NEW_POSITION:
                        mActivity.positionComponents(MainPagerAdapter.NEW);
                        break;
                    case MainActivity.YOUR_POST_POSITION:
                        mActivity.positionComponents(MainPagerAdapter.YOUR_POST);
                        break;
                    case MainActivity.SETTING_POSITION:
                        mActivity.positionComponents(MainPagerAdapter.SETTING);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getIcon() != null) {
                    tab.getIcon().setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

}
