package com.framgia.englishconversation.data.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.annotations.SerializedName;

/**
 * Created by framgia on 16/05/2017.
 */

public class Comment {
    @SerializedName("id")
    private String mId;
    @SerializedName("content")
    private String mContent;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("modified_at")
    private String mModifiedAt;
    @SerializedName("created_user")
    private UserModel mCreateUser;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getModifiedAt() {
        return mModifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        mModifiedAt = modifiedAt;
    }

    public UserModel getCreateUser() {
        return mCreateUser;
    }

    public void setCreateUser(UserModel createUser) {
        mCreateUser = createUser;
    }
}
