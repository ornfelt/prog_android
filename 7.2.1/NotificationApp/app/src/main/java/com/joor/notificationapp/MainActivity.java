package com.joor.notificationapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * This class allows testing notifications on an Android device
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //variables for notification buttons
    Button buttonShowNotification;
    Button buttonCancelNotification;
    //id for notification
    final int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize buttons
        buttonShowNotification = findViewById(R.id.buttonShowNotification);
        buttonCancelNotification = findViewById(R.id.buttonCancelNotification);
    }

    //this onClick method is called when user clicks on button: buttonShowNotification (API > 15 required)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onClickShowNotification(View view){

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(MainActivity.this, MyNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        //variables used in notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String title = "My Test Notification";
        String text = "My Notification text";
        Notification notification = null;
        //some API versions require a channel to be created
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "my_channel_01";
            String channelName = "my_channel_name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel myChannel = new NotificationChannel(channelId, channelName, importance);
            myChannel.enableLights(true);
            notificationManager.createNotificationChannel(myChannel);

            notification = new Notification.Builder(MainActivity.this)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.android_notification)
                    .addAction(R.drawable.android_notification, "Open", pendingIntent)
                    .setChannelId(channelId)
                    .addAction(0, "Remind", pendingIntent).build();
        }else{
            notification = new Notification.Builder(MainActivity.this)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.android_notification)
                    .addAction(R.drawable.android_notification, "Open", pendingIntent)
                    .addAction(0, "Remind", pendingIntent).build();
        }
        //set notification with id
        notificationManager.notify(notificationId, notification);
    }

    //this onClick method is called when user clicks on button: buttonCancelNotification
    public void onClickCancelNotification(View view){
        if(Context.NOTIFICATION_SERVICE != null){
            String notificationService = Context.NOTIFICATION_SERVICE;
            NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(notificationService);
            notificationManager.cancel(notificationId);
        }
    }
}
