package com.swetabh.imagepicker.multipleimageselection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.swetabh.imagepicker.R;
import com.swetabh.imagepicker.multipleimageselection.constants.LibConstant;
import com.swetabh.imagepicker.multipleimageselection.fragments.albumview.AlbumPresenterImp;
import com.swetabh.imagepicker.multipleimageselection.fragments.albumview.AlbumViewFragment;
import com.swetabh.imagepicker.multipleimageselection.fragments.imageselection.ImageSelectionFragment;
import com.swetabh.imagepicker.multipleimageselection.fragments.imageselection.ImageSelectionPresenterImp;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BaseView;

public class MultipleImagePickerActivity extends PickerBaseActivity
        implements MultipleImagePickerContract.ActivityCommunicator {

    private MultipleImagePickerContract.AlbumPresenter mAlbumPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        LibConstant.limit = intent.getIntExtra(LibConstant.INTENT_EXTRA_LIMIT, LibConstant.DEFAULT_LIMIT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    protected void openDefaultFragment() {
        mAlbumPresenter = new AlbumPresenterImp();
        attachFragment(AlbumViewFragment.newInstance(), mAlbumPresenter, MultipleImagePickerContract.FRAGMENT_ALBUM_VIEW);
    }

    @Override
    protected View getSnackbarParentView() {
        return findViewById(R.id.parent_view_album);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_album_view;
    }

    @Override
    protected void attachCommunicator() {
        if (mCurrentFragment != null && mCurrentFragment instanceof BaseView)
            ((BaseView) mCurrentFragment).setCommunicator(this);
    }

    @Override
    public void updateTitle(String title) {
        super.updateTitle(title);
    }

    @Override
    public Fragment getCurrentFragment() {
        return null;
    }

    @Override
    protected void permissionGranted() {
        mAlbumPresenter.permissionGranted();
    }

    @Override
    protected void hideViews() {
        mAlbumPresenter.permissionDenied();
    }

    @Override
    public void openImageSelectionFragment(String albumName) {
        attachFragment(ImageSelectionFragment.newInstance(albumName), new ImageSelectionPresenterImp(), MultipleImagePickerContract.FRAGMENT_IMAGE_SELECTION);
    }
}
