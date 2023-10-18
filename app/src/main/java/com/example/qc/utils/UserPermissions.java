package com.example.qc.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UserPermissions {
    private static final int MY_PERMISSIONS_CONSTANT = 123;

    //Manifest.permission.READ_PHONE_STATE
    public boolean checkPermission(Activity context, String permission) {
        boolean isPersmission = false;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            isPersmission = false;
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(context,
                        new String[]{permission},
                        MY_PERMISSIONS_CONSTANT);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            isPersmission = true;
        }

        return isPersmission;
    }
}
