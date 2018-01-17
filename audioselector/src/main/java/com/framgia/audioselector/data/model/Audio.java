package com.framgia.audioselector.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.audioselector.BR;

/**
 * Created by fs-sournary.
 * Date on 1/16/18.
 * Description:
 */

public class Audio extends BaseObservable implements Parcelable {

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
    private String mId;
    private String mPath;
    private String mName;
    private boolean mIsSelected;

    public Audio(String id, String name, String path, boolean isSelected) {
        mId = id;
        mName = name;
        mPath = path;
        mIsSelected = isSelected;
    }

    protected Audio(Parcel in) {
        mPath = in.readString();
        mName = in.readString();
        mId = in.readString();
        mIsSelected = in.readByte() != 0;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Bindable
    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPath);
        parcel.writeString(mName);
        parcel.writeString(mId);
        parcel.writeByte((byte) (mIsSelected ? 1 : 0));
    }
}
