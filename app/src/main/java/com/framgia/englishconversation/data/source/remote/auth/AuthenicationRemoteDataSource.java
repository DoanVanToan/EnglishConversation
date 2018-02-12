package com.framgia.englishconversation.data.source.remote.auth;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.framgia.englishconversation.data.source.callback.DataCallback;

/**
 * Created by framgia on 10/05/2017.
 */

public class AuthenicationRemoteDataSource extends BaseAuthRemoteDataSource
        implements AuthenicationDataSource.RemoteDataSource {
    public AuthenicationRemoteDataSource() {
    }

    /**
     * Register with normal account
     *
     * @param email
     * @param password
     * @param callback
     */
    @Override
    public void register(String email, String password, final DataCallback<FirebaseUser> callback) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        getResponse(task, callback);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetDataFailed(e.getMessage());
                    }
                });
    }

    /**
     * Login with normal account
     *
     * @param email
     * @param password
     * @param callback
     */
    @Override
    public void signIn(String email, String password, final DataCallback<FirebaseUser> callback) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        getResponse(task, callback);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetDataFailed(e.getMessage());
                    }
                });
    }

    /**
     * Login with google
     *
     * @param account
     * @param callback
     */
    @Override
    public void signIn(GoogleSignInAccount account, final DataCallback<FirebaseUser> callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            callback.onGetDataSuccess(user);
                        } else {
                            callback.onGetDataFailed(task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * Login with facebook using acccess token
     *
     * @param token
     * @param callback
     */
    @Override
    public void signIn(AccessToken token, final DataCallback<FirebaseUser> callback) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onGetDataSuccess(task.getResult().getUser());
                        } else {
                            callback.onGetDataFailed(task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetDataFailed(e.getMessage());
                    }
                });
    }

    /**
     * Get current firebase user
     *
     * @param callback
     */
    @Override
    public void getCurrentUser(DataCallback<FirebaseUser> callback) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user == null) {
            callback.onGetDataFailed(null);
        } else {
            callback.onGetDataSuccess(user);
        }
    }

    /**
     * Signout
     *
     * @param callback
     */
    @Override
    public void signOut(GoogleApiClient googleApiClient, DataCallback<FirebaseUser> callback) {
        if (googleApiClient != null) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
        signOut(callback);
    }

    /**
     * Signout with facebook
     *
     * @param loginManager
     * @param callback
     */
    @Override
    public void signOut(LoginManager loginManager, DataCallback<FirebaseUser> callback) {
        if (loginManager != null) {
            loginManager.logOut();
        }
        signOut(callback);
    }

    @Override
    public void signOut(DataCallback<FirebaseUser> callback) {
        mFirebaseAuth.signOut();
        callback.onGetDataSuccess(null);
    }

    /**
     * reset password by send email
     *
     * @param email
     * @param callback
     */
    @Override
    public void resetPassword(String email, final DataCallback callback) {
        mFirebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onGetDataSuccess(null);
                        } else {
                            callback.onGetDataFailed(task.getException().getMessage());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetDataFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void updateProfile(String userName, Uri photo, final DataCallback callback) {
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
        builder.setDisplayName(userName);

        if (photo != null) {
            builder.setPhotoUri(photo);
        }

        mFirebaseAuth.getCurrentUser()
                .updateProfile(builder.build())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onGetDataSuccess(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onGetDataFailed(e.getMessage());
                    }
                });
    }


    @Override
    public AuthCredential credentialUser(String email, String password) {
        return EmailAuthProvider.getCredential(email, password);
    }

    @Override
    public void changePassword(final FirebaseUser firebaseUser, String currentPassword,
                               final String newPassword, final DataCallback dataCallback,
                               final ChangePasswordCallBack changePasswordCallBack) {
        firebaseUser.reauthenticate(credentialUser(firebaseUser.getEmail(), currentPassword)).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            changePassword(firebaseUser, newPassword, changePasswordCallBack);
                        } else {
                            dataCallback.onGetDataFailed(null);
                        }
                    }
                });

    }

    @Override
    public void changePassword(FirebaseUser firebaseUser, String newPassword,
                               final ChangePasswordCallBack dataCallback) {
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dataCallback.onChangePasswordSuccess();
                        } else {
                            dataCallback.onChangePasswordFailed();
                        }
                    }
                });
    }

    private void getResponse(Task<AuthResult> authResultTask, DataCallback callback) {
        if (authResultTask == null) {
            callback.onGetDataFailed(null);
            return;
        }
        if (!authResultTask.isSuccessful()) {
            String message = authResultTask.getException().getMessage();
            callback.onGetDataFailed(message);
            return;
        }
        callback.onGetDataSuccess(authResultTask.getResult().getUser());
    }
}
