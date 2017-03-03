package com.robosoft.liveworkplay.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rahul Kumar Pandey on 23-02-2017.
 */

public class ImageStoryDetail implements Parcelable {

    private String mImagePath;
    private String mImageDesc;

    public ImageStoryDetail(String imagePath, String imageDesc) {
        mImagePath = imagePath;
        mImageDesc = imageDesc;
    }

    public String getmImageDesc() {
        return mImageDesc;
    }

    public void setmImageDesc(String mImageDesc) {
        this.mImageDesc = mImageDesc;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public ImageStoryDetail(Parcel in) {
        mImagePath = in.readString();
        mImageDesc = in.readString();
    }

    public static final Creator<ImageStoryDetail> CREATOR = new Creator<ImageStoryDetail>() {
        @Override
        public ImageStoryDetail createFromParcel(Parcel in) {
            return new ImageStoryDetail(in);
        }

        @Override
        public ImageStoryDetail[] newArray(int size) {
            return new ImageStoryDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImagePath);
        parcel.writeString(mImageDesc);
    }
}
