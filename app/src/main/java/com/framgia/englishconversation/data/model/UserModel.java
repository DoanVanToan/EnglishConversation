package com.framgia.englishconversation.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class UserModel implements Parcelable {
    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
    @SerializedName("id")
    @Expose
    private String mId;
    @SerializedName("user_name")
    @Expose
    private String mUserName;
    @SerializedName("email")
    @Expose
    private String mEmail;
    @SerializedName("authentication_token")
    @Expose
    private String mToken;
    @SerializedName("photo_url")
    @Expose
    private String mPhotoUrl;
    @SerializedName("created_at")
    @Expose
    private String mCreatedAt;
    @SerializedName("updated_at")
    @Expose
    private String mUpdatedAt;

    public UserModel(FirebaseUser user) {
        mUserName = user.getDisplayName();
        mPhotoUrl = user.getPhotoUrl().toString();
        mEmail = user.getEmail();
        mId = user.getUid();
    }

    protected UserModel(Parcel in) {
        mUserName = in.readString();
        mEmail = in.readString();
        mToken = in.readString();
        mCreatedAt = in.readString();
        mUpdatedAt = in.readString();
        mPhotoUrl = in.readString();
    }

    public UserModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserName);
        dest.writeString(mEmail);
        dest.writeString(mToken);
        dest.writeString(mCreatedAt);
        dest.writeString(mUpdatedAt);
        dest.writeString(mPhotoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String name) {
        mUserName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
}
