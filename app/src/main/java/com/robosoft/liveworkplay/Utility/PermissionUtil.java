package com.robosoft.liveworkplay.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Rahul Kumar Pandey on 22-02-2017.
 */
public class PermissionUtil {

    public static final int REQUEST_PERMISSION_SHOW_RATIONALE = 1;
    public static final int REQUEST_PERMISSION_DO_NOT_SHOW_RATIONALE = 0;
    public static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 56;

    public static boolean isVersionMarshmallowAndAbove(){
        return(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static boolean acessExternalStoragePermission(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public static void requestReadExternalStoragePermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }
        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if permission is denied, can show rationale and if permission has been been asked more than once and user has denied the permission with "Never ask again" checked
     * @param permissions
     * @param grantResults
     * @param requestedPermission
     * @param activity
     * @return 1 = permission has denied, but "Never show again" is not checked. A rationale can be shown before asking the permission again.
     * @return 0 = permission has been been asked more than once and user has denied the permission with "Never ask again" checked.
     * @return -1 = Unknown state.
     *
     */
    public static int shouldShowRequestPermissionRationaleState(String[] permissions, int[] grantResults, String requestedPermission, Activity activity){
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                if (! showRationale) {
                    // user denied flagging NEVER ASK AGAIN
                    // you can either enable some fall back,
                    // disable features of your app
                    // or open another dialog explaining
                    // again the permission and directing to
                    // the app setting
                    return REQUEST_PERMISSION_DO_NOT_SHOW_RATIONALE;

                } else if (requestedPermission.equals(permission)) {
                    // user denied WITHOUT never ask again
                    // this is a good place to explain the user
                    // why you need the permission and ask if he want
                    // to accept it (the rationale)

                    return REQUEST_PERMISSION_SHOW_RATIONALE;
                }
                return -1;
            }
        }
        return -1;
    }

    public static void openAppSettingPage(Context context){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
