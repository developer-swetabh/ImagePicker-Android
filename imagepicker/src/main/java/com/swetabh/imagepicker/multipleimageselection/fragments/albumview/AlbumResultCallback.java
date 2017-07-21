package com.swetabh.imagepicker.multipleimageselection.fragments.albumview;

import com.swetabh.imagepicker.multipleimageselection.models.Album;

import java.util.ArrayList;

/**
 * Created by swets on 20-07-2017.
 */

public interface AlbumResultCallback {
    void onErrorOccured();

    void fetchCompleted(ArrayList<Album> albums);
}
