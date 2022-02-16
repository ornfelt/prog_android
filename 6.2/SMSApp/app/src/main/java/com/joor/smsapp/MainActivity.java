package com.joor.smsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app can be used to receive and send SMS
 * @author: Jonas Örnfelt
 */
public class MainActivity extends AppCompatActivity {

    //button variable
    Button buttonSendMessage;
    //request int for receive sms
    private static final int REQUEST_RECEIVE_SMS = 0;

    //in this main activity onCreate we will ask for required permissions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize button
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        //add onClick listener method to button
        setupButtonListener();

        //check for Receive sms permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        != PackageManager.PERMISSION_GRANTED){
            //check if user has denied permission
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)){
                Toast.makeText(this, "Permission is required for using this app.\n" +
                        "Please enable in app settings", Toast.LENGTH_SHORT).show();
            }else{
                //ask for required permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                REQUEST_RECEIVE_SMS);
            }
        }
/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){
                if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED){
                }else{
                    requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                }
            }else{
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, 1);
            }
        }

 */
    }

    //this method is called when user has accepted/denied permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_RECEIVE_SMS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setupButtonListener(){
        buttonSendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, SendSmsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

}
