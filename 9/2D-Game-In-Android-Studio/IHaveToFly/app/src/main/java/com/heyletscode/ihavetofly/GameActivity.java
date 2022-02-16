package com.heyletscode.ihavetofly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

//this class inherits from AppCompatActivity and handles the GameActivity
public class GameActivity extends AppCompatActivity {

    //variable for view
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String character = intent.getStringExtra("character");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        //get display size
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView(this, point.x, point.y, character);

        setContentView(gameView);
    }

    //when game is paused
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //when game is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
