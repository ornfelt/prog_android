package com.joor.emailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This app can be used to send and receive Email directly from the app
 * @author: Jonas Örnfelt
 */

public class MailActivity extends AppCompatActivity {

    //variables for components
    EditText editTextEmail, editTextSubject, editTextMessageBody;
    Button buttonSendEmail;
    //variables for senders email and password
    String sEmail, sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        //initialize components
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessageBody = findViewById(R.id.editTextMessageBody);
        buttonSendEmail = findViewById(R.id.buttonSendEmail);

        //credentials for senders email
        sEmail = "jonjavason@gmail.com";
        sPassword = "myjavamail";

        //set onClick method for send button
        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get info entered from mail form
                String emailTo = editTextEmail.getText().toString();
                String emailSubject = editTextSubject.getText().toString();
                String emailMessage = editTextMessageBody.getText().toString();

                System.out.println("emailTO: " + emailTo + "\n"
                + "emailSUbject: " + emailSubject + "\n" +
                        "emailMess: " + emailMessage);

                //make sure there's data in all required fields
                if (!emailTo.isEmpty() && !emailSubject.isEmpty() && !emailMessage.isEmpty()) {
                    //setup properties that mail session will use
                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.starttls.enable", "true");
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.port", "587");

                    //setup session
                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(sEmail, sPassword);
                        }
                    });
                    try {
                        //setup content of new email
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(sEmail));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(emailTo.trim()));
                        message.setSubject(emailSubject.trim());
                        message.setText(emailMessage.trim());

                        //send the email
                        new SendMail().execute(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                } else {
                    //else tell user to fill in all fields
                    Toast.makeText(MailActivity.this, "Please fill in every text field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //navigate back to mainactivity if user clicks on back button
    @Override
    public void onBackPressed(){
        Intent returnIntent = new Intent(this, MainActivity.class);
        this.startActivity(returnIntent);
    }

    //this class handles mail sending via asynctask
    private class SendMail extends AsyncTask<Message, String, String> {

        //variable for progress dialog
        private ProgressDialog progressDialog;

        //this method runs before doInBackground starts
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show a progress dialog
            progressDialog = ProgressDialog.show(MailActivity.this,
                    "Please wait", "Sending mail...", true, false);
        }

        //this method runs in background (asynchronously)
        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) { e.printStackTrace(); return "Error"; }
        }

        //this method is called when doInBackground has finished
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //remove the progress dialog
            progressDialog.dismiss();
            if(s.equals("Success")){
                //mail successfully sent
                AlertDialog.Builder builder = new AlertDialog.Builder(MailActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509234'>Success</font>"));
                builder.setMessage("Mail sent successfully");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        //clear text fields
                        editTextEmail.setText("");
                        editTextSubject.setText("");
                        editTextMessageBody.setText("");
                    }
                });
                //show alert dialog
                builder.show();
            }else{
                //error happened...
                Toast.makeText(getApplicationContext(),
                        "Something went wrong while sending email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
