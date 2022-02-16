package com.heyletscode.ihavetofly;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.ihavetofly.GameView.screenRatioX;
import static com.heyletscode.ihavetofly.GameView.screenRatioY;

//this class contains the projectile that the player can shoot out
public class Bullet {

    //size variables
    int x, y, width, height;
    Bitmap bullet;

    //bullet constructor
    Bullet (Resources res) {

        //init bitmap
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

        /* set size */
        width = bullet.getWidth();
        height = bullet.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        //creat bitmap
        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);
    }

    //get shape for this (used for tracking collisions)
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
