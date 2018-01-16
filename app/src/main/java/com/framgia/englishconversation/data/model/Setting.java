package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.englishconversation.BR;

/**
 * Created by doan.van.toan on 1/16/18.
 */

public class Setting extends BaseObservable {
    private boolean mIsEnableAutoPlay;

    @Bindable
    public boolean isEnableAutoPlay() {
        return mIsEnableAutoPlay;
    }

    public void setEnableAutoPlay(boolean enableAutoPlay) {
        mIsEnableAutoPlay = enableAutoPlay;
        notifyPropertyChanged(BR.enableAutoPlay);
    }
}
