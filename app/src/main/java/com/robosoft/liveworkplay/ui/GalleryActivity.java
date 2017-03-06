package com.robosoft.liveworkplay.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robosoft.liveworkplay.OnSelectedGalleryItemClickedListner;
import com.robosoft.liveworkplay.R;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.Utility.Util;
import com.robosoft.liveworkplay.adapter.GalleryAdapter;
import com.robosoft.liveworkplay.entity.GalleryPath;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rahul Kumar Pandey on 22-02-2017.
 */

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener, OnSelectedGalleryItemClickedListner {

    private RecyclerView mRecyclerView;
    private List<String> mGalleryData = new ArrayList<>();
    private GalleryAdapter mGalleryAdapter;
    private List<String> mSelectedGalleryItem = new ArrayList<>();
    private TextView mNextBtn, mItemCountTxt;
    private List<GalleryPath> mGalleryPathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initView();
        getGalleryData();
        setAdapter();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.gallery_toolabar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mNextBtn = (TextView)findViewById(R.id.next_btn);
        mNextBtn.setClickable(false);
        mNextBtn.setOnClickListener(this);
        mItemCountTxt = (TextView)findViewById(R.id.selected_image_count);
        mItemCountTxt.setText(getResources().getString(R.string.no_item_selected));
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.back_arrow).setOnClickListener(this);
    }

    private void setAdapter() {
        // sending only 21 item at a time , using pagination
        if (mGalleryData.size() > 21) {
            for (int i = 0; i < 21; i++) {
                // save in model class list
                GalleryPath galleryPath = new GalleryPath();
                galleryPath.setPath(mGalleryData.get(i));
                galleryPath.setmIsSelected(false);
                galleryPath.setmIsVideoType(false);
                mGalleryPathList.add(galleryPath);
            }
        }
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mGalleryAdapter = new GalleryAdapter(GalleryActivity.this , mGalleryPathList);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mGalleryAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    //  mProgressBar.setVisibility(View.VISIBLE);
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                        //Do pagination.. i.e. fetch new data
                        //Add the values in your Array here.

                        if (mGalleryData.size() > mGalleryPathList.size()) {
                            for (int i = 1; i <= 21; i++) {
                                if (mGalleryData.size() - 1 == mGalleryPathList.size())
                                    break;
                               // mOptimizedGalleryData.add(mOptimizedGalleryData.size(), mGalleryData.get(mOptimizedGalleryData.size() + 1));
                                GalleryPath galleryPath = new GalleryPath();
                                galleryPath.setPath(mGalleryData.get(mGalleryPathList.size() + 1));
                                galleryPath.setmIsSelected(false);
                                galleryPath.setmIsVideoType(false);
                                mGalleryPathList.add(galleryPath);
                            }
                        }

                        //Notify the adapter about the data set change.
                        mGalleryAdapter.notifyDataSetChanged();
                        //  mProgressBar.setVisibility(View.GONE);

                    }
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn:
                if (mSelectedGalleryItem.size() == 0)
                    Util.showAlertMessage(this, getResources().getString(R.string.select_data_alert_msg));
                else {
                    // open gallery detail activity
                    Intent intent = new Intent(this, GalleryDetailActivity.class);
                    intent.putStringArrayListExtra(Constants.EXTRA_INTENT_SELECTED_IMAGE, (ArrayList<String>) mSelectedGalleryItem);
                    // intent.putStringArrayListExtra(Constants.EXTRA_INTENT_SELECTED_IMAGE, (ArrayList<String>) mAboutImage);
                    startActivity(intent);
                }
                break;
            case R.id.back_arrow:
                onBackPressed();
                break;
        }
    }

    // method for getting all image and video path from device gallery
    private void getGalleryData() {

        // Get relevant columns for use later.
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };

        // Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + Constants.OR_TXT
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri(Constants.EXTERNAL_MEMORY);

        CursorLoader cursorLoader = new CursorLoader(
                this,
                queryUri,
                projection,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + Constants.SORTING_ORDER // Sort order.
        );

        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);

        if (cursor.moveToFirst()) {
            do {
                // String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                String image = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String video = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                mGalleryData.add(image);

                // String uri = cursor.getString(columnIndex);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onSelectedGalleryItem(int position, String operation) {
        mNextBtn.setClickable(true);
        if (operation.equalsIgnoreCase(Constants.ADD_IMAGE))
            mSelectedGalleryItem.add(mGalleryData.get(position));
        else if (operation.equalsIgnoreCase(Constants.REMOVE_IMAGE))
            mSelectedGalleryItem.remove(mGalleryData.get(position));
        mItemCountTxt.setText(mSelectedGalleryItem.size() + " / " + mGalleryData.size() + getResources().getString(R.string.selected_txt));
    }
}
