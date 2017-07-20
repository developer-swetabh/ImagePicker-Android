package com.swetabh.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by swets on 17-07-2017.
 */

public class ImagePicker {

    private final int GALLERY_RESULT_CODE = 901, CAMERA_RESULT_CODE = 902;
    private final String IMAGE_MIME_TYPE = "image/*";
    private final String TAG = ImagePicker.class.getSimpleName();
    private final String PROVIDER_AUTHORITY;
    private WeakReference<Activity> activityWeakReference;
    private String mCurrentPhotoPath;

    /**
     * Constructor to take
     *
     * @param activity     - for reference from where it is called
     * @param providerName - provider name
     */
    public ImagePicker(@NonNull Activity activity, @NonNull int providerName) {
        activityWeakReference = new WeakReference<Activity>(activity);
        PROVIDER_AUTHORITY = activityWeakReference.get().getString(providerName);
    }

    /*
    * method to check permission of external storage
    * */
    public boolean checkPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * method for creating file name
     *
     * @param activity
     */
    private File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = new File(storageDir, File.separator + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void RequestPermission(@NonNull String permission, int resultCode) {
        if (activityWeakReference.get() == null) {
            return;
        }
        Activity activity = activityWeakReference.get();
        // No explanation needed, we can request the permission.

        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                resultCode);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == CAMERA_RESULT_CODE
                || requestCode == GALLERY_RESULT_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == CAMERA_RESULT_CODE) {
                    pickImageFromCamera();
                } else {
                    pickImageFromGallery();
                }

            } else {
                if (activityWeakReference.get() == null) {
                    return;
                }

                Activity activity = activityWeakReference.get();
                Toast.makeText(activity, activity.getString(R.string.storage_denial), Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    * method used for picking image from gallery
    * */
    public void pickImageFromGallery() {
        if (activityWeakReference.get() == null) {
            return;
        }
        Activity activity = activityWeakReference.get();
        if (checkPermission(activity)) {
            Intent intent = new Intent();
            intent.setType(IMAGE_MIME_TYPE);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, "Select picture"), GALLERY_RESULT_CODE);
            Log.d(TAG, "gallery");
        } else {
            RequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, GALLERY_RESULT_CODE);
        }
    }


    /**
     * method used for picking image from camera
     */
    public void pickImageFromCamera() {
        if (activityWeakReference.get() == null) {
            return;
        }
        Activity activity = activityWeakReference.get();
        if (checkPermission(activity)) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile(activity);
                    mCurrentPhotoPath = photoFile.getPath();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
                Uri photoURI = FileProvider.getUriForFile(activity,
                        PROVIDER_AUTHORITY,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(takePictureIntent, CAMERA_RESULT_CODE);
            }

        } else {
            RequestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, CAMERA_RESULT_CODE);
        }
    }

    /*
    * method used for getting path of the selected image
    * */
    public String onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && (requestCode == CAMERA_RESULT_CODE || requestCode == GALLERY_RESULT_CODE)) {

            if (requestCode == CAMERA_RESULT_CODE) {
                return mCurrentPhotoPath;
            } else {
                return getRealPathFromURI(data.getData());
            }
        }
        return null;
    }

    /**
     * method to get real path from uri
     *
     * @param contentURI
     */
    private String getRealPathFromURI(Uri contentURI) {
        String result = null;
        Activity activity = activityWeakReference.get();
        Cursor cursor = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
            cursor = activity.getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        } else {
            cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
