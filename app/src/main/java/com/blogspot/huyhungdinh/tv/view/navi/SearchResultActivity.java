package com.blogspot.huyhungdinh.tv.view.navi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.adapter.GridViewChannelAdapter;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.model.Channel;
import com.blogspot.huyhungdinh.tv.view.channel.WatchTvActivity;

import java.util.ArrayList;

/**
 * Created by HUNGDH on 5/9/2016.
 */
public class SearchResultActivity extends AppCompatActivity {

    private String searchQuery;
    private GridView gv_search;
    private DBChannelController db;
    private ArrayList<Channel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        try {
            Bundle data = getIntent().getExtras();
            searchQuery = data.getString("query");
            actionBar.setTitle(searchQuery);
            //Log.d("huyhungdinh", "Received: " + searchQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db = new DBChannelController(this);
        gv_search = (GridView)findViewById(R.id.gv_search);

        getResult(searchQuery);

        gv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new CallChannel().execute(list.get(position).getId());
            }
        });
    }

    private void getResult(String searchQuery) {
        new LoadDataFromQuery().execute(searchQuery);
    }

    private class LoadDataFromQuery extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            list = db.search(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void rs) {
            super.onPostExecute(rs);
            GridViewChannelAdapter adapter = new GridViewChannelAdapter(SearchResultActivity.this, list);
            gv_search.setAdapter(adapter);
        }
    }

    private class CallChannel extends AsyncTask<Integer, Void, Void> {

        private ProgressDialog dialog;
        private Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(SearchResultActivity.this, "", getResources().getString(R.string.loading));
            dialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            intent = new Intent(SearchResultActivity.this, WatchTvActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            intent.putExtras(bundle);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) dialog.dismiss();
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
