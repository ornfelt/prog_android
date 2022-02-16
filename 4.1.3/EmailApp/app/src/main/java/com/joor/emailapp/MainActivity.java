package com.joor.emailapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

/**
 * This app can be used to send and receive emails. In this activity we can see our inbox and go to MailActivity to send an email.
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //variables for listview and inbox content that we will show in list
    ListView listView;
    String[] emailDates;
    String[] emailFroms;
    String[] emailSubjects;
    String[] emailMessages;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init listview
        listView = this.findViewById(R.id.listView);
        getInboxContent();
        setupSendMailButton();
    }

    //get inbox content via async task, which will call setupListView when done
    private void getInboxContent(){
        new MailInboxAsync().execute();
    }

    //configure floating action button so that it navigates to mailactivity
    private void setupSendMailButton(){
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.bringToFront();
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, MailActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    //this gets called from the mailinboxasync class when data is ready
    private void setupListView(List<Email> emails){

        //ListAdapter adapter = new ListAdapter(getApplicationContext(), emailDates, emailFroms, emailSubjects, emailMessages);
        EmailAdapter adapter = new EmailAdapter((Activity)this, emails);
        //set adapter on listview
        listView.setAdapter(adapter);

        //tell user about floating action button in the bottom right corner
        Toast.makeText(this, "Press the button in the bottom\nright corner to send a new mail!", Toast.LENGTH_LONG).show();
    }

    //this class gets data from inbox and then calls listview to show items when ready
    private class MailInboxAsync extends AsyncTask<Void, Void, Void> {

        //variable for progress dialog
        private ProgressDialog progressDialog;
        List<Email> emails = new ArrayList<>();

        //show dialog saying that the inbox is loading
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show a progress dialog
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please wait", "Loading inbox...", true, false);
        }

        //this runs in the background (async)
        @Override
        protected Void doInBackground(Void... params) {
            //email credentials
            String myEmail = "jonjavason@gmail.com";
            String myPassword = "myjavamail";

            //set properties that session will use
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "imaps");
            try {
                //try to setup session with properties and connect to email
                Session session = Session.getInstance(props, null);
                Store store = session.getStore();
                store.connect("imap.gmail.com", myEmail, myPassword);
                //get inbox content
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
                Message[] result = inbox.getMessages();

                int resultLength = result.length;

                //save inbox data to string arrays
                for(int i = 0; i < resultLength; i++){
                    //get email sent date
                    String emailDate = result[i].getSentDate().toString();
                    javax.mail.Address[] froms = result[i].getFrom();
                    String[] emailFroms = new String[froms.length];
                    //get from addresses
                    for(int j = 0; j < froms.length; j++){
                        emailFroms[j] = froms[j].toString();
                    }

                    String emailSubject = result[i].getSubject();
                    String emailMessage = "";

                    if(result[i].getContent().toString().contains("MimeMultipart")){
                        Multipart mp = (Multipart) result[i].getContent();
                        BodyPart bp = mp.getBodyPart(0);
                        emailMessage = bp.getContent().toString();
                    }else {
                        emailMessage = result[i].getContent().toString();
                    }
                    //create email object and add to list of emails
                    Email email = new Email(emailDate, emailFroms, emailSubject, emailMessage);
                    emails.add(email);

                    //test print email data
                    /*
                    System.out.println("Date: " + email.getDate() + "\n"
                            + "From: " + email.getFrom()[0] + "\n"
                            + "Subject: " + email.getSubject() + "\n"
                            + "Message: " + email.getMessage() + "\n");

                     */
                }

            } catch (Exception mex) {
                mex.printStackTrace();
            }
            //revert order for mails (to make them in order)
            List<Email> tempList = new ArrayList<>();

            for(int i = 1; i < emails.size()+1; i++){
                tempList.add(emails.get(emails.size()-i));
            }
            emails = tempList;

            return null;
        }

        //this method gets called when async doInBackground is completed
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //remove the progress dialog
            progressDialog.dismiss();
            setupListView(emails);
        }
    }
}
