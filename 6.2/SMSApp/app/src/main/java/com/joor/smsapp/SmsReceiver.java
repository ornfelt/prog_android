package com.joor.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

//this class inherits from BroadcastReceiver and handles received SMS
public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    //variables for sms content
    String msg, number;

    //this is called when SMS is received
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);

        Intent smsIntent = new Intent(context, MainActivity.class);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String messageAuthor = message.getOriginatingAddress();
        smsIntent.putExtra("MessageNumber", messageAuthor);
        smsIntent.putExtra("Message", message.getMessageBody());
        context.startActivity(smsIntent);
        Toast.makeText(context, "SMS Received from: " + messageAuthor, Toast.LENGTH_SHORT).show();

         */

        if(intent.getAction() == SMS_RECEIVED){
            Bundle dataBundle = intent.getExtras();
            if(dataBundle != null){
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for(int i = 0; i < mypdu.length; i++){
                    //for API > 23
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = dataBundle.getString("format");
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i], format);
                    }else{
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    msg = message[i].getMessageBody();
                    number = message[i].getOriginatingAddress();
                }
                Toast.makeText(context, "Message from: " + number + "\nMessage: " + msg, Toast.LENGTH_LONG).show();

                Intent smsIntent = new Intent(context, ReceivedSMSActivity.class);
                smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                smsIntent.putExtra("MessageNumber", number);
                smsIntent.putExtra("Message", msg);
                context.startActivity(smsIntent);
            }
        }
    }
}
