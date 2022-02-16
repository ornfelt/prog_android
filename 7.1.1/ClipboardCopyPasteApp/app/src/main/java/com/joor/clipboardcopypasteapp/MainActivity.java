package com.joor.clipboardcopypasteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class uses the ClipboardManager to provide copy/paste functionality
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //component variables
    EditText editText;
    Button buttonCopy;
    Button buttonPaste;
    TextView textView;

    ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize graphic elements
        editText = findViewById(R.id.editText);
        buttonCopy = findViewById(R.id.buttonCopy);
        buttonPaste = findViewById(R.id.buttonPaste);
        textView = findViewById(R.id.textView);

        //init clipboard service
        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        if(!clipboardManager.hasPrimaryClip()){
            buttonPaste.setEnabled(false);
        }
    }

    //this method is called when user clicks the copy button
    public void onClickCopy(View view){
        //get text
        String textToCopy = editText.getText().toString();
        //make sure something is entered into text field
        if(!textToCopy.equals("") && textToCopy != null){
            ClipData clipData = ClipData.newPlainText("text", textToCopy);
            clipboardManager.setPrimaryClip(clipData);
            //make toast saying text is copied and enable paste button so user can paste the text
            Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show();
            buttonPaste.setEnabled(true);
        }else{
            //tell user that (s)he needs to enter something into text field
            Toast.makeText(this, "Enter something into text field,\n" +
                    "then press copy", Toast.LENGTH_SHORT).show();
        }
    }

    //this method is called when user clicks the paste button
    public void onClickPaste(View view){
        //get data from clipboard
        ClipData clipData = clipboardManager.getPrimaryClip();
        //get first item
        ClipData.Item item = clipData.getItemAt(0);

        textView.setText(item.getText().toString());
        Toast.makeText(this, "Pasted text", Toast.LENGTH_SHORT).show();
    }
}
