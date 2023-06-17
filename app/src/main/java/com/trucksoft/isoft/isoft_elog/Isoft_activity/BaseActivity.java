package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


/**
 * Created by nil on 10/03/16.
 */
public class BaseActivity extends AppCompatActivity {

    public static final int PERMISSION_LOCATION = 1;
    public static final int PERMISSION_READ_STORAGE = 2;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE= 3;
    public static final int PERMISSION_CAMERA = 4;
    public static final int PERMISSION_NETWORKLOCATION = 5;
    private static final int REQUEST_SMS = 0;
    public static final int RECORD_AUDIOS = 6;
    public static final int WRITE_CONTACTS = 7;


//boolean boolok=true;
    public boolean checkPermission(int permission) {

        if (ActivityCompat.checkSelfPermission(this, getPermission(permission)) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }

    }


    public String getPermission(int permis) {

        String permission = null;

        switch (permis) {
            case REQUEST_SMS:
                permission = Manifest.permission.SEND_SMS;
                return permission;
            case PERMISSION_LOCATION:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                return permission;
            case PERMISSION_READ_STORAGE:
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                return permission;
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                return permission;
            case PERMISSION_CAMERA:
                permission= Manifest.permission.CAMERA;
                return permission;
            case PERMISSION_NETWORKLOCATION:
                permission= Manifest.permission.ACCESS_COARSE_LOCATION;
                return permission;
//
            case RECORD_AUDIOS:
                permission=Manifest.permission.RECORD_AUDIO;
                return permission;
            case WRITE_CONTACTS:
                permission=Manifest.permission.WRITE_EXTERNAL_STORAGE;
                return permission;

        }
        return permission;
    }  public void requestForPermission(final int permission) {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                getPermission(permission))) {


            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Message
            alertDialog.setMessage("Permission is needed to perform action...");

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // boolok=true;
                    ActivityCompat.requestPermissions(BaseActivity.this, new String[]{getPermission(permission)}, permission);

                }
            });

//            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Write your code here to invoke NO event
//                    boolok=false;
//                    dialog.dismiss();
//                }
//            });

            // Showing Alert Message
            alertDialog.show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{getPermission(permission)}, permission);
            //  boolok=true;
        }
        // END_INCLUDE(camera_permission_request)

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Camera permission has been granted, preview can be displayed

//            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//
//            // Setting Dialog Message
//            alertDialog.setMessage("Permission granted...");
//
//            // Setting Positive "Yes" Button
//            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//
//                    dialog.dismiss();
//
//                }
//            });
//            // Showing Alert Message
//            alertDialog.show();

        } else {

            /*final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            // Setting Dialog Message
            alertDialog.setMessage("Permission not granted...");

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });
            // Showing Alert Message
            alertDialog.show();*/
        }

    }


}
