package com.example.applicationmarsh.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.applicationmarsh.R;

public class SplashActivity extends Activity {

    //Time of the splash screen
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //Create intent
                Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);

                //Start login activity
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();

                //Create fade animation
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
