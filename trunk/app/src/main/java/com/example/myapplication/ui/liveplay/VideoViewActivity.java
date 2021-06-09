package com.example.myapplication.ui.liveplay;

import android.os.Bundle;

import com.example.filgthhublibrary.flighthub.CustomFlightHubManager2;
import com.example.myapplication.R;

import io.vov.vitamio.Vitamio;

public class VideoViewActivity extends VideoViewBase{
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Vitamio.isInitialized(getApplicationContext());
        setContentView(R.layout.videoview);
        initVitamio();
        String url = getIntent().getStringExtra("url");
        if (url!=null&&!"".equals(url)){

            playfunction(url);
        }

    }
}
