package com.robosoft.liveworkplay.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.robosoft.liveworkplay.OnGalleryItemClickedListner;
import com.robosoft.liveworkplay.R;

import java.util.ArrayList;
import java.util.List;

import com.robosoft.liveworkplay.OnSendStoryItemClickedListner;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.Utility.Util;
import com.robosoft.liveworkplay.ui.GalleryViewPagerFragment;
import com.robosoft.liveworkplay.ui.SendStoryActivity;


/**
 * Created by Rahul Kumar Pandey on 23-02-2017.
 */

public class SendStoryAdapter extends RecyclerView.Adapter<SendStoryAdapter.ViewHolder> {

    private List<String> mSelectedGalleryData = new ArrayList<>();
    private OnGalleryItemClickedListner mOnGalleryItemClickedListner;
    private OnSendStoryItemClickedListner mSendStoryItemClickedListner;
    private int mViewPagerItemPosition;
    private String mReference;

    public SendStoryAdapter(SendStoryActivity sendStoryActivity, List<String> selectedItem) {
        mSelectedGalleryData = selectedItem;
        mSendStoryItemClickedListner = sendStoryActivity;
        mReference = Constants.SEND_STORY_REFERENCE;
    }

    public SendStoryAdapter(GalleryViewPagerFragment galleryViewPagerFragment, List<String> selectedGalleryItem) {
        mSelectedGalleryData = selectedGalleryItem;
        mOnGalleryItemClickedListner = galleryViewPagerFragment;
        mReference = Constants.SEND_STORY_GALLERY_REFERENCE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_send_story_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String extension = mSelectedGalleryData.get(position).substring(mSelectedGalleryData.get(position).lastIndexOf(".") + 1);
        if (extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_JPG) || extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_JPEG) || extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_PNG)) {
            // resizing image
            holder.mSelectedImage.setImageBitmap(Util.getThumbnailBitmap(mSelectedGalleryData.get(position), 100));

        } else {
            // getting video thumbnail and the setting in image view
            holder.mSelectedImage.setImageBitmap(Util.getVideoThumbnail(mSelectedGalleryData.get(position)));
            // set visibilty for video icon
            holder.mPlayVideoIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mSelectedGalleryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mSelectedImage, mPlayVideoIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mSelectedImage = (ImageView) itemView.findViewById(R.id.selected_image);
            mPlayVideoIcon = (ImageView) itemView.findViewById(R.id.play_video_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // mSelectedImage.setVisibility(View.VISIBLE);
                    // check for reference that from where its calling so that there we have to send position
                    if (mReference.matches(Constants.SEND_STORY_GALLERY_REFERENCE))
                        mOnGalleryItemClickedListner.onGalleryItemClicked(getAdapterPosition(), Constants.ON_CLICK_TYPE);
                    else if (mReference.matches(Constants.SEND_STORY_REFERENCE))
                        mSendStoryItemClickedListner.onSendStoryItemClicked(getAdapterPosition(), Constants.ON_CLICK_TYPE);
                }
            });

            // using long click listner for removing the recyler view item
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mReference.matches(Constants.SEND_STORY_GALLERY_REFERENCE))
                        mOnGalleryItemClickedListner.onGalleryItemClicked(getAdapterPosition(), Constants.ON_LONG_CLICK_TYPE);
                    else if (mReference.matches(Constants.SEND_STORY_REFERENCE))
                        mSendStoryItemClickedListner.onSendStoryItemClicked(getAdapterPosition(), Constants.ON_LONG_CLICK_TYPE);
                    return false;
                }
            });
        }
    }

    public void getViewPagerItemPosition(int position) {
        mViewPagerItemPosition = position;
    }
}

