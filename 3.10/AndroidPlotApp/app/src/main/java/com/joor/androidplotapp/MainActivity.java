package com.joor.androidplotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.Arrays;

/**
 * This app paints a XY graph showing a prime number curve
 * @autho: Jonas Örnfelt
 */
public class MainActivity extends AppCompatActivity {

    //variable for androidplot
    private XYPlot plot;
    //first ten prime numbers
    private Number[] primeNumbers = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init my androidplot
        plot = (XYPlot) findViewById(R.id.androidPlot);

        //single series of simple implementation of xy series interface
        XYSeries s1 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "my_series", 1, 7, 3, 11, 8, 16);

        XYSeries primeSeries = new SimpleXYSeries(Arrays.asList(primeNumbers),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "prime_series");
        //add series to plot with green color and no formatter
        plot.addSeries(primeSeries, new LineAndPointFormatter(Color.GREEN, Color.GREEN, null, null));
    }
}
