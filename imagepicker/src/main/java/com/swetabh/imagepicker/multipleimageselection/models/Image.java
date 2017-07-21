package com.swetabh.imagepicker.multipleimageselection.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by swets on 20-07-2017.
 */

public class Image implements Parcelable {
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    public long id;
    public String name;
    public String path;
    public boolean isSelected;

    public Image(long id, String name, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
    }

    private Image(Parcel in) {
        id = in.readLong();
        name = in.readString();
        path = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
    }
}