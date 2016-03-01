package com.example.ericbrenner.instagramapiapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ericbrenner on 2/24/16.
 */
public class ImageSet implements Parcelable {
    public Image low_resolution;
    public Image thumbnail;
    public Image standard_resolution;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(low_resolution,flags);
        dest.writeParcelable(thumbnail, flags);
        dest.writeParcelable(standard_resolution, flags);
    }

    public static final Parcelable.Creator<ImageSet> CREATOR
            = new Parcelable.Creator<ImageSet>() {
        public ImageSet createFromParcel(Parcel in) {
            return new ImageSet(in);
        }

        public ImageSet[] newArray(int size) {
            return new ImageSet[size];
        }
    };

    private ImageSet(Parcel in) {
        low_resolution = in.readParcelable(Image.class.getClassLoader());
        thumbnail = in.readParcelable(Image.class.getClassLoader());
        standard_resolution = in.readParcelable(Image.class.getClassLoader());
    }
}
