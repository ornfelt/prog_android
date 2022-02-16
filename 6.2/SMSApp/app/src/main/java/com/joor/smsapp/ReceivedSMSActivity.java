package com.joor.smsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//this activity will show new SMS via textviews
public class ReceivedSMSActivity extends AppCompatActivity {

    //graphics variables
    Button buttonSendMessage;
    TextView textViewAddress, textViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_sms);

        System.out.println("we in here");

        //initialize components
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewMessage = findViewById(R.id.textViewMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        //add onClick listener method to button
        setupButtonListener();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String address = extras.getString("MessageNumber");
            String message = extras.getString("Message");

            textViewAddress.setText("SMS from: " + address);
            textViewMessage.setText("Message: " + message);
        }
    }

    private void setupButtonListener(){
        buttonSendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ReceivedSMSActivity.this, SendSmsActivity.class);
                ReceivedSMSActivity.this.startActivity(intent);
            }
        });
    }
}
