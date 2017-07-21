package com.swetabh.imagepickerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swetabh.imagepicker.ImagePicker;
import com.swetabh.imagepicker.multipleimageselection.constants.LibConstant;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImagePicker mPicker;
    private ImageView mImageView;
    private TextView mImagePathTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Passing activity reference
        mPicker = new ImagePicker(this, R.string.provider_authorities);
        mImageView = (ImageView) findViewById(R.id.img_selected_image);
        mImagePathTv = (TextView) findViewById(R.id.txt_imagePath);
        findViewById(R.id.btn_gallery).setOnClickListener(this);
        findViewById(R.id.btn_multiple).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery:
                mPicker.pickImageFromGallery();
                break;
            case R.id.btn_multiple:
                mPicker.pickMultipleImagesFromGallery(3);
                break;

            case R.id.btn_camera:
                mPicker.pickImageFromCamera();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == LibConstant.MULTIPLE_SELECT_REQ_CODE) {
                ArrayList<String> imagesPath = data.getStringArrayListExtra(LibConstant.INTENT_EXTRA_IMAGES);
                for (String imagePath : imagesPath) {
                    Log.d("<--ImagesPath-->", imagePath);
                }
            } else {
                String imagePath = mPicker.onActivityResult(requestCode, resultCode, data);
                mImagePathTv.setText(imagePath);
                Glide.with(this).load(new File(imagePath)).into(mImageView);
            }
        }
    }
}
