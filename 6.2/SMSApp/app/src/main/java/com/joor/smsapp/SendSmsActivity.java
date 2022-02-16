package com.joor.smsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//this activity can be used to send a SMS
public class SendSmsActivity extends AppCompatActivity {

    //send button variable
    Button buttonSendSMS;
    //edittext field variables
    EditText editTextNumber, editTextMessageBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        //initialize edittext fields
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextMessageBody = findViewById(R.id.editTextMessageBody);
        //init button and call method to create onclick listener
        buttonSendSMS = findViewById(R.id.buttonSendSMS);
        setupButtonOnClick();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            }else{
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }
    }

    //setup onclick method for button that will send a message
    private void setupButtonOnClick(){
        buttonSendSMS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //get data from edittextfields
                String phoneNumber = editTextNumber.getText().toString().trim();
                String message = editTextMessageBody.getText().toString().trim();

                if(phoneNumber.isEmpty() || message.isEmpty()){
                    Toast.makeText(SendSmsActivity.this, "Please fill every text field", Toast.LENGTH_SHORT).show();
                }else {
                    //if number and message is entered, try to send SMS
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                        Toast.makeText(SendSmsActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SendSmsActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
