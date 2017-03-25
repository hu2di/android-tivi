package com.blogspot.huyhungdinh.tv.view.navi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.adapter.ListViewNaviAdapter;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.model.Channel;
import com.blogspot.huyhungdinh.tv.view.navi.livetv.LiveTVFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private LinearLayout left_drawer;
    private ListView navList;

    private ActionBar actionBar;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private LiveTVFragment liveTVFragment;
    private FavoriteFragment favoriteFragment;
    private ScheduleFragment scheduleFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initUI();
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();

        liveTVFragment = new LiveTVFragment();
        favoriteFragment = new FavoriteFragment();
        scheduleFragment = new ScheduleFragment();
        aboutFragment = new AboutFragment();
    }

    private void initUI() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        left_drawer = (LinearLayout) findViewById(R.id.left_drawer);

        navList = (ListView) findViewById(R.id.navList);
        ListViewNaviAdapter adapter = new ListViewNaviAdapter(this);
        navList.setAdapter(adapter);
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadSelection(position);
                drawerLayout.closeDrawer(left_drawer);
            }
        });

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadSelection(0);
    }

    private void loadSelection(int i) {
        navList.setItemChecked(i, true);
        switch (i) {
            case 0:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, liveTVFragment, "LIVE");
                fragmentTransaction.addToBackStack("LIVE");
                fragmentTransaction.commit();
                actionBar.setTitle(getResources().getString(R.string.live));
                break;
            case 1:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, favoriteFragment, "FAVORITE");
                fragmentTransaction.addToBackStack("FAVORITE");
                fragmentTransaction.commit();
                actionBar.setTitle(getResources().getString(R.string.favorite));
                break;
            case 2:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, scheduleFragment, "SCHEDULE");
                fragmentTransaction.addToBackStack("SCHEDULE");
                fragmentTransaction.commit();
                actionBar.setTitle(getResources().getString(R.string.schedule));
                break;
            case 3:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, aboutFragment, "ABOUT");
                fragmentTransaction.addToBackStack("ABOUT");
                fragmentTransaction.commit();
                actionBar.setTitle(getResources().getString(R.string.about));
                break;
            case 4:
                update();
                break;
            case 5:
                shareTextUrl();
                break;
        }
    }

    private void update() {
        new UpdateServer().execute();
    }

    private class UpdateServer extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        SharedPreferences preferences = getSharedPreferences("last_update_time", Context.MODE_PRIVATE);
        String last_update;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", getResources().getString(R.string.updating));
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String myUrl = "http://hebitaxy.esy.es/index.php?cmd=1&time=";
            String time = preferences.getString("time", "0");
            myUrl = myUrl + URLEncoder.encode(time);

            Log.d("myLog", myUrl);

            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(myUrl);
                URLConnection urlConnection = url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line + "\n");
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject json = new JSONObject(content.toString());
                last_update = json.getString("time");
                JSONArray data = json.getJSONArray("data");

                Log.d("myLog", "Data: " + data.toString());
                DBChannelController db = new DBChannelController(MainActivity.this);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonObject = data.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String link = jsonObject.getString("link");
                    db.UpdateLink(id, link);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            //Log.d("huyhundinh", "Content: " + s);
            if (last_update != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("time", last_update);
                editor.commit();
            }
            if (dialog != null) dialog.dismiss();
            Toast.makeText(MainActivity.this, getResources().getString(R.string.updated), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareTextUrl() {
        String appPackageName = this.getPackageName();
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.app_name));
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);

        startActivity(Intent.createChooser(share, getResources().getText(R.string.send_to)));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(left_drawer)) {
                    drawerLayout.closeDrawer(left_drawer);
                } else {
                    drawerLayout.openDrawer(left_drawer);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("huyhungdinh", "Query: " + query);
                searchItem.collapseActionView();
                new CallSearch().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("huyhungdinh", "New: " + newText);
                return false;
            }
        });

        return true;
    }

    private class CallSearch extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;
        private Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", getResources().getString(R.string.loading));
            dialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(String... params) {
            String query = params[0];
            intent = new Intent(getApplication().getApplicationContext(), SearchResultActivity.class);
            Bundle data = new Bundle();
            data.putString("query", query);
            intent.putExtras(data);
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(left_drawer)) {
            drawerLayout.closeDrawer(left_drawer);
        } else {
            dialogExit();
        }
    }

    private void dialogExit() {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question);

        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.exit));

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
                finish();
            }
        });

        dialog.show();
    }
}
