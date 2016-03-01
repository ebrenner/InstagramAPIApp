package com.example.ericbrenner.instagramapiapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ericbrenner on 2/24/16.
 */
public class Image implements Parcelable {
    public String url;
    public int width;
    public int height;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    public static final Parcelable.Creator<Image> CREATOR
            = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    private Image(Parcel in) {
        url = in.readString();
        width = in.readInt();
        height = in.readInt();
    }
}
