package com.blogspot.huyhungdinh.tv.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.view.navi.MainActivity;

/**
 * Created by HUNGDH on 5/8/2016.
 */
public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //ImageView iv_splash = (ImageView) findViewById(R.id.iv_splash);
        //Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotale);
        //iv_splash.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
