package com.blogspot.huyhungdinh.tv.controller.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.model.Channel;
import com.blogspot.huyhungdinh.tv.model.Schedule;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by HUNGDH on 5/25/2016.
 */
public class ListViewScheduleAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<Schedule> list;
    private Context context;

    public ListViewScheduleAdapter(Context context, ArrayList<Schedule> list) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
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
            listItem = layoutInflater.inflate(R.layout.listview_schedule_layout, null);
        }

        Schedule schedule = list.get(position);
        DBChannelController db = new DBChannelController(context);
        Channel channel = db.getChannel(schedule.getIdChannel());

        ImageView iv_schedule_channel = (ImageView)listItem.findViewById(R.id.iv_schedule_channel);
        try {
            // get input stream
            InputStream ims = context.getAssets().open("img/" + channel.getLinkImage());
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            iv_schedule_channel.setImageDrawable(d);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        TextView tv_schedule_content = (TextView)listItem.findViewById(R.id.tv_schedule_content);
        TextView tv_schedule_time = (TextView)listItem.findViewById(R.id.tv_schedule_time);
        tv_schedule_content.setText(schedule.getContent());
        tv_schedule_time.setText(schedule.getTime());

        return listItem;
    }
}
