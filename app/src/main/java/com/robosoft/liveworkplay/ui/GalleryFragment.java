package com.robosoft.liveworkplay.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.robosoft.liveworkplay.OnSelectedGalleryItemClickedListner;
import com.robosoft.liveworkplay.R;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.adapter.GalleryAdapter;
import com.robosoft.liveworkplay.entity.GalleryPath;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 09-03-2017.
 */

public class GalleryFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private List<String> mGalleryData = new ArrayList<>();
    private List<GalleryPath> mGalleryPathList = new ArrayList<>();
    private GalleryAdapter mGalleryAdapter;
    private OnSelectedGalleryItemClickedListner mOnSelectedGalleryItemClickedListner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        initView(view);
        getBundleData();
        setAdapter();
        return view;
    }

    private void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    private void getBundleData() {
        mGalleryData = getArguments().getStringArrayList(Constants.GALLERY_DATA);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnSelectedGalleryItemClickedListner = (OnSelectedGalleryItemClickedListner) context;
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
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mGalleryAdapter = new GalleryAdapter(mOnSelectedGalleryItemClickedListner, mGalleryPathList);
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
    public void onDetach() {
        super.onDetach();
        mOnSelectedGalleryItemClickedListner = null;
    }
}
