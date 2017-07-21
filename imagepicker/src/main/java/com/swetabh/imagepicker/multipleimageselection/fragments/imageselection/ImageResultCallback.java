package com.swetabh.imagepicker.multipleimageselection.fragments.imageselection;

import com.swetabh.imagepicker.multipleimageselection.models.Image;

import java.util.ArrayList;

/**
 * Created by swets on 21-07-2017.
 */

public interface ImageResultCallback {
    void fetchStarted();

    void errorOccured();


    void fetchCompleted(ArrayList<Image> images);
}
