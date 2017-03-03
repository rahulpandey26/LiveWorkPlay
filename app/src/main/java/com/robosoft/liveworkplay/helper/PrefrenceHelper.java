package com.robosoft.liveworkplay.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 25-02-2017.
 */

public class PrefrenceHelper {

    private static PrefrenceHelper sInstance;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private interface PreferenceKeys {
        String STORY_TITLE = "title";
        String STORY_DESC = "storyDesc";
    }

    private PrefrenceHelper(Context ctx) {
        mPrefs = ctx.getApplicationContext().getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public static PrefrenceHelper getInstance(Context ctx) {
        if (sInstance == null)
            sInstance = new PrefrenceHelper(ctx.getApplicationContext());
        return sInstance;
    }

    public void clearUserData() {
        mEditor.remove(PreferenceKeys.STORY_TITLE)
                .remove(PreferenceKeys.STORY_DESC).commit();
    }

    public void setStoryTitle(String accessToken) {
        mEditor.putString(PreferenceKeys.STORY_TITLE, accessToken).apply();
    }

    public String getStoryTitle() {
        return mPrefs.getString(PreferenceKeys.STORY_TITLE, null);
    }

    public void setStoryDesc(String accessToken) {
        mEditor.putString(PreferenceKeys.STORY_DESC, accessToken).apply();
    }

    public String getStoryDesc() {
        return mPrefs.getString(PreferenceKeys.STORY_DESC, null);
    }
}
