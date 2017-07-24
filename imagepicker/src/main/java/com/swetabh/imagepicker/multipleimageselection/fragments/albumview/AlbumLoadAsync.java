package com.swetabh.imagepicker.multipleimageselection.fragments.albumview;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Process;
import android.provider.MediaStore;

import com.swetabh.imagepicker.multipleimageselection.models.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by swets on 20-07-2017.
 */

public final class AlbumLoadAsync extends AsyncTask<Void, Void, ArrayList<Album>> {

    private final String[] projection = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA};
    private AlbumResultCallback mCallback;
    private Context mContext;

    public AlbumLoadAsync(AlbumResultCallback callback, Context context) {
        mCallback = callback;
        mContext = context;
    }

    @Override
    protected ArrayList<Album> doInBackground(Void... voids) {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Cursor cursor = mContext.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, MediaStore.Images.Media.DATE_ADDED);
        if (cursor == null) {
            return null;
        }

        ArrayList<Album> temp = new ArrayList<>(cursor.getCount());
        HashSet<Long> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return null;
                }

                long albumId = cursor.getLong(cursor.getColumnIndex(projection[0]));
                String album = cursor.getString(cursor.getColumnIndex(projection[1]));
                String image = cursor.getString(cursor.getColumnIndex(projection[2]));

                if (!albumSet.contains(albumId)) {
                        /*
                        It may happen that some image file paths are still present in cache,
                        though image file does not exist. These last as long as media
                        scanner is not run again. To avoid get such image file paths, check
                        if image file exists.
                         */
                    file = new File(image);
                    if (file.exists()) {
                        temp.add(new Album(album, image));
                        albumSet.add(albumId);
                    }
                }

            } while (cursor.moveToPrevious());
        }
        cursor.close();
        ArrayList<Album> albums = new ArrayList<>();
        albums.clear();
        albums.addAll(temp);
        return albums;
    }

    @Override
    protected void onPostExecute(ArrayList<Album> albums) {
        if (albums != null) {
            mCallback.fetchCompleted(albums);
        } else {
            mCallback.onErrorOccured();
        }
        mContext = null;
        mCallback = null;
    }
}
