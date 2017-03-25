package com.blogspot.huyhungdinh.tv.view.navi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.adapter.GridViewChannelAdapter;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.model.Channel;
import com.blogspot.huyhungdinh.tv.view.channel.WatchTvActivity;

import java.util.ArrayList;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class FavoriteFragment extends Fragment {

    private DBChannelController db;
    private GridView gv_favorite;

    public FavoriteFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBChannelController(getActivity());

        gv_favorite = (GridView) view.findViewById(R.id.gv_favorite);
    }

    @Override
    public void onResume() {
        super.onResume();
        initFavorite();
    }

    private void initFavorite() {
        class GetChannel extends AsyncTask<Void, Void, Void> {

            private ArrayList<Channel> list;
            private GridViewChannelAdapter adapter;

            @Override
            protected Void doInBackground(Void... params) {
                list = db.getFavorite();
                //Log.d("myLog", "Size: " + list.size());
                adapter = new GridViewChannelAdapter(getActivity(), list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                gv_favorite.setAdapter(adapter);
                gv_favorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        new CallChannel().execute(list.get(position).getId());
                    }
                });
            }
        }
        new GetChannel().execute();
    }

    private class CallChannel extends AsyncTask<Integer, Void, Void> {

        private ProgressDialog dialog;
        private Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading));
            dialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            intent = new Intent(getActivity().getApplicationContext(), WatchTvActivity.class);
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
}
