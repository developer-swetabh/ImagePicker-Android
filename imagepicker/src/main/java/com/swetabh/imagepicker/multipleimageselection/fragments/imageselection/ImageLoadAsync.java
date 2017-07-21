package com.swetabh.imagepicker.multipleimageselection.fragments.imageselection;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.swetabh.imagepicker.multipleimageselection.models.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by swets on 21-07-2017.
 */

public class ImageLoadAsync extends AsyncTask<Void, Void, ArrayList<Image>> {

    private final String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA};
    private Context mContext;
    private ImageResultCallback mCallback;
    private String mAlbum;
    private ArrayList<Image> images;


    public ImageLoadAsync(Context context, ImageResultCallback callback, String album, ArrayList<Image> images) {
        mContext = context;
        mCallback = callback;
        mAlbum = album;
        this.images = images;
    }

    @Override
    protected void onPreExecute() {
        mCallback.fetchStarted();
    }

    @Override
    protected ArrayList<Image> doInBackground(Void... voids) {

        File file;
        HashSet<Long> selectedImages = new HashSet<>();
        if (images != null) {
            Image image;
            for (int i = 0, l = images.size(); i < l; i++) {
                image = images.get(i);
                file = new File(image.path);
                if (file.exists() && image.isSelected) {
                    selectedImages.add(image.id);
                }
            }
        }

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{mAlbum}, MediaStore.Images.Media.DATE_ADDED);
        if (cursor == null) {
            return null;
        }

            /*
            In case this runnable is executed to onChange calling loadImages,
            using countSelected variable can result in a race condition. To avoid that,
            tempCountSelected keeps track of number of selected images. On handling
            FETCH_COMPLETED message, countSelected is assigned value of tempCountSelected.
             */
        int tempCountSelected = 0;
        ArrayList<Image> temp = new ArrayList<>(cursor.getCount());
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return null;
                }

                long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                String path = cursor.getString(cursor.getColumnIndex(projection[2]));
                boolean isSelected = selectedImages.contains(id);
                if (isSelected) {
                    tempCountSelected++;
                }

                file = new File(path);
                if (file.exists()) {
                    temp.add(new Image(id, name, path, isSelected));
                }

            } while (cursor.moveToPrevious());
        }
        cursor.close();

        if (images == null) {
            images = new ArrayList<>();
        }
        images.clear();
        images.addAll(temp);
        return images;
    }

    @Override
    protected void onPostExecute(ArrayList<Image> images) {
        if(images == null){
            mCallback.errorOccured();
        }else {
            mCallback.fetchCompleted(images);
        }
    }
}
