package com.robosoft.liveworkplay.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.robosoft.liveworkplay.OnSelectedGalleryItemClickedListner;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.Utility.Util;
import com.robosoft.liveworkplay.entity.GalleryPath;
import com.robosoft.liveworkplay.ui.GalleryActivity;
import com.robosoft.liveworkplay.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 22-02-2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<Integer> mSelecetdPosition = new ArrayList<>();
    private OnSelectedGalleryItemClickedListner mOnSelectedGalleryItemClickedListner;
    private List<GalleryPath> mGalleryPathList;

    public GalleryAdapter(OnSelectedGalleryItemClickedListner context, List<GalleryPath> galleryPath) {
        mOnSelectedGalleryItemClickedListner = context;
        mGalleryPathList = galleryPath;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gallery_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String galleryPath = mGalleryPathList.get(position).getPath();
        String extension = galleryPath.substring(galleryPath.lastIndexOf(".") + 1);
        // checking for extension of path weather its a image or video path
        if (extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_JPG) || extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_JPEG) || extension.equalsIgnoreCase(Constants.IMAGE_EXTENSION_PNG)) {
            // Reducing the image size , so it will load faster
            Picasso.with(holder.mImage.getContext()).load(new File(galleryPath)).resize(150, 150).into(holder.mImage);
            mGalleryPathList.get(position).setmIsVideoType(false);
        } else {
            // getting video thumbnail and the setting in image view
            holder.mImage.setImageBitmap(Util.getVideoThumbnail(galleryPath));
            // set visibilty for video icon
            holder.mPlayVideoImage.setVisibility(View.VISIBLE);
            // set boolean value true if it is video
            mGalleryPathList.get(position).setmIsVideoType(true);
        }

        // handle video icon while scrolling recycler view item
        if (mGalleryPathList.get(position).getmIsVideoType())
            holder.mPlayVideoImage.setVisibility(View.VISIBLE);
        else
            holder.mPlayVideoImage.setVisibility(View.GONE);

        // handle selection image while scrolling recycler view item
        if (mGalleryPathList.get(position).getmIsSelected())
            holder.mSelectedImage.setVisibility(View.VISIBLE);
        else
            holder.mSelectedImage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mGalleryPathList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private ImageView mSelectedImage;
        private ImageView mPlayVideoImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            mSelectedImage = (ImageView) itemView.findViewById(R.id.selected_icon);
            mPlayVideoImage = (ImageView) itemView.findViewById(R.id.play_video_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // operation for getting selected or unselected image , if selected then only add position
                    if (!mSelecetdPosition.contains(getAdapterPosition())) {
                        mSelecetdPosition.add(getAdapterPosition());
                        mSelectedImage.setVisibility(View.VISIBLE);
                        // set boolean value true if item is selected
                        mGalleryPathList.get(getAdapterPosition()).setmIsSelected(true);
                        mOnSelectedGalleryItemClickedListner.onSelectedGalleryItem(getAdapterPosition(), Constants.ADD_IMAGE);
                    } else {
                        mSelectedImage.setVisibility(View.GONE);
                        mGalleryPathList.get(getAdapterPosition()).setmIsSelected(false);
                        for (int pos = 0; pos < mSelecetdPosition.size(); pos++) {
                            if (mSelecetdPosition.get(pos) == getAdapterPosition()) {
                                mSelecetdPosition.remove(pos);
                                mOnSelectedGalleryItemClickedListner.onSelectedGalleryItem(getAdapterPosition(), Constants.REMOVE_IMAGE);
                            }
                        }
                    }
                }
            });
        }
    }
}
