package com.blogspot.huyhungdinh.tv.view.navi.livetv;

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

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.adapter.GridViewChannelAdapter;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.model.Channel;
import com.blogspot.huyhungdinh.tv.model.ExGridView;
import com.blogspot.huyhungdinh.tv.view.channel.WatchTvActivity;

import java.util.ArrayList;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class LiveTVFragment extends Fragment {

    private DBChannelController db;
    private ExGridView gv_cat0, gv_cat1, gv_cat2, gv_cat3, gv_cat4;

    public LiveTVFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_livetv, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gv_cat0 = (ExGridView) view.findViewById(R.id.gv_cat0);
        gv_cat0.setExpanded(true);
        gv_cat1 = (ExGridView) view.findViewById(R.id.gv_cat1);
        gv_cat1.setExpanded(true);
        gv_cat2 = (ExGridView) view.findViewById(R.id.gv_cat2);
        gv_cat2.setExpanded(true);
        gv_cat3 = (ExGridView) view.findViewById(R.id.gv_cat3);
        gv_cat3.setExpanded(true);
        gv_cat4 = (ExGridView) view.findViewById(R.id.gv_cat4);
        gv_cat4.setExpanded(true);

        db = new DBChannelController(getActivity());

        initCat(gv_cat0, 0);
        initCat(gv_cat1, 1);
        initCat(gv_cat2, 2);
        initCat(gv_cat3, 3);
        initCat(gv_cat4, 4);
    }

    private void initCat(final ExGridView gv_cat, final int idCat) {
        class GetChannel extends AsyncTask<Void, Void, Void> {

            private ArrayList<Channel> list;
            private GridViewChannelAdapter adapter;

            @Override
            protected Void doInBackground(Void... params) {
                list = db.getCatalog(idCat);
                //Log.d("myLog", "Size: " + list.size());
                adapter = new GridViewChannelAdapter(getActivity(), list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                gv_cat.setAdapter(adapter);
                gv_cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading));
            dialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            int id = params[0];
            Intent intent = new Intent(getActivity().getApplicationContext(), WatchTvActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            intent.putExtras(bundle);
            startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) dialog.dismiss();
        }
    }
}
