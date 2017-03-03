package com.robosoft.liveworkplay.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.robosoft.liveworkplay.R;
import com.robosoft.liveworkplay.OnSendStoryItemClickedListner;
import com.robosoft.liveworkplay.Utility.Constants;
import com.robosoft.liveworkplay.Utility.Util;
import com.robosoft.liveworkplay.adapter.SendStoryAdapter;
import com.robosoft.liveworkplay.Utility.PermissionUtil;
import com.robosoft.liveworkplay.entity.ImageStoryDetail;
import com.robosoft.liveworkplay.helper.PrefrenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendStoryActivity extends AppCompatActivity implements View.OnClickListener,OnSendStoryItemClickedListner {

    private EditText mTitleText;
    private EditText mStoryDescText;
    private List<String> mSelectedItem = new ArrayList<>();
    private HashMap<Integer, ImageStoryDetail> mAboutImage = new HashMap<>();
    private TextView mTextView;
    private TextView mSubmitBtn;
    private SendStoryAdapter mSendStoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_story);
        initView();
        getIntentData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.send_story_toolabar);
        setSupportActionBar(toolbar);
        mTitleText = (EditText)findViewById(R.id.title);
        mStoryDescText = (EditText)findViewById(R.id.story_desc);
        mTextView = (TextView)findViewById(R.id.count_text);
        mSubmitBtn = (TextView)findViewById(R.id.submit_btn);
        // set done btn in edit text
        mStoryDescText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mStoryDescText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mStoryDescText.addTextChangedListener(mTextEditorWatcher);
        findViewById(R.id.add_image_btn).setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.getStringArrayListExtra(Constants.EXTRA_INTENT_SELECTED_IMAGE) != null) {
            // get story details
            PrefrenceHelper.getInstance(this).getStoryTitle();
            mTitleText.setText(PrefrenceHelper.getInstance(this).getStoryTitle());
            mStoryDescText.setText(PrefrenceHelper.getInstance(this).getStoryDesc());

            mSelectedItem = intent.getStringArrayListExtra(Constants.EXTRA_INTENT_SELECTED_IMAGE);
            mAboutImage = (HashMap<Integer, ImageStoryDetail>) intent.getSerializableExtra(Constants.EXTRA_INTENT_ABOUT_IMAGE);
            setAdapter();
            mSubmitBtn.setClickable(true);
        }
    }

    private void setAdapter() {
        RecyclerView selectedImageRecyclerView = (RecyclerView) findViewById(R.id.selected_image_recycler_view);
        mSendStoryAdapter = new SendStoryAdapter(this , mSelectedItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selectedImageRecyclerView.setLayoutManager(linearLayoutManager);
        selectedImageRecyclerView.setAdapter(mSendStoryAdapter);
    }

    // for keeping eye that how many character are entering (max 500 character)
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // This sets a textview to the current remaining length
            mTextView.setText(Integer.toString(500 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_image_btn:
                // check for title and description of story is not null
                if(mTitleText.getText().toString().matches("") || mStoryDescText.getText().toString().matches("")){
                    Util.showAlertMessage(this , getResources().getString(R.string.fill_data_alert_msg));
                } else {
                    // save the tite and description of story
                    saveText();
                    // we have to check for permission is granted or not for above api level 23
                    if (PermissionUtil.isVersionMarshmallowAndAbove() && !PermissionUtil.acessExternalStoragePermission(this)) {
                        //If the OS version is marshmallow and read contacts permission is not given yet,
                        // ask for the permission first
                        PermissionUtil.requestReadExternalStoragePermission(this);
                    } else {
                        Intent intent = new Intent(this, GalleryActivity.class);
                        startActivity(intent);
                    }
                }
                break;

            case R.id.submit_btn:
                if(!mTitleText.getText().toString().matches("") && !mStoryDescText.getText().toString().matches("")) {
                    Intent intent = new Intent(this, SentSucessfulActivity.class);
                    startActivity(intent);
                } else
                    Util.showAlertMessage(this , getResources().getString(R.string.fill_data_alert_msg));
                break;

            case R.id.close:
                finish();
                break;

        }
    }

    private void saveText() {
        PrefrenceHelper.getInstance(this).setStoryTitle(mTitleText.getText().toString());
        PrefrenceHelper.getInstance(this).setStoryDesc(mStoryDescText.getText().toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtil.REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                //permission is given
                Intent intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);

            } else {
                int rationalState = PermissionUtil.shouldShowRequestPermissionRationaleState(permissions, grantResults, Manifest.permission.READ_CONTACTS, this);
                if (rationalState == PermissionUtil.REQUEST_PERMISSION_SHOW_RATIONALE) {
                } else {
                    showPermissionAlertDialog();
                }
            }
        }
    }

    private void showPermissionAlertDialog() {
        try {
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.dialog_alert_title));
            builder.setMessage(getResources().getString(R.string.permission_alert_msg));
            builder.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PermissionUtil.openAppSettingPage(SendStoryActivity.this);
                    SendStoryActivity.this.finish();
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
            //  if (net.one97.paytm.common.utility.CJRAppCommonUtility.isDebug) e.printStackTrace();
        }
    }

    @Override
    public void onSendStoryItemClicked(final int position, String clickType) {
        if(clickType.equalsIgnoreCase(Constants.ON_CLICK_TYPE)){
            // if user click on image of send story activity for editing text again go to gallery detail page for view pager item
            Intent intent = new Intent(this , GalleryDetailActivity.class);
            intent.putExtra(Constants.VIEW_PAGER_POSITION , position);
            intent.putStringArrayListExtra(Constants.EXTRA_INTENT_SELECTED_IMAGE , (ArrayList<String>) mSelectedItem);
            intent.putExtra(Constants.EXTRA_INTENT_ABOUT_IMAGE , mAboutImage);
            intent.putExtra(Constants.VIEW_PAGER_REFERENCE , Constants.VIEW_PAGER_REFERENCE_VALUE);
            startActivity(intent);
        }
        else {
            // show confirmation dialog for removing
            try {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.dialog_alert_title));
                builder.setMessage(getResources().getString(R.string.remove_item_confirmation));
                builder.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSelectedItem.remove(position);
                        mSendStoryAdapter.notifyDataSetChanged();
                        // remove details of image
                        if (mAboutImage != null)
                            mAboutImage.remove(position);
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
