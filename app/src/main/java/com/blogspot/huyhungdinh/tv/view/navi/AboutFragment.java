package com.blogspot.huyhungdinh.tv.view.navi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blogspot.huyhungdinh.tv.R;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class AboutFragment extends Fragment {

    public AboutFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_review_app = (Button) view.findViewById(R.id.btn_review_app);
        btn_review_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                Log.d("myLog", "PACKAGE: " + appPackageName);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }
}
