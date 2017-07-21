package com.swetabh.imagepicker.multipleimageselection.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.swetabh.imagepicker.R;
import com.swetabh.imagepicker.multipleimageselection.constants.LibConstant;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BasePresenter;
import com.swetabh.imagepicker.multipleimageselection.presenterandview.BaseView;

import java.util.Stack;

/**
 * Created by swets on 20-07-2017.
 */

public abstract class PickerBaseActivity extends AppCompatActivity {

    private final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public Stack<BasePresenter> mPresenterStack = new Stack<>();
    public Fragment mCurrentFragment;
    public FragmentManager mFragmentManager = getSupportFragmentManager();
    public BasePresenter mPresenter;
    private View mSnackBarParentView;
    private ActionBar actionBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        mSnackBarParentView = getSnackbarParentView();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.select_photo_album);
        }
        mPresenterStack = new Stack<>();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        openDefaultFragment();
    }

    protected abstract void openDefaultFragment();

    protected abstract View getSnackbarParentView();

    protected abstract int getContentView();

    protected abstract void attachCommunicator();

    protected void updateTitle(String title) {
        actionBar.setTitle(title);
    }


    protected void attachFragment(Fragment fragment, BasePresenter presenter, String tag) {
        mCurrentFragment = fragment;
        mPresenter = presenter;
        attachCommunicator();
        attachView();
        mPresenterStack.push(mPresenter);
        mFragmentManager.beginTransaction()
                .replace(R.id.container, mCurrentFragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private void attachView() {
        if (mPresenter != null)
            mPresenter.attachView((BaseView) mCurrentFragment);
    }

    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissions, LibConstant.PERMISSION_REQ_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LibConstant.PERMISSION_REQ_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted();
            } else {
                permissionDenied();
            }
        }
    }

    private void permissionDenied() {
        hideViews();
        requestPermission();
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
            showRequestPermissionRationale();
        } else {
            showAppPermissionSetting();
        }
    }

    private void showAppPermissionSetting() {
        Snackbar snackbar = Snackbar
                .make(mSnackBarParentView, R.string.permission_setting_info, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.open_settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.fromParts(
                                getString(R.string.permission_package),
                                PickerBaseActivity.this.getPackageName(),
                                null);

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.setData(uri);
                        startActivityForResult(intent, LibConstant.PERMISSION_REQ_CODE);
                    }
                });
        snackbar.show();
    }

    private void showRequestPermissionRationale() {
        Snackbar snackbar = Snackbar
                .make(mSnackBarParentView, R.string.permission_rationale_info, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(
                                PickerBaseActivity.this,
                                permissions,
                                LibConstant.PERMISSION_REQ_CODE);
                    }
                });
        snackbar.show();
    }

    protected void hideViews() {
    }

    protected void permissionGranted() {

    }


    @Override
    public void onBackPressed() {
        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0 || backStackEntryCount == 1) {
            finish();
        } else {
            String fragmentTag = mFragmentManager.getBackStackEntryAt(backStackEntryCount - 2).getName();
            mCurrentFragment = mFragmentManager.findFragmentByTag(fragmentTag);
            if (!mPresenterStack.isEmpty()) {
                mPresenterStack.pop();
                mPresenter = mPresenterStack.peek();
                attachCommunicator();
                attachView();
            }
            super.onBackPressed();
        }
    }
}
