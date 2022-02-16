package com.joor.youtubeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This app contains three buttons which leads to different youtube videos
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //variables for buttons
    Button buttonFirstVideo, buttonSecondVideo, buttonThirdVideo;
    //variables for youtube videos - random cat videos that'll make you smile :)
    String firstVideo = "https://www.youtube.com/watch?v=tpiyEe_CqB4";
    String secondVideo = "https://www.youtube.com/watch?v=ByH9LuSILxU";
    String thirdVideo = "https://www.youtube.com/watch?v=XyNlqQId-nk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //open youtube video via YoutubeViewActivity
    private void openYoutubeVideo(String link){
        Intent intent = new Intent(this, YoutubeViewActivity.class);
        intent.putExtra("youtube_link", link);
        this.startActivity(intent);
    }

    //this method is called if user clicks on the first button
    public void onClickFirstVideo(View view){
        openYoutubeVideo(firstVideo);
    }

    //this method is called if user clicks on the second button
    public void onClickSecondVideo(View view){
        openYoutubeVideo(secondVideo);
    }

    //this method is called if user clicks on the third button
    public void onClickThirdVideo(View view){
        openYoutubeVideo(thirdVideo);
    }
}
