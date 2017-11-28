package com.framgia.englishconversation.screen.profile;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.callback.DataCallback;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link ProfileFragment}), retrieves the data and updates
 * the UI as required.
 */
final class ProfilePresenter implements ProfileContract.Presenter {
    private static final String TAG = ProfilePresenter.class.getName();

    private final ProfileContract.ViewModel mViewModel;
    private AuthenicationRepository mAuthenicationRepository;
    private FirebaseUser mUser;
    private DataCallback mSignOutCallback;

    public ProfilePresenter(ProfileContract.ViewModel viewModel,
            AuthenicationRepository authenicationRepository) {
        mViewModel = viewModel;
        mAuthenicationRepository = authenicationRepository;
    }

    @Override
    public void onStart() {
        mAuthenicationRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mUser = data;
                mViewModel.onGetUserSuccesss(new UserModel(data));
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });
    }

    @Override
    public void onStop() {
    }

    private void initSignOutCallback() {
        mSignOutCallback = new DataCallback() {
            @Override
            public void onGetDataSuccess(Object data) {
                mViewModel.onSignOutSuccess();
            }

            @Override
            public void onGetDataFailed(String msg) {
                mViewModel.onSignOutFailed(msg);
            }
        };
    }

    @Override
    public void signOut() {
        if (mUser == null) return;
        if (mSignOutCallback == null) {
            initSignOutCallback();
        }
        List<String> provinders = mUser.getProviders();
        if (provinders != null && provinders.size() != 0) {
            for (String provinder : provinders) {
                switch (provinder) {
                    case GoogleAuthProvider.PROVIDER_ID:
                        mAuthenicationRepository.signOut(mViewModel.getGoogleApiCliennt(), mSignOutCallback);
                        break;
                    case FacebookAuthProvider.PROVIDER_ID:
                        mAuthenicationRepository.signOut(LoginManager.getInstance(), mSignOutCallback);
                        break;
                    default:
                        mAuthenicationRepository.signOut(mSignOutCallback);
                        break;
                }
            }
        }
    }
}
