package com.robosoft.liveworkplay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.robosoft.liveworkplay.ui.GalleryDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 23-02-2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mSelectedGalleryItem = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, List<String> selectedGalleryItem) {
        super(fm);
        mSelectedGalleryItem = selectedGalleryItem;
    }

    @Override
    public Fragment getItem(int position) {
        return new GalleryDetailFragment(mSelectedGalleryItem , position);
    }

    @Override
    public int getCount() {
        return mSelectedGalleryItem.size();
    }
}
