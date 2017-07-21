package com.swetabh.imagepicker.multipleimageselection.fragments.albumview;

import android.content.Context;

import com.swetabh.imagepicker.multipleimageselection.activities.MultipleImagePickerContract;
import com.swetabh.imagepicker.multipleimageselection.models.Album;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BasePresenterImp;

import java.util.ArrayList;

/**
 * Created by swets on 20-07-2017.
 */

public class AlbumPresenterImp extends BasePresenterImp<MultipleImagePickerContract.AlbumView>
        implements MultipleImagePickerContract.AlbumPresenter, AlbumResultCallback {

    private Thread thread;

    @Override
    public void permissionGranted() {
        if (mView != null) {
            mView.permissionGranted();
        }
    }

    @Override
    public void permissionDenied() {
        if (mView != null) {
            mView.permissionDenied();
        }
    }

    @Override
    public void loadAlbum(Context context) {
        startThread(new AlbumLoadRunnable(this,context));
    }

    private void startThread(Runnable runnable) {
        stopThread();
        thread = new Thread(runnable);
        thread.start();

    }

    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;
        }

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorOccured() {
        if(mView!=null){
            mView.onErrorOccured();
        }
    }

    @Override
    public void fetchCompleted(ArrayList<Album> albums) {
        if(mView!=null){
            mView.loadAlbums(albums);
        }
    }
}
