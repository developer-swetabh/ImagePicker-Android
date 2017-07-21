package com.swetabh.imagepicker.multipleimageselection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by swets on 07/20/2017.
 */

public abstract class CustomGenericAdapter<T> extends BaseAdapter {
    protected ArrayList<T> arrayList;
    protected Context context;
    protected LayoutInflater layoutInflater;

    protected int size;

    public CustomGenericAdapter(Context context, ArrayList<T> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    public T getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setLayoutParams(int size) {
        this.size = size;
    }

    public void releaseResources() {
        arrayList = null;
        context = null;
    }
}
