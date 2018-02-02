package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.englishconversation.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bui Danh Nam on 26/1/2018.
 */

public class StatusModel extends BaseObservable implements Parcelable {

    @SerializedName("updated_user")
    private UserModel mUserUpdate;
    @SerializedName("created_at")
    private long mCreatedAt;
    @SerializedName("status")
    private int mStatus;

    public StatusModel() {
    }

    public StatusModel(UserModel userUpdate, long createdAt, int status) {
        mUserUpdate = userUpdate;
        mCreatedAt = createdAt;
        mStatus = status;
    }

    @Bindable
    public UserModel getUserUpdate() {
        return mUserUpdate;
    }

    public void setUserUpdate(UserModel userUpdate) {
        mUserUpdate = userUpdate;
        notifyPropertyChanged(BR.userUpdate);
    }

    @Bindable
    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
        notifyPropertyChanged(BR.createdUser);
    }

    @Bindable
    public int getStatus() {
        return mStatus;
    }

    public void setStatus(@Status int status) {
        mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    protected StatusModel(Parcel in) {
        mUserUpdate = (UserModel) in.readValue(UserModel.class.getClassLoader());
        mStatus = in.readInt();
        mCreatedAt = in.readLong();
    }

    @SuppressWarnings("unused")
    public static final Creator<StatusModel> CREATOR = new Creator<StatusModel>() {
        @Override
        public StatusModel createFromParcel(Parcel in) {
            return new StatusModel(in);
        }

        @Override
        public StatusModel[] newArray(int size) {
            return new StatusModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStatus);
        dest.writeLong(mCreatedAt);
        dest.writeLong(mCreatedAt);
        dest.writeValue(mUserUpdate);
    }
}
