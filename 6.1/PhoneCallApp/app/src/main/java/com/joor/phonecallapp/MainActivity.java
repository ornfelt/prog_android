package com.joor.phonecallapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * This class implements functionality for receiving and making phone calls
 * @author: Jonas Örnfelt
 */
public class MainActivity extends AppCompatActivity {

    //edittext and button variable
    EditText editTextPhoneNumber;
    ImageButton imageButtonCall;
    //request code for making call
    private static final int REQUEST = 112;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize my graphic components
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        imageButtonCall = findViewById(R.id.imageButtonCall);
        context = getApplicationContext();

        //fix permissions needed for reading phone state on device
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }

        //make sure we have correct permissions to make a call
        if(Build.VERSION.SDK_INT >= 23){
            String[] permissions = {android.Manifest.permission.CALL_PHONE};
            if(!hasPermissions(context, permissions)){
                ActivityCompat.requestPermissions(this, permissions, REQUEST);
            }
        }
    }

    //onclick method that runs when user clicks on the call button
    public void onClickCall(View view){
        makeCall();
    }

    //check permissions for marshmallow
    private static boolean hasPermissions(Context context, String... permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null
        && permissions != null){
            for(String permission : permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    //make phone call to number entered into edittext field
    private void makeCall(){
        String number = editTextPhoneNumber.getText().toString();
        if(!number.isEmpty()){
            //try to make call via ACTION_CALL intent
            try {
                String telNumber = "tel: " + number;
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(telNumber));
                this.startActivity(intent);
            } catch(SecurityException e ) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to make call due to security exception", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
        }
    }

    //when permission has been accepted/denied by user
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case 1:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission to read phone state granted!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission to read phone state denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission to make phone call granted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission to make phone call denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
