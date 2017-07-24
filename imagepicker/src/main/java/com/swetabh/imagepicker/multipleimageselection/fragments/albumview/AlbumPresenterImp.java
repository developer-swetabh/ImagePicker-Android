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
        new AlbumLoadAsync(this, context).execute();
    }

    @Override
    public void onErrorOccured() {
        if (mView != null) {
            mView.onErrorOccured();
        }
    }

    @Override
    public void fetchCompleted(ArrayList<Album> albums) {
        if (mView != null) {
            mView.loadAlbums(albums);
        }
    }
}
