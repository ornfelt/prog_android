package com.joor.notificationapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

//this class inherits from the Activity class and is used for receiving the notification
public class MyNotificationReceiver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textview with notification text
        TextView textView = new TextView(this);
        textView.setText("Notification text");
        setContentView(textView);
    }
}
