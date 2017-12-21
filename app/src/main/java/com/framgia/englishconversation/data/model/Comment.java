package com.framgia.englishconversation.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by framgia on 16/05/2017.
 */

public class Comment {
    @SerializedName("id")
    private String mId;
    @SerializedName("post_id")
    private String mPostId;
    @SerializedName("content")
    private String mContent;
    @SerializedName("created_at")
    private long mCreatedAt;
    @SerializedName("modified_at")
    private String mModifiedAt;
    @SerializedName("created_user")
    private UserModel mCreateUser;
    @SerializedName("media")
    private MediaModel mMediaModel;

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

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
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

    public MediaModel getMediaModel() {
        return mMediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        mMediaModel = mediaModel;
    }

    public void setCreateUser(UserModel createUser) {
        mCreateUser = createUser;
    }
}
