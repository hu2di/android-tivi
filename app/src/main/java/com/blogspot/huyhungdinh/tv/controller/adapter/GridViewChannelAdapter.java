package com.blogspot.huyhungdinh.tv.controller.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.model.Channel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class GridViewChannelAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Channel> list;
    private LayoutInflater layoutInflater;

    public GridViewChannelAdapter(Context c, ArrayList<Channel> list) {
        mContext = c;
        this.list = list;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = layoutInflater.inflate(R.layout.gridview_channel_layout, null);
        }

        ImageView imageView = (ImageView) listItem.findViewById(R.id.iv_channel);
        try {
            // get input stream
            InputStream ims = mContext.getAssets().open("img/" + list.get(position).getLinkImage());
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imageView.setImageDrawable(d);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return listItem;
    }
}
