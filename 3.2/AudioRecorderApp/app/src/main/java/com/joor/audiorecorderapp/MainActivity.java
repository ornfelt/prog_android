package com.joor.audiorecorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * This class provides functionality for recording audio on your Android device
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //variables for graphic components and Media
    Button buttonRecord, buttonStopRecord, buttonPlay, buttonStopPlay;
    String filePath = "";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    //request permission code for microphone
    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init graphic components
        buttonRecord = findViewById(R.id.buttonRecord);
        buttonStopRecord = findViewById(R.id.buttonStopRecord);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonStopPlay = findViewById(R.id.buttonStopPlay);

        //get required permissions for app
        if(!checkPermissionFromDevice()){
            requestPermission();
        }

        //make sure we have permission...
        if(checkPermissionFromDevice()){
            //onClick method to start recording audio
            buttonRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    //filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ UUID.randomUUID().toString()+"_audio_record.3gp";

                    filePath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/new_recording";
                    prepareMediaRecorder();
                    try{

                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    }catch (IOException e) { e.printStackTrace(); }

                    /*
                    String state = Environment.getExternalStorageState();
                    if(!state.equals(Environment.MEDIA_MOUNTED)){
                        try {
                            throw new IOException("SD Card is not connected: " + state);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    //make sure directory we intend to store on exists
                    File directory = new File("my_recorded_file");
                    */

                    buttonPlay.setEnabled(false);
                    buttonStopPlay.setEnabled(false);
                    buttonRecord.setEnabled(false);
                    buttonStopRecord.setEnabled(true);

                    Toast.makeText(getApplicationContext(), "Recording audio...", Toast.LENGTH_LONG).show();
                }});

            //onClick method used to stop recording audio
            buttonStopRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    mediaRecorder.stop();
                    buttonStopRecord.setEnabled(false);
                    buttonStopPlay.setEnabled(false);
                    buttonPlay.setEnabled(true);
                    buttonRecord.setEnabled(true);
                }});

            //onClick method for playing the recorded audio
            buttonPlay.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    buttonStopPlay.setEnabled(true);
                    buttonStopRecord.setEnabled(false);
                    buttonRecord.setEnabled(false);

                    mediaPlayer = new MediaPlayer();
                    try{
                        mediaPlayer.setDataSource(filePath);
                        mediaPlayer.prepare();
                    }catch (IOException e) { e.printStackTrace(); }

                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing recorded audio", Toast.LENGTH_LONG).show();
                }});

            //onClick method for stopping mediaplayer
            buttonStopPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    buttonStopRecord.setEnabled(false);
                    buttonStopPlay.setEnabled(false);
                    buttonRecord.setEnabled(true);
                    buttonPlay.setEnabled(true);

                    if(mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        prepareMediaRecorder();
                    }
                }});
        }else{
            requestPermission();
        }
    }

    //setup the media recorder
    private void prepareMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(filePath);
    }

    //check if app can access permissions needed
    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;

    }

    //make a request for required permissions
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    //this is called when app gets response regarding permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case REQUEST_PERMISSION_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //write toast saying permission is granted
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                }else{
                    //write toast saying permission is not granted
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }
}
