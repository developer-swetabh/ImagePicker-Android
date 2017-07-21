package com.swetabh.imagepicker.multipleimageselection.fragments.imageselection;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swetabh.imagepicker.R;
import com.swetabh.imagepicker.multipleimageselection.activities.MultipleImagePickerContract;
import com.swetabh.imagepicker.multipleimageselection.adapters.CustomImageSelectAdapter;
import com.swetabh.imagepicker.multipleimageselection.constants.LibConstant;
import com.swetabh.imagepicker.multipleimageselection.models.Image;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSelectionFragment extends Fragment implements MultipleImagePickerContract.ImageSelectionView {


    private MultipleImagePickerContract.ImageSelectionPresenter mPresenter;
    private MultipleImagePickerContract.ActivityCommunicator mCommunicator;
    private ArrayList<Image> images;
    private String album;

    private TextView errorDisplay;

    private ProgressBar progressBar;
    private GridView gridView;
    private CustomImageSelectAdapter adapter;
    private Context mContext;
    private ActionMode actionMode;
    private ActionMode.Callback callback;
    private int countSelected;


    public ImageSelectionFragment() {
        // Required empty public constructor
    }

    public static ImageSelectionFragment newInstance(String albumName) {
        ImageSelectionFragment fragment = new ImageSelectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LibConstant.INTENT_EXTRA_ALBUM, albumName);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_image_selection, container, false);
        album = getArguments().getString(LibConstant.INTENT_EXTRA_ALBUM);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        errorDisplay = (TextView) view.findViewById(R.id.text_view_error);
        errorDisplay.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_image_select);
        gridView = (GridView) view.findViewById(R.id.grid_view_image_select);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
            mPresenter.loadImages(mContext, album, images);
        }
    }


    @Override
    public void setPresenter(MultipleImagePickerContract.ImageSelectionPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setCommunicator(MultipleImagePickerContract.ActivityCommunicator communicator) {
        mCommunicator = communicator;
    }

    @Override
    public void updateActionBar() {
        mCommunicator.updateTitle(getString(R.string.tap_to_select));
    }

    @Override
    public void fetchStarted() {
        progressBar.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void someErrorOccured() {
        progressBar.setVisibility(View.INVISIBLE);
        errorDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void fetchCompleted(ArrayList<Image> images) {
        if (adapter == null) {
            adapter = new CustomImageSelectAdapter(mContext, images);
            gridView.setAdapter(adapter);
        } else {
            adapter.addImages(images);
            adapter.notifyDataSetChanged();
        }
        progressBar.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
        orientationBasedUI(getResources().getConfiguration().orientation);
    }

    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        if (adapter != null) {
            int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 3 : metrics.widthPixels / 5;
            adapter.setLayoutParams(size);
        }
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5);
    }
}
