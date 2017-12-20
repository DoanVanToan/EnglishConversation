package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.englishconversation.BR;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by framgia on 16/05/2017.
 */

public class TimelineModel extends BaseObservable implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TimelineModel> CREATOR = new Parcelable.Creator<TimelineModel>() {
        @Override
        public TimelineModel createFromParcel(Parcel in) {
            return new TimelineModel(in);
        }

        @Override
        public TimelineModel[] newArray(int size) {
            return new TimelineModel[size];
        }
    };
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
    @SerializedName("conversations")
    private List<ConversationModel> mConversations;

    public TimelineModel() {
    }

    protected TimelineModel(Parcel in) {
        mId = in.readString();
        mContent = in.readString();
        mCreatedUser = (UserModel) in.readValue(UserModel.class.getClassLoader());
        mCreatedAt = in.readLong();
        mModifiedAt = in.readLong();
        mLocation = (LocationModel) in.readValue(LocationModel.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mMedias = new ArrayList<MediaModel>();
            in.readList(mMedias, MediaModel.class.getClassLoader());
        } else {
            mMedias = null;
        }
        if (in.readByte() == 0x01) {
            mComments = new ArrayList<Comment>();
            in.readList(mComments, Comment.class.getClassLoader());
        } else {
            mComments = null;
        }
        if (in.readByte() == 0x01) {
            mLikeUser = new ArrayList<UserModel>();
            in.readList(mLikeUser, UserModel.class.getClassLoader());
        } else {
            mLikeUser = null;
        }
        if (in.readByte() == 0x01) {
            mDishLikeUser = new ArrayList<UserModel>();
            in.readList(mDishLikeUser, UserModel.class.getClassLoader());
        } else {
            mDishLikeUser = null;
        }
        if (in.readByte() == 0x01) {
            mReportUser = new ArrayList<UserModel>();
            in.readList(mReportUser, UserModel.class.getClassLoader());
        } else {
            mReportUser = null;
        }
        if (in.readByte() == 0x01) {
            mConversations = new ArrayList<ConversationModel>();
            in.readList(mConversations, ConversationModel.class.getClassLoader());
        } else {
            mConversations = null;
        }
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
    public List<ConversationModel> getConversations() {
        return mConversations;
    }

    public void setConversations(List<ConversationModel> conversations) {
        mConversations = conversations;
        notifyPropertyChanged(BR.conversations);
    }

    @Bindable
    public int getPostType() {
        if (mConversations != null && mConversations.size() > 0) {
            return MediaModel.MediaType.CONVERSATION;
        }
        if (mMedias != null && mMedias.size() > 0) {
            return mMedias.get(0).getType();
        }
        return MediaModel.MediaType.ONLY_TEXT;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mContent);
        dest.writeValue(mCreatedUser);
        dest.writeLong(mCreatedAt);
        dest.writeLong(mModifiedAt);
        dest.writeValue(mLocation);
        if (mMedias == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mMedias);
        }
        if (mComments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mComments);
        }
        if (mLikeUser == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mLikeUser);
        }
        if (mDishLikeUser == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mDishLikeUser);
        }
        if (mReportUser == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mReportUser);
        }
        if (mConversations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mConversations);
        }
    }
}
