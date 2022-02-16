package com.heyletscode.ihavetofly;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.ihavetofly.GameView.screenRatioX;
import static com.heyletscode.ihavetofly.GameView.screenRatioY;

//this class contains the enemy object
public class Enemy {

    //speed for enemy
    public int speed = 10;
    //boolean controlling if enemy is alive or not
    public boolean wasShot = true;
    //size variables
    int x = 0, y, width, height, enemyCounter = 1;
    Bitmap enemy1, enemy2, enemy3, enemy4;

    Enemy (Resources res) {
        enemy1 = BitmapFactory.decodeResource(res, R.drawable.enemy1);
        enemy2 = BitmapFactory.decodeResource(res, R.drawable.enemy2);
        enemy3 = BitmapFactory.decodeResource(res, R.drawable.enemy3);
        enemy4 = BitmapFactory.decodeResource(res, R.drawable.enemy4);

        width = enemy1.getWidth();
        height = enemy1.getHeight();

        //might need adjusting for different displays
        width /= 13;
        height /= 13;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        //create bitmap for enemies (4 at a time)
        enemy1 = Bitmap.createScaledBitmap(enemy1, width, height, false);
        enemy2 = Bitmap.createScaledBitmap(enemy2, width, height, false);
        enemy3 = Bitmap.createScaledBitmap(enemy3, width, height, false);
        enemy4 = Bitmap.createScaledBitmap(enemy4, width, height, false);

        y = -height;
    }

    //getter for enemy object
    Bitmap getEnemy () {

        //get first enemy
        if (enemyCounter == 1) {
            enemyCounter++;
            return enemy1;
        }

        //get second enemy
        if (enemyCounter == 2) {
            enemyCounter++;
            return enemy2;
        }

        //get third enemy
        if (enemyCounter == 3) {
            enemyCounter++;
            return enemy3;
        }

        enemyCounter = 1;
        //get fourth enemy
        return enemy4;
    }

    //getter for enemy shape (used for tracking collisions)
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
