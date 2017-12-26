package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by framgia on 16/05/2017.
 */

public class Comment extends BaseObservable {
    @SerializedName("id")
    private String mId;
    @SerializedName("post_id")
    private String mPostId;
    @SerializedName("content")
    private String mContent;
    @SerializedName("created_at")
    private long mCreatedAt;
    @SerializedName("modified_at")
    private long mModifiedAt;
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

    @Bindable
    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    @Bindable
    public long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }

    @Bindable
    public long getModifiedAt() {
        return mModifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        mModifiedAt = modifiedAt;
        notifyPropertyChanged(BR.modifiedAt);
    }

    @Bindable
    public UserModel getCreateUser() {
        return mCreateUser;
    }

    public void setCreateUser(UserModel createUser) {
        mCreateUser = createUser;
        notifyPropertyChanged(BR.createUser);
    }

    @Bindable
    public MediaModel getMediaModel() {
        return mMediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        mMediaModel = mediaModel;
        notifyPropertyChanged(BR.mediaModel);
    }

    @Bindable
    public int getCommentType() {
        if (mMediaModel != null) {
            return mMediaModel.getType();
        }
        return MediaModel.MediaType.ONLY_TEXT;
    }
}
