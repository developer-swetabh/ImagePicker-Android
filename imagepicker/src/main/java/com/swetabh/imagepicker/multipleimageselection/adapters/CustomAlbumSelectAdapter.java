package com.swetabh.imagepicker.multipleimageselection.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swetabh.imagepicker.R;
import com.swetabh.imagepicker.multipleimageselection.models.Album;

import java.util.ArrayList;

/**
 * Created by swets on 07/20/2017.
 */


public class CustomAlbumSelectAdapter extends CustomGenericAdapter<Album> {
    public CustomAlbumSelectAdapter(Context context, ArrayList<Album> albums) {
        super(context, albums);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_folder_item, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_album);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.txt_album);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;

        viewHolder.textView.setText(arrayList.get(position).name);
        Glide.with(context)
                .load(arrayList.get(position).cover)
                .placeholder(R.drawable.image_placeholder).centerCrop().into(viewHolder.imageView);

        return convertView;
    }

    public void addAlbums(ArrayList<Album> albums) {
        arrayList = albums;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
