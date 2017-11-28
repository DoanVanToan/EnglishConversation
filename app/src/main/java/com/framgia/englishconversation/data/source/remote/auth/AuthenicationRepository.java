package com.framgia.englishconversation.data.source.remote.auth;

import android.net.Uri;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 * Created by framgia on 10/05/2017.
 */

public class AuthenicationRepository {
    private AuthenicationDataSource.RemoteDataSource mRemoteDataSource;

    public AuthenicationRepository(AuthenicationDataSource.RemoteDataSource remoteDataSource) {
        mRemoteDataSource = remoteDataSource;
    }

    public void register(String email, String password, DataCallback<FirebaseUser> callback) {
        mRemoteDataSource.register(email, password, callback);
    }

    public void signIn(String email, String password, DataCallback<FirebaseUser> callback) {
        mRemoteDataSource.signIn(email, password, callback);
    }

    public void signIn(GoogleSignInAccount account, DataCallback<FirebaseUser> callback) {
        mRemoteDataSource.signIn(account, callback);
    }

    public void signIn(AccessToken accessToken, DataCallback<FirebaseUser> callback) {
        mRemoteDataSource.signIn(accessToken, callback);
    }

    public void getCurrentUser(DataCallback<FirebaseUser> callback) {
        mRemoteDataSource.getCurrentUser(callback);
    }

    public void signOut(GoogleApiClient googleApiClient, DataCallback callback) {
        mRemoteDataSource.signOut(googleApiClient, callback);
    }

    public void signOut(LoginManager instance, DataCallback callback) {
        mRemoteDataSource.signOut(instance, callback);
    }

    public void signOut(DataCallback callback){
        mRemoteDataSource.signOut(callback);
    }

    public void resetPassword(String email, DataCallback callback) {
        mRemoteDataSource.resetPassword(email, callback);
    }

    public void updateProfile(String userName, Uri photo, DataCallback callback) {
        mRemoteDataSource.updateProfile(userName, photo, callback);
    }
}
