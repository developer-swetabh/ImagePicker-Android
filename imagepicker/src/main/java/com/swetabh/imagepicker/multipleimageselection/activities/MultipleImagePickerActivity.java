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

import java.util.ArrayList;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LibConstant.PERMISSION_REQ_CODE) {
            checkPermission();
        }
    }

    @Override
    public void sendBackResult(ArrayList<String> selectedImagesPath) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(LibConstant.INTENT_EXTRA_IMAGES, selectedImagesPath);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void checkStoragePermission() {
        checkPermission();
    }
}
