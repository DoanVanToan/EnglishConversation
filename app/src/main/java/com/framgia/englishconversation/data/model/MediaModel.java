package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import com.google.gson.annotations.SerializedName;
import com.framgia.englishconversation.BR;

import static com.framgia.englishconversation.data.model.MediaModel.MediaType.AUDIO;
import static com.framgia.englishconversation.data.model.MediaModel.MediaType.CONVERSATION;
import static com.framgia.englishconversation.data.model.MediaModel.MediaType.IMAGE;
import static com.framgia.englishconversation.data.model.MediaModel.MediaType.VIDEO;

/**
 * Created by framgia on 16/05/2017.
 */

public class MediaModel extends BaseObservable implements Parcelable {
    @SerializedName("id")
    private String mId;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("type")
    @MediaType
    private int mType;
    @SerializedName("name")
    private String mName;
    @SerializedName("upload_percent")
    private int mUploadPercent;

    public MediaModel() {
    }

    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };

    protected MediaModel(Parcel in) {
        mId = in.readString();
        mUrl = in.readString();
        mType = in.readInt();
        mName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mUrl);
        parcel.writeInt(mType);
        parcel.writeString(mName);
    }

    @IntDef({ IMAGE, VIDEO, AUDIO, CONVERSATION })
    @interface MediaType {
        int IMAGE = 0;
        int VIDEO = 1;
        int AUDIO = 2;
        int CONVERSATION = 3;
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
    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getUploadPercent() {
        return mUploadPercent;
    }

    public void setUploadPercent(int uploadPercent) {
        mUploadPercent = uploadPercent;
        notifyPropertyChanged(BR.uploadPercent);
    }
}
