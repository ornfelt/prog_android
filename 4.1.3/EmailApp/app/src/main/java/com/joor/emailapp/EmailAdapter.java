package com.joor.emailapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

//this class is an adapter class for the listview
public class EmailAdapter extends BaseAdapter {
    Context context;
    List<Email> emailObjects;
    private LayoutInflater inflater = null;

    public EmailAdapter(Activity context, List<Email> emailObjects){
        this.context = context;
        this.emailObjects = emailObjects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.row, null) : itemView;

        if(position <= emailObjects.size()) {
            //get textviews from row layout
            TextView textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            TextView textViewFrom = (TextView) itemView.findViewById(R.id.textViewFrom);
            TextView textViewSubject = (TextView) itemView.findViewById(R.id.textViewSubject);
            TextView textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);

            //get email object
            Email email = emailObjects.get(position);
            System.out.println("position: "  + position + ", emailObjects.size: " + emailObjects.size());
            System.out.println("emailobject subject: " + email.getSubject());

            //make sure there's some data to display
            if (!email.getDate().isEmpty()) {
                try {
                    //set data on textviews via position
                    textViewDate.setText(email.getDate());
                    //get first from
                    String froms = email.getFrom()[0];
                    //get other froms (if any) and append onto string
                    for (int i = 1; i < email.getFrom().length; i++) {
                        froms += (", " + email.getFrom()[i]);
                    }
                    textViewFrom.setText("From: " + froms);
                    textViewSubject.setText("Subject: " + email.getSubject());
                    textViewMessage.setText(email.getMessage());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        //return the itemView layout
        return itemView;
    }

    @Override
    public int getCount() {
        return emailObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return emailObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}