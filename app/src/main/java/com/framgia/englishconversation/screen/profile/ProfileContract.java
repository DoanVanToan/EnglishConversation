package com.framgia.englishconversation.screen.profile;

;import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.UserModel;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ProfileContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onGetUserSuccesss(UserModel data);

        void onChangeAvtClick();

        void onEditUserClick();

        void showChangePasswordDialog();

        void onSignOutClick();

        GoogleApiClient getGoogleApiCliennt();

        void onSignOutSuccess();

        void onSignOutFailed(String msg);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void signOut();
    }
}
