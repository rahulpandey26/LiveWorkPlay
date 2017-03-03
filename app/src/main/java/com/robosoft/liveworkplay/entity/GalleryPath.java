package com.robosoft.liveworkplay.entity;

/**
 * Created by Rahul Kumar Pandey on 01-03-2017.
 */

public class GalleryPath {
    private String path;
    private Boolean mIsSelected;
    private Boolean mIsVideoType;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getmIsSelected() {
        return mIsSelected;
    }

    public void setmIsSelected(Boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    public Boolean getmIsVideoType() {
        return mIsVideoType;
    }

    public void setmIsVideoType(Boolean mIsVideoType) {
        this.mIsVideoType = mIsVideoType;
    }
}
