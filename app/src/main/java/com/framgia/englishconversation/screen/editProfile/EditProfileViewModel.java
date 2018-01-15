package com.framgia.englishconversation.screen.editProfile;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import com.google.firebase.auth.FirebaseUser;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.screen.main.MainActivity;
import com.framgia.englishconversation.service.FirebaseUploadService;
import com.framgia.englishconversation.utils.navigator.Navigator;

import static android.app.Activity.RESULT_OK;
import static com.framgia.englishconversation.service.BaseStorageService.AVATAR_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService.ACTION_UPLOAD;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_FOLDER;
import static com.framgia.englishconversation.service.FirebaseUploadService.EXTRA_URI;

/**
 * Exposes the data to be used in the EditProfile screen.
 */

public class EditProfileViewModel extends BaseObservable implements EditProfileContract.ViewModel {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditProfileContract.Presenter mPresenter;
    private String mUserName;
    private Uri mPhotoUri;
    private String mUserEmail;
    private Navigator mNavigator;
    private Context mContext;
    private ProgressDialog mDialog;
    private BroadcastReceiver mReceiver;

    public EditProfileViewModel(Context context, Navigator navigator) {
        mContext = context;
        mNavigator = navigator;
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(mContext.getString(R.string.msg_loading));
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {

                    case FirebaseUploadService.UPLOAD_FINNISH:
                        Uri uri =
                                intent.getParcelableExtra(FirebaseUploadService.EXTRA_DOWNLOAD_URL);
                        setPhotoUri(uri);
                        mPresenter.saveUser(mUserName, mPhotoUri);
                        break;
                    case FirebaseUploadService.UPLOAD_ERROR:
                        mNavigator.showToast(R.string.error_upload_file);
                        break;
                }
            }
        };

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
        manager.registerReceiver(mReceiver, FirebaseUploadService.getIntentFilter());
    }

    private void uploadFromUri() {
        mContext.startService(
                new Intent(mContext, FirebaseUploadService.class).putExtra(EXTRA_URI, mPhotoUri)
                        .putExtra(EXTRA_FOLDER, AVATAR_FOLDER)
                        .setAction(ACTION_UPLOAD));

        showProgressDialog(mContext.getString(R.string.progress_uploading));
    }

    private void showProgressDialog(String caption) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setIndeterminate(true);
        }

        mDialog.setMessage(caption);
        mDialog.show();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void setPresenter(EditProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSelectImageClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mNavigator.startActivityForResult(
                Intent.createChooser(intent, mContext.getString(R.string.title_upload)),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onSaveUserClick() {
        if (mPhotoUri != null) {
            uploadFromUri();
        } else {
            mPresenter.saveUser(mUserName, mPhotoUri);
        }
    }

    @Override
    public void onSaveUserSuccess() {
        mNavigator.startActivity(MainActivity.getInstance(mContext));
    }

    @Override
    public void onSaveUserFailed(String msg) {
        mNavigator.showToast(msg);
    }

    @Override
    public void onEmptyUserName() {
        mNavigator.showToast(mContext.getString(R.string.empty_user_name));
    }

    @Override
    public void hideProgressDialog() {
        if (mDialog != null) mDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        if (mDialog != null) mDialog.show();
    }

    @Override
    public void onGetUserSuccess(FirebaseUser data) {
        if (data != null) {
            setUserEmail(data.getEmail());
        }
    }

    @Bindable
    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            setPhotoUri(data.getData());
        }
    }

    @Bindable
    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        mPhotoUri = photoUri;
        notifyPropertyChanged(BR.photoUri);
    }

    @Bindable
    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
        notifyPropertyChanged(BR.userEmail);
    }
}
