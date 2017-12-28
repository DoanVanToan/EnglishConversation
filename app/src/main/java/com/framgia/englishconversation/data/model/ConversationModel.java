package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.englishconversation.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fs-sournary.
 * Date on 12/7/17.
 * Description:
 */

public class ConversationModel extends BaseObservable implements Parcelable {

    public static final Creator<ConversationModel> CREATOR = new Creator<ConversationModel>() {
        @Override
        public ConversationModel createFromParcel(Parcel in) {
            return new ConversationModel(in);
        }

        @Override
        public ConversationModel[] newArray(int size) {
            return new ConversationModel[size];
        }
    };
    @SerializedName("content")
    private String mContent;
    @SerializedName("media")
    private MediaModel mMediaModel;
    @SerializedName("type")
    @GravityType
    private int mGravity;

    public ConversationModel() {
    }

    public ConversationModel(@GravityType int gravity) {
        mGravity = gravity;
    }

    protected ConversationModel(Parcel in) {
        mContent = in.readString();
        mMediaModel = in.readParcelable(MediaModel.class.getClassLoader());
        mGravity = in.readInt();
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
    public MediaModel getMediaModel() {
        return mMediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        mMediaModel = mediaModel;
        notifyPropertyChanged(BR.mediaModel);
    }

    @Bindable
    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
        notifyPropertyChanged(BR.gravity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mContent);
        parcel.writeParcelable(mMediaModel, i);
        parcel.writeInt(mGravity);
    }
}
