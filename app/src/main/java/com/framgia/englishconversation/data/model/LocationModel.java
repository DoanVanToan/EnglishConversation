package com.framgia.englishconversation.data.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.google.gson.annotations.SerializedName;
import com.framgia.englishconversation.BR;

/**
 * Created by framgia on 16/05/2017.
 */

public class LocationModel extends BaseObservable {
    @SerializedName("lat")
    private double mLat;
    @SerializedName("lng")
    private double mLng;
    @SerializedName("address")
    private String mAddress;

    @Bindable
    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
        notifyPropertyChanged(BR.lat);
    }

    @Bindable
    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
        notifyPropertyChanged(BR.lng);
    }

    @Bindable
    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
        notifyPropertyChanged(BR.address);
    }
}
