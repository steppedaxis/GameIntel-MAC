package com.example.gameintel.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.gameintel.R;

public class SplashScreen extends AppCompatActivity {

    private ImageView logoPic;
    private ImageView logo;
    private static int splashTimeOut=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logoPic=findViewById(R.id.logopic);
        logo=findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,GameList.class);
                startActivity(intent);
                finish();
            }
        },splashTimeOut);


        Animation splashAnime= AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        logoPic.startAnimation(splashAnime);
        logo.startAnimation(splashAnime);
    }
}
