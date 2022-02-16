package com.joor.youtubeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

//this class opens the youtube video sent by intent
public class YoutubeViewActivity extends AppCompatActivity {

    //WebView variable
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_view);

        //open youtube link in webview
        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String youtubeLink = intent.getStringExtra("youtube_link");
        webView.loadUrl(youtubeLink);
    }

    //this method runs if user tries to go back
    @Override
    public void onBackPressed() {
        //goBackToMainActivity();
        if (webView.canGoBack()) {
            //go back
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    //go back to main activity via intent
    private void goBackToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }
}
