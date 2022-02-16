package com.joor.savetofileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import static java.lang.Long.parseLong;

/**
 * This app uses external storage to save new highest prime values found and displaying them via textview
 * @author: Jonas Örnfelt
 */

public class MainActivity extends AppCompatActivity {

    //variables for file and textview
    private File file;
    String fileName = "prime_numbers.txt";
    TextView textViewPrime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init textview for displaying primes
        textViewPrime = findViewById(R.id.textViewPrime);

        //init file
        File path = getApplicationContext().getExternalFilesDir(null);
        file = new File(path, fileName);

        long lastPrime = 0;
        try{
            lastPrime = parseLong(fileText(file));
        }catch(NumberFormatException e) { e.printStackTrace(); }
        System.out.println("last prime saved: " + lastPrime);

        textViewPrime.setText(String.valueOf(lastPrime));
    }

    //return string of file content
    private String fileText(File file){
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        FileInputStream in = null;

        //try to get file content
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            in.read(bytes);
            in.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(NullPointerException e ){
            e.printStackTrace();
        }
        //return file content as string
        String contents = new String(bytes);
        return contents;
    }

    //this method is called when user clicks on find prime button
    public void findNewPrime(View view){
        new FindPrimeAsync().execute();
    }

    //this method is called when user clicks reset button
    public void resetPrimeFile(View view){
        //reset prime value to 0
        writeNewPrime(0);
    }

    //this method is used to write a prime value to the prime_numbers.txt file
    private void writeNewPrime(long newPrime){
        //init file
        File path = getApplicationContext().getExternalFilesDir(null);
        file = new File(path, fileName);

        //get string of prime value
        String primeString = String.valueOf(newPrime);

        //write new prime to file
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(primeString.getBytes());
            stream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("text in file: " + fileText(file));
        //update textview value
        textViewPrime.setText(String.valueOf(newPrime));
    }

    //this class searches for a new prime asynchronously
    private class FindPrimeAsync extends AsyncTask<Void, Void, Void> {

        //variable for progress dialog
        private ProgressDialog progressDialog;
        long newPrimeAsync = 0;

        //show dialog saying that app is looking for new prime
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show a progress dialog
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please wait", "Searching for prime", true, false);
        }

        //this runs in the background (async)
        @Override
        protected Void doInBackground(Void... params) {
            //init file
            File path = getApplicationContext().getExternalFilesDir(null);
            file = new File(path, fileName);

            long lastPrime = 0;
            try{
                lastPrime = parseLong(fileText(file));
            }catch(NumberFormatException e) { e.printStackTrace(); }
            System.out.println("last prime saved: " + lastPrime);

            newPrimeAsync = lastPrime;
            int bitLength = 10;
            int loopCount = 0;

            //try to find bigger bigger prime than existing one
            boolean newPrimeFound = false;
            while(!newPrimeFound){
                BigInteger prime = new BigInteger(bitLength, 0, new Random());
                if(prime.longValue() > lastPrime){
                    System.out.println("potential new prime: " + prime);
                    if(isPrime(prime.longValue())){
                        newPrimeAsync = prime.longValue();
                        newPrimeFound = true;
                    }
                }

                //make sure bigLength is increasing
                if(loopCount == 10){
                    bitLength += 10;
                    loopCount = 0;
                }
                loopCount++;
            }
            return null;
        }

        //this method gets called when async doInBackground is completed
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //remove the progress dialog
            progressDialog.dismiss();
            //write new prime to file
            writeNewPrime(newPrimeAsync);
        }

        //checks if incoming long is prime
        private boolean isPrime(long candidate) {
            long sqrt = (long)Math.sqrt(candidate);
            for(long i = 3; i <= sqrt; i += 2)
                if(candidate % i == 0) return false;
            return true;
        }
    }
}
