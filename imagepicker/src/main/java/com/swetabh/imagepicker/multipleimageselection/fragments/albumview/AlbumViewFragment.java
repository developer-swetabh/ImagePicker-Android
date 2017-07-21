package com.swetabh.imagepicker.multipleimageselection.fragments.albumview;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.swetabh.imagepicker.multipleimageselection.adapters.CustomAlbumSelectAdapter;
import com.swetabh.imagepicker.multipleimageselection.models.Album;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumViewFragment extends Fragment implements MultipleImagePickerContract.AlbumView {

    private MultipleImagePickerContract.AlbumPresenter mPresenter;
    private MultipleImagePickerContract.ActivityCommunicator mCommunicator;
    private ArrayList<Album> mAlbumsList;
    private GridView gridView;
    private TextView errorDisplay;
    private ProgressBar progressBar;
    private CustomAlbumSelectAdapter adapter;
    private Context mContext;


    public AlbumViewFragment() {
        // Required empty public constructor
    }

    public static AlbumViewFragment newInstance() {
        return new AlbumViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_album_view, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        errorDisplay = (TextView) view.findViewById(R.id.text_view_error);
        errorDisplay.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_album_select);
        gridView = (GridView) view.findViewById(R.id.grid_view_album_select);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCommunicator != null) {
                    mCommunicator.openImageSelectionFragment(mAlbumsList.get(position).name);
                }
               /* Intent intent = new Intent(getApplicationContext(), ImageSelectActivity.class);
                intent.putExtra(LibConstant.INTENT_EXTRA_ALBUM, albums.get(position).name);
                startActivityForResult(intent, Constants.REQUEST_CODE);*/
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
            mPresenter.loadAlbum(mContext);
        }
    }

    @Override
    public void setPresenter(MultipleImagePickerContract.AlbumPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setCommunicator(MultipleImagePickerContract.ActivityCommunicator communicator) {
        mCommunicator = communicator;
    }

    @Override
    public void updateActionBar() {
        mCommunicator.updateTitle(getString(R.string.select_photo_album));
    }

    @Override
    public void permissionGranted() {
        mPresenter.loadAlbum(mContext);
        if (adapter == null) {
            fetchStarted();
        }
    }

    private void fetchStarted() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void permissionDenied() {
        progressBar.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onErrorOccured() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                errorDisplay.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void loadAlbums(final ArrayList<Album> albums) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new CustomAlbumSelectAdapter(mContext, albums);
                gridView.setAdapter(adapter);
                mAlbumsList = albums;
                progressBar.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                orientationBasedUI(getResources().getConfiguration().orientation);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);
    }

    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        if (adapter != null) {
            int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 2 : metrics.widthPixels / 4;
            adapter.setLayoutParams(size);
        }
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.releaseResources();
        }
    }
}