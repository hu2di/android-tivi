package com.blogspot.huyhungdinh.tv.view.channel;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.controller.service.NotiReceiver;
import com.blogspot.huyhungdinh.tv.model.Channel;
import com.blogspot.huyhungdinh.tv.model.Schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class WatchTvActivity extends AppCompatActivity /*implements OnPreparedListener*/ {

    private String urlStream;
    private Channel channel;
    private DBChannelController db;
    //private EMVideoView emVideoView;
    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchtv);

       /* if (!LibsChecker.checkVitamioLibs(this))
            return;*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this, R.string.swipe_refresh, Toast.LENGTH_LONG).show();

        db = new DBChannelController(this);
        int id;
        try {
            Bundle bundle = getIntent().getExtras();
            id = bundle.getInt("id");
            channel = db.getChannel(id);
            actionBar.setTitle(channel.getName());
            urlStream = channel.getLinkStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                playDefault();
                mVideoView.start();
            }
        });
    }

    private void playDefault() {
        mVideoView = (VideoView) findViewById(R.id.vv_watch_tv);
        mVideoView.setVideoURI(Uri.parse(urlStream));
        MediaController mediaControllerView = new MediaController(this);
        mediaControllerView.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaControllerView);
        //mVideoView.start();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        playDefault();
        mVideoView.start();
    }

    /*private void playVitamio() {
        mVideoView = (VideoView) findViewById(R.id.vv_watch_tv);
        mVideoView.setVideoURI(Uri.parse("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"));
        //mVideoView.setVideoPath(pathToFileOrUrl);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // optional need Vitamio 4.0
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
    }*/

    /*private void playExoMedia() {
        emVideoView = (EMVideoView) findViewById(R.id.vv_watch_tv);
        emVideoView.setOnPreparedListener(this);
        //emVideoView.setVideoURI(Uri.parse("https://archive.org/download/Popeye_forPresident/Popeye_forPresident_512kb.mp4"));
        emVideoView.setVideoURI(Uri.parse(urlStream));
        Log.d("myLog", urlStream);
    }

    @Override
    public void onPrepared() {
        emVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Pause Video Playback
        emVideoView.pause();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_watch_tv, menu);
        MenuItem itemLike = menu.findItem(R.id.action_like);
        if (channel.isFavorite()) {
            itemLike.setIcon(R.drawable.ic_like);
        } else {
            itemLike.setIcon(R.drawable.ic_unlike);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_like:
                Log.d("myLog", "Click Menu");
                if (channel.isFavorite()) {
                    dialogFavorite(0, item);
                } else {
                    dialogFavorite(1, item);
                }
                return true;
            case R.id.action_schedule:
                dialogSchedule();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogSchedule() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_schedule, null);
        final DatePicker dp = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker tp = (TimePicker) dialogView.findViewById(R.id.time_picker);
        tp.setIs24HourView(true);
        dialog.setView(dialogView);
        dialog.setTitle(R.string.schedule);
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, dp.getYear());
                calendar.set(Calendar.MONTH, dp.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                calendar.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
                calendar.set(Calendar.MINUTE, tp.getCurrentMinute());

                dialogDescription(calendar);
            }
        });
        dialog.create();
        dialog.show();
    }

    private void dialogDescription(final Calendar calendar) {
        //Toast.makeText(WatchTvActivity.this, "" + calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_description, null);
        final EditText et_description = (EditText)dialogView.findViewById(R.id.et_description);
        dialog.setView(dialogView);
        dialog.setTitle(R.string.schedule);
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton(R.string.schedule, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = (int) System.currentTimeMillis();
                int idChannel = channel.getId();
                String content = et_description.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                String time = sdf.format(calendar.getTime());
                Schedule schedule = new Schedule(id, idChannel, content, time);
                db.addSchedule(schedule);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(WatchTvActivity.this, NotiReceiver.class);
                myIntent.putExtra("schedule", schedule);
                PendingIntent appIntent = PendingIntent.getBroadcast(WatchTvActivity.this, id, myIntent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), appIntent);

                Toast.makeText(WatchTvActivity.this, getResources().getString(R.string.schedule_success), Toast.LENGTH_SHORT).show();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void dialogFavorite(final int status, final MenuItem item) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question);

        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setTextSize(17f);
        tv_title.setPadding(10, 10, 10, 10);
        if (status == 0) {
            tv_title.setText("Xóa " + channel.getName() + " khỏi danh sách yêu thích?");
        } else {
            tv_title.setText("Thêm " + channel.getName() + " vào danh sách yêu thích?");
        }

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
                if (status == 0) {
                    removeFromFavorite(item);
                } else {
                    addToFavorite(item);
                }
            }
        });

        dialog.show();
    }

    private void removeFromFavorite(MenuItem item) {
        new Remove().execute(item);
    }

    private class Remove extends AsyncTask<MenuItem, Void, Void> {

        private int result;
        private MenuItem item;

        @Override
        protected Void doInBackground(MenuItem... params) {
            item = params[0];
            channel.setFavorite(false);
            result = db.Update(channel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (result > -1) {
                Toast.makeText(WatchTvActivity.this, R.string.removeFromFavorite, Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_unlike);
            } else {
                Toast.makeText(WatchTvActivity.this, R.string.not_success, Toast.LENGTH_SHORT).show();
                channel.setFavorite(true);
            }
        }
    }

    private void addToFavorite(MenuItem item) {
        new Add().execute(item);
    }

    private class Add extends AsyncTask<MenuItem, Void, Void> {

        private int result;
        private MenuItem item;

        @Override
        protected Void doInBackground(MenuItem... params) {
            item = params[0];
            channel.setFavorite(true);
            result = db.Update(channel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (result > -1) {
                Toast.makeText(WatchTvActivity.this, R.string.addToFavorite, Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_like);
            } else {
                Toast.makeText(WatchTvActivity.this, R.string.not_success, Toast.LENGTH_SHORT).show();
                channel.setFavorite(false);
            }
        }
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_watchtv_land);
        } else {
            setContentView(R.layout.activity_watchtv);
        }
    }*/
}
