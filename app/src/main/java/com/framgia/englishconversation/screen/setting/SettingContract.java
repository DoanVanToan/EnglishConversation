package com.framgia.englishconversation.screen.setting;

/**
 * Created by doan.van.toan on 1/16/18.
 */

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.UserModel;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Define contract of SettingViewModel and SettingPresneter
 */
public interface SettingContract {
    /**
     * Setting ViewModel
     */
    interface ViewModel extends BaseViewModel<Presenter> {

        void onSignOutSuccess();

        void onSignOutFailed(String msg);

        void onGetCurrentUserSuccess(UserModel userModel);

        void onGetUserFailed(String msg);

        GoogleApiClient getGoogleApiCliennt();
    }

    /**
     * Setting Presenter
     */
    interface Presenter extends BasePresenter {

        void signOut();
    }
}
