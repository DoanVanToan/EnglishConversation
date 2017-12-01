package com.framgia.englishconversation.screen.main;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.callback.DataCallback;

import java.util.List;

/**
 * Listens to user actions from the UI ({@link MainActivity}), retrieves the data and updates
 * the UI as required.
 */
final class MainPresenter implements MainContract.Presenter {
    private static final String TAG = MainPresenter.class.getName();

    private final MainContract.ViewModel mViewModel;
    private AuthenicationRepository mRepository;
    private FirebaseUser mUser;
    private DataCallback mSignOutCallback;

    public MainPresenter(MainContract.ViewModel viewModel, AuthenicationRepository repository) {
        mViewModel = viewModel;
        mRepository = repository;
    }

    @Override
    public void onStart() {
        mRepository.getCurrentUser(new DataCallback<FirebaseUser>() {
            @Override
            public void onGetDataSuccess(FirebaseUser data) {
                mUser = data;
                mViewModel.onGetCurrentUserSuccess(data);
            }

            @Override
            public void onGetDataFailed(String msg) {

            }
        });


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
    public void onStop() {
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
                        mRepository.signOut(mViewModel.getGoogleApiClient(), mSignOutCallback);
                        break;
                    case FacebookAuthProvider.PROVIDER_ID:
                        mRepository.signOut(LoginManager.getInstance(), mSignOutCallback);
                        break;
                    default:
                        mRepository.signOut(mSignOutCallback);
                        break;
                }
            }
        }
    }
}
