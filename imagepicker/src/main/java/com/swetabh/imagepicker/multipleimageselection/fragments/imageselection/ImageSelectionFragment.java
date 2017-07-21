package com.swetabh.imagepicker.multipleimageselection.fragments.imageselection;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swetabh.imagepicker.R;
import com.swetabh.imagepicker.multipleimageselection.activities.MultipleImagePickerActivity;
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
    private ActionModeCallback actionModeCallback;

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
        actionModeCallback = new ActionModeCallback();
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
                if (actionMode == null) {
                    actionMode = ((MultipleImagePickerActivity) mContext).startSupportActionMode(actionModeCallback);
                }
                toggleSelection(position);
                actionMode.setTitle(String.valueOf(countSelected));

                if (countSelected == 0) {
                    actionMode.finish();
                }
            }
        });
    }

    private void toggleSelection(int position) {
        if (!images.get(position).isSelected && countSelected >= LibConstant.limit) {
            Toast.makeText(
                    mContext,
                    String.format(getString(R.string.limit_exceeded), LibConstant.limit),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        images.get(position).isSelected = !images.get(position).isSelected;
        if (images.get(position).isSelected) {
            countSelected++;
        } else {
            countSelected--;
        }
        adapter.notifyDataSetChanged();
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
    public void fetchCompleted(ArrayList<Image> images, int tempCountSelected) {
        if (adapter == null) {
            this.images = images;
            adapter = new CustomImageSelectAdapter(mContext, this.images);
            gridView.setAdapter(adapter);
        } else {
            adapter.addImages(this.images);
            adapter.notifyDataSetChanged();
            if (actionMode != null) {
                countSelected = tempCountSelected;
                actionMode.setTitle(String.valueOf(countSelected));
            }
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

    private void sendIntent() {
        mCommunicator.sendBackResult(getSelectedImagesPath());
    }

    private ArrayList<String> getSelectedImagesPath() {
        ArrayList<String> selectedImages = new ArrayList<>();
        for (int i = 0, l = images.size(); i < l; i++) {
            if (images.get(i).isSelected) {
                selectedImages.add(images.get(i).path);
            }
        }
        return selectedImages;
    }

    private void deselectAll() {
        for (int i = 0, l = images.size(); i < l; i++) {
            images.get(i).isSelected = false;
        }
        countSelected = 0;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        images = null;
        if (adapter != null) {
            adapter.releaseResources();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            countSelected = 0;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.menu_item_add_image) {
                sendIntent();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (countSelected > 0) {
                deselectAll();
            }
            actionMode = null;
        }
    }
}
