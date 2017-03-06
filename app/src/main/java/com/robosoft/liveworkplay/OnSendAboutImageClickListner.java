package com.robosoft.liveworkplay;

import com.robosoft.liveworkplay.entity.ImageStoryDetail;
import com.robosoft.liveworkplay.entity.StoryDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 24-02-2017.
 */

public interface OnSendAboutImageClickListner {
    void sendAboutImage(HashMap<Integer, ImageStoryDetail> aboutImage);
}
