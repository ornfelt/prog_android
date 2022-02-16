package com.joor.networkstatusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * This class checks if the android device is connected to internet via wifi or mobile data
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //variables for context and textview
    static Context context;
    TextView textViewNetworkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize app context and textview
        context = getApplicationContext();
        textViewNetworkInfo = findViewById(R.id.textViewNetworkInfo);

        System.out.println("hasNetworkConnection: " + hasNetworkConnection());
        //set textview to inform if device is connected to internet or not
        if(hasNetworkConnection()){
            textViewNetworkInfo.setText("You are connected to the internet");
        }else{
            textViewNetworkInfo.setText("You are not connected to the internet");
        }
    }

    //check if device is connected to wifi or via mobile data
    private boolean hasNetworkConnection() {
        //boolean variables to return
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        //setup network info
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        //return either wifi or mobile connection
        return haveConnectedWifi || haveConnectedMobile;
    }
}
