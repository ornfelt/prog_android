package com.joor.emailapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//this class is an adapter class for the listview
public class ListAdapter extends ArrayAdapter<String> {
    Context context;
    String[] date;
    String[] from;
    String[] subject;
    String[] message;

    ListAdapter(Context c, String[] date, String[] from, String[] subject, String[] message) {
        super(c, R.layout.row, R.id.textViewDate, date);
        this.context = c;
        this.date = date;
        this.from = from;
        this.subject = subject;
        this.message = message;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);

        //get textviews from row layout
        TextView textViewDate = row.findViewById(R.id.textViewDate);
        TextView textViewFrom = row.findViewById(R.id.textViewFrom);
        TextView textViewSubject = row.findViewById(R.id.textViewSubject);
        TextView textViewMessage = row.findViewById(R.id.textViewMessage);

        //make sure there's some data to display
        if (!date[position].isEmpty()) {
            try {
                //set data on textviews via position
                textViewDate.setText(date[position]);
                textViewFrom.setText("From: " + from[position]);
                textViewSubject.setText("Subject: " + subject[position]);
                textViewMessage.setText(message[position]);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        //return the row layout
        return row;
    }
}
