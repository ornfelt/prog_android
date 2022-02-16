package com.joor.streamsocketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

/**
 * This app can be used to connect to a stream socket via server and port and chat with a server
 * @author: Jonas Örnfelt
 */
public class MainActivity extends AppCompatActivity {

    //variables for graphics
    Button buttonConnect;
    EditText editTextServer, editTextPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init graphic components
        buttonConnect = findViewById(R.id.buttonConnect);
        editTextServer = findViewById(R.id.editTextServer);
        editTextPort = findViewById(R.id.editTextPort);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get server and port from edittext fields
                String serverAddress = editTextServer.getText().toString().trim();
                String port = editTextPort.getText().toString().trim();

                //tell user to fill in all fields if any is empty
                if(serverAddress.isEmpty() || port.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill both text fields before connecting",
                            Toast.LENGTH_SHORT).show();
                }else {

                    //start server before navigating to clientactivity
                    Server server = new Server(Integer.parseInt(port));

                    //start clientactivity
                    Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                    intent.putExtra("server_address", serverAddress);
                    intent.putExtra("port_number", port);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }
}
