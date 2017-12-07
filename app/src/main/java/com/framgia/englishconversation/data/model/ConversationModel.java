package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fs-sournary.
 * Date on 12/7/17.
 * Description:
 */

public class ConversationModel extends BaseObservable {

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

}
