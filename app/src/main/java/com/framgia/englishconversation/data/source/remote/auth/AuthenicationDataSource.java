package com.framgia.englishconversation.data.source.remote.auth;

import android.net.Uri;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 *
 */

public interface AuthenicationDataSource {
    interface RemoteDataSource {
        void register(String email, String password, DataCallback<FirebaseUser> callBack);

        void getCurrentUser(DataCallback<FirebaseUser> callBack);

        void signIn(String email, String password, DataCallback<FirebaseUser> callBack);

        void signIn(GoogleSignInAccount account, DataCallback<FirebaseUser> callback);

        void signIn(AccessToken token, DataCallback<FirebaseUser> callback);

        void signOut(GoogleApiClient googleApiClient, DataCallback<FirebaseUser> callback);

        void signOut(LoginManager loginManager, DataCallback<FirebaseUser> callback);

        void signOut(DataCallback<FirebaseUser> callback);

        void resetPassword(String email, DataCallback callback);

        void updateProfile(String userName, Uri photo, DataCallback callback);

    }
}
