package com.example.ericbrenner.instagramapiapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ericbrenner on 2/24/16.
 */
public class SearchResult implements Parcelable {
    public String id;
    public String type;
    public ImageSet images;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeParcelable(images, flags);
    }

    public static final Parcelable.Creator<SearchResult> CREATOR
            = new Parcelable.Creator<SearchResult>() {
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    private SearchResult(Parcel in) {
        id = in.readString();
        type = in.readString();
        images = in.readParcelable(ImageSet.class.getClassLoader());
    }
}
