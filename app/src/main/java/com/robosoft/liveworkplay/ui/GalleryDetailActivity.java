package com.robosoft.liveworkplay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.robosoft.liveworkplay.R;
import com.robosoft.liveworkplay.OnSendAboutImageClickListner;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.entity.ImageStoryDetail;
import com.robosoft.liveworkplay.helper.FragmentHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 27-02-2017.
 */

public class GalleryDetailActivity extends AppCompatActivity implements OnSendAboutImageClickListner, View.OnClickListener {

    private List<String> mSelectedGalleryItem = new ArrayList<>();
    private HashMap<Integer, ImageStoryDetail> mImageStoryDetail = new HashMap<>();
    private int mSelectedPosition;
    private String mRef = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        initView();
        getIntentData();
        startGalleryFragment();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mSelectedGalleryItem = intent.getStringArrayListExtra(Constants.EXTRA_INTENT_SELECTED_IMAGE);
        mImageStoryDetail = (HashMap<Integer, ImageStoryDetail>) intent.getSerializableExtra(Constants.EXTRA_INTENT_ABOUT_IMAGE);
        mSelectedPosition = intent.getIntExtra(Constants.VIEW_PAGER_POSITION , 0);
        mRef = intent.getStringExtra(Constants.VIEW_PAGER_REFERENCE);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.gallery_detail_toolabar);
        setSupportActionBar(toolbar);
        findViewById(R.id.next_btn).setOnClickListener(this);
        findViewById(R.id.back_arrow).setOnClickListener(this);
    }

    private void startGalleryFragment() {
        GalleryViewPagerFragment galleryViewPagerFragment = new GalleryViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.EXTRA_INTENT_SELECTED_IMAGE, (ArrayList<String>) mSelectedGalleryItem);
        bundle.putSerializable(Constants.EXTRA_INTENT_ABOUT_IMAGE,  mImageStoryDetail);
        bundle.putInt(Constants.VIEW_PAGER_POSITION , mSelectedPosition);
        bundle.putString(Constants.VIEW_PAGER_REFERENCE , mRef);
        galleryViewPagerFragment.setArguments(bundle);
        FragmentHelper.replaceFragment(R.id.activity_gallery_detail, galleryViewPagerFragment, getSupportFragmentManager());
    }

    @Override
    public void sendAboutImage(HashMap<Integer, ImageStoryDetail> aboutImage) {
        mImageStoryDetail = aboutImage;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.next_btn:
                Intent intent = new Intent(this, SendStoryActivity.class);
                intent.putStringArrayListExtra(Constants.EXTRA_INTENT_SELECTED_IMAGE, (ArrayList<String>) mSelectedGalleryItem);
                intent.putExtra(Constants.EXTRA_INTENT_ABOUT_IMAGE,  mImageStoryDetail);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.back_arrow:
                onBackPressed();
                break;
        }
    }
}
