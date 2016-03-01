package com.example.ericbrenner.instagramapiapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ericbrenner.instagramapiapp.pojos.Image;
import com.example.ericbrenner.instagramapiapp.pojos.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ericbrenner on 2/25/16.
 */
public class ImageAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<SearchResult> mResults;

    public ImageAdapter(Context context, ArrayList<SearchResult> results) {
        mContext = context;
        mResults = results;
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Image thumbnail = mResults.get(position).images.thumbnail;
        int factor = 2;
        int w = thumbnail.width * factor;
        int h = thumbnail.height * factor;
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(w, h));
            //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(mResults.get(position).images.thumbnail.url).into(imageView);
        return imageView;
    }
}
