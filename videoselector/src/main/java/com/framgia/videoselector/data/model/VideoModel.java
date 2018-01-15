package com.framgia.videoselector.data.model;

import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import com.framgia.videoselector.BR;

/**
 * Created by doan.van.toan on 1/15/18.
 */

public class VideoModel extends BaseObservable implements Parcelable {
    private String mId;
    private String mFilePath;
    private String mCreatedAt;
    private String mModifyAt;
    private String mTitle;
    private String mDisplayName;
    private long mSize;
    private String mThumb;
    private boolean mIsSelected;

    public VideoModel(Cursor cursor) {
        mId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
        mFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
        mCreatedAt =
            cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED));
        mModifyAt =
            cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED));
        mTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
        mDisplayName =
            cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME));
        mThumb = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
        mSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE));
    }

    protected VideoModel(Parcel in) {
        mId = in.readString();
        mFilePath = in.readString();
        mCreatedAt = in.readString();
        mModifyAt = in.readString();
        mTitle = in.readString();
        mDisplayName = in.readString();
        mSize = in.readLong();
        mThumb = in.readString();
    }

    public static final Creator<VideoModel> CREATOR = new Creator<VideoModel>() {
        @Override
        public VideoModel createFromParcel(Parcel in) {
            return new VideoModel(in);
        }

        @Override
        public VideoModel[] newArray(int size) {
            return new VideoModel[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getModifyAt() {
        return mModifyAt;
    }

    public void setModifyAt(String modifyAt) {
        mModifyAt = modifyAt;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        mSize = size;
    }

    @Override
    public String toString() {
        return "VideoDataSource{"
            + "mId='"
            + mId
            + '\''
            + ", mFilePath='"
            + mFilePath
            + '\''
            + ", mThumb='"
            + mThumb
            + '\''
            + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mFilePath);
        dest.writeString(mCreatedAt);
        dest.writeString(mModifyAt);
        dest.writeString(mTitle);
        dest.writeString(mDisplayName);
        dest.writeLong(mSize);
        dest.writeString(mThumb);
    }

    @Bindable
    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
        notifyPropertyChanged(BR.selected);
    }
}
