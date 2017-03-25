package com.blogspot.huyhungdinh.tv.view.navi;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.adapter.ListViewScheduleAdapter;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.controller.service.NotiReceiver;
import com.blogspot.huyhungdinh.tv.model.Schedule;
import com.blogspot.huyhungdinh.tv.view.channel.WatchTvActivity;

import java.util.ArrayList;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class ScheduleFragment extends Fragment {

    private DBChannelController db;
    private ListView lv_schedule;

    public ScheduleFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBChannelController(getActivity());
        lv_schedule = (ListView) view.findViewById(R.id.lv_schedule);
    }

    private void initSchedule() {
        class GetSchedule extends AsyncTask<Void, Void, Void> {

            private ArrayList<Schedule> list;
            private ListViewScheduleAdapter adapter;

            @Override
            protected Void doInBackground(Void... params) {
                list = db.getAllSchedule();
                //Log.d("myLog", "Size: " + list.size());
                adapter = new ListViewScheduleAdapter(getActivity(), list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                lv_schedule.setAdapter(adapter);
               /* lv_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialogCancel(list.get(position));
                    }
                });*/
            }
        }
        new GetSchedule().execute();
    }

    private void dialogCancel(final Schedule schedule) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question);

        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.cancel_schedule));

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        btn_cancel.setText(R.string.cancel);
        btn_ok.setText(R.string.ok);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteSchedule(schedule);
            }
        });

        dialog.show();
    }

    private void deleteSchedule(Schedule schedule) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity(), NotiReceiver.class);
        myIntent.putExtra("schedule", schedule);
        PendingIntent appIntent = PendingIntent.getBroadcast(getActivity(), schedule.getId(), myIntent, 0);
        alarmManager.cancel(appIntent);
        db.deleteSchedule(schedule);
        Toast.makeText(getActivity(), getResources().getString(R.string.canceled), Toast.LENGTH_SHORT).show();
        initSchedule();
    }

    @Override
    public void onResume() {
        super.onResume();
        initSchedule();
    }
}
