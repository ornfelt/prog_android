package com.joor.webviewapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    Button buttonFirstLink;
    Button buttonSecondLink;
    Button buttonThirdLink;

    String firstLink = "https://www.wikipedia.org";
    String secondLink = "https://www.outlook.com";
    String thirdLink = "https://www.gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize buttons and set onclick methods via separate methods
        buttonFirstLink = findViewById(R.id.buttonFirstLink);
        buttonSecondLink = findViewById(R.id.buttonSecondLink);
        buttonThirdLink = findViewById(R.id.buttonThirdLink);
        //init webview
        webView = (WebView) findViewById(R.id.webView);
        //setup onClick methods for buttons via general method
        setupButtonOnClick(buttonFirstLink, firstLink);
        setupButtonOnClick(buttonSecondLink, secondLink);
        setupButtonOnClick(buttonThirdLink, thirdLink);
    }

    //general method that sets a onclick method for incoming parameters (button and link)
    private void setupButtonOnClick(Button button, final String link){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to link
                webView.loadUrl(link);
            }
        });
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
