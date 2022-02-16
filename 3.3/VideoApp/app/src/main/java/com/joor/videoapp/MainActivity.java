package com.joor.videoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

/**
 * This app provides functionality for recording video and playing it.
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //variables for request id and uri
    private static int VIDEO_REQUEST = 101;
    private Uri videoUri = null;
    //variables for permission
    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
    }

    //this onClick method is called when user presses the record button
    public void recordVideo(View view){
        //fix permission first
        if(!checkPermissionForCamera()){
            requestPermissionForCamera();
        } else{
            System.out.println("starting record video intent");
            //start intent to record video
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (videoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(videoIntent, VIDEO_REQUEST);
            }
        }
    }

    //this onClick method is called when user wants to play the recorded video
    public void playVideo(View view){
        if(videoUri != null) {
            //start new activity where there's a videoview that can play the recorded vidoe
            Intent playVideoIntent = new Intent(this, PlayVideoActivity.class);
            playVideoIntent.putExtra("videoUri", videoUri.toString());
            startActivity(playVideoIntent);
        }else{
            Toast.makeText(this, "No video recorded", Toast.LENGTH_LONG).show();
        }
    }

    //this method is called when video is done recording
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
        }
    }

    /**
     * The methods below are for asking the device for required permissions
     */

    //check if app has access to device's microphone
    public boolean checkPermissionForRecord(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    //check if app has access to device's external storage
    public boolean checkPermissionForExternalStorage(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    //check if app has access to device's camera
    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    //ask device for access to its microphone
    public void requestPermissionForRecord(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)){
            Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECORD_AUDIO},RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    //ask device for access to its external storage
    public void requestPermissionForExternalStorage(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    //ask device for access to its camera
    public void requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)){
            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
}
