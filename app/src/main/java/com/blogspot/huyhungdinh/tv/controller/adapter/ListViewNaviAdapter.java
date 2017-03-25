package com.blogspot.huyhungdinh.tv.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.huyhungdinh.tv.R;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class ListViewNaviAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private static final int[] FIELD = {R.string.live, R.string.favorite, R.string.schedule, R.string.about, R.string.update, R.string.share};
    private static final int[] ICONS = {R.drawable.ic_live, R.drawable.ic_favorite, R.drawable.ic_schedule, R.drawable.ic_about, R.drawable.ic_update, R.drawable.ic_share};

    public ListViewNaviAdapter(Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = layoutInflater.inflate(R.layout.listview_navi_layout, null);
        }
        ImageView iv_navi = (ImageView) listItem.findViewById(R.id.iv_navi);
        TextView tv_navi = (TextView) listItem.findViewById(R.id.tv_navi);
        iv_navi.setImageResource(ICONS[position]);
        tv_navi.setText(FIELD[position]);
        return listItem;
    }
}
