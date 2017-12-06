package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.google.gson.annotations.SerializedName;
import com.framgia.englishconversation.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by framgia on 16/05/2017.
 */

public class TimelineModel extends BaseObservable {
    @SerializedName("id")
    private String mId;
    @SerializedName("content")
    private String mContent;
    @SerializedName("created_user")
    private UserModel mCreatedUser;
    @SerializedName("created_at")
    private long mCreatedAt;
    @SerializedName("modified_at")
    private long mModifiedAt;
    @SerializedName("location")
    private LocationModel mLocation;
    @SerializedName("medias")
    private List<MediaModel> mMedias = new ArrayList<>();
    @SerializedName("comments")
    private List<Comment> mComments;
    @SerializedName("likes")
    private List<UserModel> mLikeUser;
    @SerializedName("dish_like")
    private List<UserModel> mDishLikeUser;
    @SerializedName("report")
    private List<UserModel> mReportUser;

    public TimelineModel() {

    }

    @Bindable
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
        notifyPropertyChanged(BR.content);
    }

    @Bindable
    public UserModel getCreatedUser() {
        return mCreatedUser;
    }

    public void setCreatedUser(UserModel createdUser) {
        mCreatedUser = createdUser;
        notifyPropertyChanged(BR.createdUser);
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
    public long getModifiedAt() {
        return mModifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        mModifiedAt = modifiedAt;
        notifyPropertyChanged(BR.createdUser);
    }

    @Bindable
    public LocationModel getLocation() {
        return mLocation;
    }

    public void setLocation(LocationModel location) {
        mLocation = location;
        notifyPropertyChanged(BR.location);
    }

    @Bindable
    public List<MediaModel> getMedias() {
        return mMedias;
    }

    public void setMedias(List<MediaModel> medias) {
        mMedias = medias;
        notifyPropertyChanged(BR.medias);
    }

    @Bindable
    public List<Comment> getComments() {
        return mComments;
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
        notifyPropertyChanged(BR.comments);
    }

    @Bindable
    public List<UserModel> getLikeUser() {
        return mLikeUser;
    }

    public void setLikeUser(List<UserModel> likeUser) {
        mLikeUser = likeUser;
        notifyPropertyChanged(BR.likeUser);
    }

    @Bindable
    public List<UserModel> getDishLikeUser() {
        return mDishLikeUser;
    }

    public void setDishLikeUser(List<UserModel> dishLikeUser) {
        mDishLikeUser = dishLikeUser;
        notifyPropertyChanged(BR.dishLikeUser);
    }

    @Bindable
    public List<UserModel> getReportUser() {
        return mReportUser;
    }

    public void setReportUser(List<UserModel> reportUser) {
        mReportUser = reportUser;
        notifyPropertyChanged(BR.reportUser);
    }

    @Bindable
    public int getViewType() {
        return getMedias() != null ? getMedias().size() : 0;
    }

    @Override
    public String toString() {
        return "TimelineModel{"
            + "mId='"
            + mId
            + '\''
            + ", mContent='"
            + mContent
            + '\''
            + ", mCreatedUser="
            + mCreatedUser
            + '}';
    }
}
