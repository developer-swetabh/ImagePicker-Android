package com.swetabh.imagepicker.multipleimageselection.fragments.imageselection;

import android.content.Context;

import com.swetabh.imagepicker.multipleimageselection.activities.MultipleImagePickerContract;
import com.swetabh.imagepicker.multipleimageselection.models.Image;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BasePresenterImp;

import java.util.ArrayList;

/**
 * Created by swets on 20-07-2017.
 */

public class ImageSelectionPresenterImp extends BasePresenterImp<MultipleImagePickerContract.ImageSelectionView>
        implements MultipleImagePickerContract.ImageSelectionPresenter, ImageResultCallback {


    @Override
    public void loadImages(Context context, String album, ArrayList<Image> images) {
        new ImageLoadAsync(context, this, album, images).execute();
    }

    @Override
    public void fetchStarted() {
        if (mView != null) {
            mView.fetchStarted();
        }
    }

    @Override
    public void errorOccured() {
        if (mView != null) {
            mView.someErrorOccured();
        }
    }

    @Override
    public void fetchCompleted(ArrayList<Image> images, int tempCountSelected) {
        if (mView != null) {
            mView.fetchCompleted(images,tempCountSelected);
        }
    }
}
