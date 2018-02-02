package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by framgia on 16/05/2017.
 */

public class Comment extends BaseObservable implements Parcelable, Cloneable {
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
    @SerializedName("status")
    private StatusModel mStatusModel;

    public Comment() {
    }

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

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
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

    @Bindable
    public StatusModel getStatusModel() {
        return mStatusModel;
    }

    public void setStatusModel(StatusModel status) {
        mStatusModel = status;
        notifyPropertyChanged(BR.statusModel);
    }

    protected Comment(Parcel in) {
        mId = in.readString();
        mPostId = in.readString();
        mContent = in.readString();
        mCreatedAt = in.readLong();
        mModifiedAt = in.readLong();
        mCreateUser = (UserModel) in.readValue(UserModel.class.getClassLoader());
        mMediaModel = (MediaModel) in.readValue(MediaModel.class.getClassLoader());
        mStatusModel = (StatusModel) in.readValue(
                StatusModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mPostId);
        dest.writeString(mContent);
        dest.writeLong(mCreatedAt);
        dest.writeLong(mModifiedAt);
        dest.writeValue(mCreateUser);
        dest.writeValue(mMediaModel);
        dest.writeValue(mStatusModel);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Comment comment = (Comment) o;

        return mId != null ? mId.equals(comment.mId) : comment.mId == null;
    }

    @Override
    public int hashCode() {
        return mId != null ? mId.hashCode() : 0;
    }

    @Override
    public Comment clone() throws CloneNotSupportedException {
        return (Comment) super.clone();
    }
}
