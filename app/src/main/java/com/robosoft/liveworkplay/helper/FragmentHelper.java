package com.robosoft.liveworkplay.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by rahul on 23/2/16.
 */
public class FragmentHelper {

    public  static  void  replaceFragment(Fragment fragment, FragmentManager fragmentManager){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(com.robosoft.liveworkplay.R.id.activity_gallery_detail, fragment);
        transaction.commit();
    }
}
