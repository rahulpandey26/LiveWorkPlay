package com.robosoft.liveworkplay.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.robosoft.liveworkplay.R;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.Utility.Util;
import com.robosoft.liveworkplay.entity.ImageStoryDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 23-02-2017.
 */

public class GalleryDetailFragment extends Fragment {

    private List<String> mSelectedGalleryItem = new ArrayList<>();
    private int mPosition;
    private ImageView mGalleryImage, mSelectedPlayVideoImage;
    private TextView mImageCount;

    public GalleryDetailFragment(List<String> selectedGalleryItem, int position) {
        mSelectedGalleryItem = selectedGalleryItem;
        mPosition = position;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_detail, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mGalleryImage = (ImageView)view.findViewById(R.id.gallery_image);
        mImageCount = (TextView)view.findViewById(R.id.image_count);
        mSelectedPlayVideoImage = (ImageView)view.findViewById(R.id.selected_play_video_image);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // if it is a single digit then add zero before number
        if (mPosition < 10 && mSelectedGalleryItem.size() < 10) {
            String position = String.format("%02d", mPosition + 1);
            String totalSelection = String.format("%02d", mSelectedGalleryItem.size());
            mImageCount.setText(position + "/" + totalSelection);
        } else
            mImageCount.setText(mPosition + 1 + "/" + mSelectedGalleryItem.size());

        // for checking its a image or video
        String extension = mSelectedGalleryItem.get(mPosition).substring(mSelectedGalleryItem.get(mPosition).lastIndexOf(".") + 1);
        if (extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_JPG) || extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_JPEG) || extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_PNG))
            mGalleryImage.setImageBitmap(Util.getThumbnailBitmap(mSelectedGalleryItem.get(mPosition), 400));
        else {
            mGalleryImage.setImageBitmap(Util.getVideoThumbnail(mSelectedGalleryItem.get(mPosition)));
            mSelectedPlayVideoImage.setVisibility(View.VISIBLE);
        }
    }

}
