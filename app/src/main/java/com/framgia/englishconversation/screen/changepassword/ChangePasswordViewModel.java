package com.framgia.englishconversation.screen.changepassword;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;

/**
 * Created by Sony on 2/7/2018.
 */

public class ChangePasswordViewModel extends BaseObservable{
    private String mCurrentPassword;
    private String mNewPassword;
    private String mRetypePassword;

    @Bindable
    public String getCurrentPassword() {
        return mCurrentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        mCurrentPassword = currentPassword;
        notifyPropertyChanged(BR.currentPassword);
    }

    @Bindable
    public String getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(String newPassword) {
        mNewPassword = newPassword;
        notifyPropertyChanged(BR.newPassword);
    }

    @Bindable
    public String getRetypePassword() {
        return mRetypePassword;
    }

    public void setRetypePassword(String retypePassword) {
        mRetypePassword = retypePassword;
        notifyPropertyChanged(BR.retypePassword);
    }
}
