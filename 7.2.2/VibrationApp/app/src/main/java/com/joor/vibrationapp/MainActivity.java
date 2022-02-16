package com.joor.vibrationapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This class provides functionality for testing vibration on an android device
 * @author: Jonas Örnfelt
 */
public class MainActivity extends AppCompatActivity {

    //button variables
    Button buttonTestVibration;
    Button buttonTestLongVibration;
    //vibrator variables
    Vibrator vibrator;
    final long[] pattern = {2000, 1000};
    boolean vibrationActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize buttons from layout
        buttonTestVibration = findViewById(R.id.buttonTestVibration);
        buttonTestLongVibration = findViewById(R.id.buttonTestLongVibration);
        //init vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    //this method is called when user clicks button: buttonTestVibration
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickTestVibration(View view){
        //create short vibration
        //vibrator.vibrate(pattern, -1);
        System.out.println("has vibrator: " + vibrator.hasVibrator());
        vibrator.vibrate(500);
    }

    //this method is called when user clicks button: buttonTestLongVibration
    public void onClickTestLongVibration(View view){
        //if vibration isn't active
        if(!vibrationActive) {
            //create long vibration
            vibrator.vibrate(pattern, 0); //0 to repeat indefinitely, -1 to not repeat at all
            Toast.makeText(this, "Long vibration started", Toast.LENGTH_SHORT).show();
            buttonTestLongVibration.setText("Stop vibration");
            vibrationActive = true;
        }else{
            //cancel vibration
            vibrator.cancel();
            buttonTestLongVibration.setText("Test Long Vibration");
            Toast.makeText(this, "long vibration stopped", Toast.LENGTH_SHORT).show();
            vibrationActive = false;
        }
    }
}
