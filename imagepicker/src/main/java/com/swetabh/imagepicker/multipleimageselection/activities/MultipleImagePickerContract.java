package com.swetabh.imagepicker.multipleimageselection.activities;

import android.content.Context;

import com.swetabh.imagepicker.multipleimageselection.models.Album;
import com.swetabh.imagepicker.multipleimageselection.models.Image;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BaseActivityCommunicator;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BasePresenter;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BaseView;

import java.util.ArrayList;

/**
 * Created by swets on 20-07-2017.
 */

public interface MultipleImagePickerContract {

    String FRAGMENT_ALBUM_VIEW = "album_fragment";
    String FRAGMENT_IMAGE_SELECTION = "image_selection_fragment";

    interface ActivityCommunicator extends BaseActivityCommunicator {

        void openImageSelectionFragment(String albumName);

        void sendBackResult(ArrayList<String> selectedImagesPath);
    }

    interface AlbumPresenter extends BasePresenter {
        void permissionGranted();

        void permissionDenied();

        void loadAlbum(Context context);
    }

    interface AlbumView extends BaseView<AlbumPresenter, ActivityCommunicator> {

        void permissionGranted();

        void permissionDenied();

        void onErrorOccured();

        void loadAlbums(ArrayList<Album> albums);
    }


    interface ImageSelectionPresenter extends BasePresenter {
        void loadImages(Context context, String album, ArrayList<Image> images);
    }

    interface ImageSelectionView extends BaseView<ImageSelectionPresenter, ActivityCommunicator> {
        void fetchStarted();

        void someErrorOccured();

        void fetchCompleted(ArrayList<Image> images, int tempCountSelected);
    }
}
