package com.robosoft.liveworkplay.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.robosoft.liveworkplay.OnGalleryItemClickedListner;
import com.robosoft.liveworkplay.R;
import com.robosoft.liveworkplay.OnSendAboutImageClickListner;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.adapter.SendStoryAdapter;
import com.robosoft.liveworkplay.adapter.ViewPagerAdapter;
import com.robosoft.liveworkplay.entity.ImageStoryDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rahul Kumar Pandey on 23-02-2017.
 */

public class GalleryViewPagerFragment extends Fragment implements OnGalleryItemClickedListner {

    private ViewPager mViewPager;
    private List<String> mSelectedGalleryItem = new ArrayList<>();
    private RecyclerView mGalleryDetailRecyclerView;
    private SendStoryAdapter mSendStoryAdapter;
    private int mPosition;
    private int mViewPagerSelectedPos;
    private EditText mImageStory;
    private OnSendAboutImageClickListner mSendAboutImageClickListner;
    private HashMap<Integer, ImageStoryDetail> mImageStoryContent = new HashMap();
    private ViewPagerAdapter mViewPagerAdapter;
    private String mRef = "";
    private List<Integer> mSelectedPos = new ArrayList<>();
    private String aboutImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_view_pager, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mImageStory = (EditText) view.findViewById(R.id.story_desc);
        mImageStory.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mImageStory.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mGalleryDetailRecyclerView = (RecyclerView) view.findViewById(R.id.gallery_detail_recycler_view);
        mSelectedGalleryItem = getArguments().getStringArrayList(Constants.EXTRA_INTENT_SELECTED_IMAGE);
        mRef = getArguments().getString(Constants.VIEW_PAGER_REFERENCE);
        if (!TextUtils.isEmpty(mRef))
            mImageStoryContent = (HashMap<Integer, ImageStoryDetail>) getArguments().getSerializable(Constants.EXTRA_INTENT_ABOUT_IMAGE);
        mPosition = getArguments().getInt(Constants.VIEW_PAGER_POSITION);

        mImageStory.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    savingImageDetail(mSelectedGalleryItem.size());
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSendAboutImageClickListner = (OnSendAboutImageClickListner) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), mSelectedGalleryItem);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(mPosition);
        setAdapter();

        if (!TextUtils.isEmpty(mRef)) {
            if (mImageStoryContent != null)
                mImageStory.setText(mImageStoryContent.get(mPosition).getmImageDesc());
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // for last position
                if (position == mSelectedGalleryItem.size() - 1) {
                    // savingImageDetail(position);
                    mImageStory.addTextChangedListener(mTextEditorWatcher);
                }

            }

            @Override
            public void onPageSelected(int position) {
                mViewPagerSelectedPos = position;
                mSendStoryAdapter.getViewPagerItemPosition(mViewPagerSelectedPos);

                // add all story about image in a list
                // storing in model class
                mSelectedPos.add(position);
                int count = 0;

                if (TextUtils.isEmpty(mRef)) {
                    for (int i = 0; i < mSelectedPos.size(); i++) {
                        if (mSelectedPos.get(i) == position) {
                            count++;
                            // for 0 position it will call only when u r going back in view pager and that data is also saved
                            // so increment by 1 , so that it will not saved again.
                            if (position == 0)
                                count++;
                        }
                    }

                    if (count >= 2) {
                        // as data is saved so no need to do anything.
                        // if(mImageStoryDetails.get(position).getmImageDesc().equalsIgnoreCase(""))
                        //  savingImageDetail(position);
                    } else
                        savingImageDetail(position);

                }

                // if image description is available for this position then show
                if (position >= mImageStoryContent.size())
                    mImageStory.setText("");
                else {
                    if (!mImageStoryContent.get(position).getmImageDesc().equalsIgnoreCase("")) {
                        mImageStory.setText(mImageStoryContent.get(position).getmImageDesc());
                    } else
                        mImageStory.setText("");
                }

                if (!TextUtils.isEmpty(mRef)) {
                    /* if(!mImageStoryContent.get(position).getmImageDesc().equalsIgnoreCase(mImageStoryContent.get(position).getmImageDesc()))
                            savingImageDetail(position);*/
                }
                // sending image description
                mSendAboutImageClickListner.sendAboutImage(mImageStoryContent);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // for saving image description of last image
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            aboutImage = s.toString();
            // This sets a textview to the current length
        }

        public void afterTextChanged(Editable s) {
        }
    };


    private void savingImageDetail(int position) {
        int newPosition;
        if (position > 0) {
            newPosition = position - 1;
        } else
            newPosition = 0;
        ImageStoryDetail imageStoryDetail = new ImageStoryDetail(mSelectedGalleryItem.get(newPosition), mImageStory.getText().toString());
        imageStoryDetail.setmImagePath(mSelectedGalleryItem.get(newPosition));
        imageStoryDetail.setmImageDesc(String.valueOf(mImageStory.getText()));
        mImageStoryContent.put(newPosition, imageStoryDetail);
    }


    private void setAdapter() {
        mSendStoryAdapter = new SendStoryAdapter(this, mSelectedGalleryItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGalleryDetailRecyclerView.setLayoutManager(linearLayoutManager);
        mGalleryDetailRecyclerView.setAdapter(mSendStoryAdapter);
    }

    @Override
    public void onGalleryItemClicked(final int position, String clickType) {
        if (clickType.equalsIgnoreCase(Constants.ON_CLICK_TYPE)) {
            // setting view pager item according to which item is clicked on recycler view
            mViewPager.setCurrentItem(position);
        } else {
            // show confirmation dialog for removing
            try {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle(getResources().getString(R.string.dialog_alert_title));
                builder.setMessage(getResources().getString(R.string.remove_item_confirmation));
                builder.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSelectedGalleryItem.remove(position);
                        mSendStoryAdapter.notifyDataSetChanged();
                        mViewPagerAdapter.notifyDataSetChanged();
                        // remove image detail
                        if (mImageStoryContent != null)
                            mImageStoryContent.remove(position);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            } catch (Exception e) {

            }
        }
    }
}
