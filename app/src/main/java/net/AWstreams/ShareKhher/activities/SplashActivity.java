package net.AWstreams.ShareKhher.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import net.AWstreams.ShareKhher.R;
import net.AWstreams.ShareKhher.utils.Constants;

public class SplashActivity extends AppCompatActivity {
    ImageView ivLogoAnimated, ivShareKherAnimated;
    Animation animation;
    boolean isLoggedin;
    private static int SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        initViews();
        isLoggedin = prefs.getBoolean(Constants.isLoggedin, false);
        if (isLoggedin) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(SplashActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);


        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);

        }


    }

    private void initViews() {

        ivLogoAnimated = (ImageView) findViewById(R.id.splash_logo_iv);
        ivShareKherAnimated = (ImageView) findViewById(R.id.splash_text_iv);
        animation = AnimationUtils.loadAnimation(this, R.anim.splash_fadein);
        ivLogoAnimated.startAnimation(animation);
        ivShareKherAnimated.startAnimation(animation);


    }
}
