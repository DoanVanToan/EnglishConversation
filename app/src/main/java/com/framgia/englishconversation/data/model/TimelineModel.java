package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.framgia.englishconversation.AppApplication;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.framgia.englishconversation.data.model.TimelineModel.Flag.EDITOR_CHOISE;
import static com.framgia.englishconversation.data.model.TimelineModel.Flag.NOMARL;

/**
 * Created by framgia on 16/05/2017.
 */

public class TimelineModel extends BaseObservable implements Parcelable, Cloneable {

    public static final Creator<TimelineModel> CREATOR = new Creator<TimelineModel>() {
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
    @SerializedName("flag")
    @Flag
    private int mFlag;

    public TimelineModel() {
    }

    protected TimelineModel(Parcel in) {
        mId = in.readString();
        mContent = in.readString();
        mCreatedUser = in.readParcelable(UserModel.class.getClassLoader());
        mCreatedAt = in.readLong();
        mModifiedAt = in.readLong();
        mMedias = in.createTypedArrayList(MediaModel.CREATOR);
        mComments = in.createTypedArrayList(Comment.CREATOR);
        mLikeUser = in.createTypedArrayList(UserModel.CREATOR);
        mDishLikeUser = in.createTypedArrayList(UserModel.CREATOR);
        mReportUser = in.createTypedArrayList(UserModel.CREATOR);
        mConversations = in.createTypedArrayList(ConversationModel.CREATOR);
        mFlag = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mContent);
        dest.writeParcelable(mCreatedUser, flags);
        dest.writeLong(mCreatedAt);
        dest.writeLong(mModifiedAt);
        dest.writeTypedList(mMedias);
        dest.writeTypedList(mComments);
        dest.writeTypedList(mLikeUser);
        dest.writeTypedList(mDishLikeUser);
        dest.writeTypedList(mReportUser);
        dest.writeTypedList(mConversations);
        dest.writeInt(mFlag);
    }

    @Override
    public int describeContents() {
        return 0;
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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof TimelineModel) {
            TimelineModel other = (TimelineModel) obj;
            return other.getId().equals(getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Bindable
    public int getFlag() {
        return mFlag;
    }

    public void setFlag(int flag) {
        mFlag = flag;
        notifyPropertyChanged(BR.flag);
    }

    @Bindable
    public String getFlagName() {
        switch (mFlag) {
            case EDITOR_CHOISE:
                return AppApplication.getInstance().getString(R.string.flag_editor_choise);
            default:
                return null;
        }
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

    /**
     * Post Flag
     * 0 - Normal
     * 1 - Editor choise
     * ....
     */
    @IntDef({NOMARL, EDITOR_CHOISE})
    public @interface Flag {
        int NOMARL = 0;
        int EDITOR_CHOISE = 1;
    }
}
