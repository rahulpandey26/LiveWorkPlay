package com.robosoft.liveworkplay.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 28-02-2017.
 */

public class StoryDetail {
    private List<ImageStoryDetail> mImageStory = new ArrayList<>();

    public List<ImageStoryDetail> getmImageStory() {
        return mImageStory;
    }

    public void setmImageStory(List<ImageStoryDetail> mImageStory) {
        this.mImageStory = mImageStory;
    }
}
