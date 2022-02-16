package com.joor.videoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

//In this activity we can play the recorded video in a VideoView
public class PlayVideoActivity extends AppCompatActivity {

    //variable for videoview
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        //init videoview
        videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse(getIntent().getExtras().getString("videoUri"));
        //show video
        videoView.setVideoURI(videoUri);
        videoView.start();
    }
}
