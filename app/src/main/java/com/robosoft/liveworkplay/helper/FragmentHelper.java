package com.robosoft.liveworkplay.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by rahul on 23/2/16.
 */
public class FragmentHelper {

    public  static  void  replaceFragment(int containerId, Fragment fragment, FragmentManager fragmentManager ){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }
}
