package com.robosoft.liveworkplay.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.robosoft.liveworkplay.Utility.Constants;
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
        GalleryDetailFragment galleryDetailFragment = new GalleryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.GALLERY_DETAIL_BUNDLE_DATA_POSITION, position);
        bundle.putStringArrayList(Constants.GALLERY_DETAIL_BUNDLE_DATA_SELECTEDITEM , (ArrayList<String>) mSelectedGalleryItem);
        galleryDetailFragment.setArguments(bundle);
        return galleryDetailFragment;
    }

    @Override
    public int getCount() {
        return mSelectedGalleryItem.size();
    }
}
