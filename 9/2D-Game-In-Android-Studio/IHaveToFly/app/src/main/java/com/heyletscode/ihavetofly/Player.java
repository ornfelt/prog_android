package com.heyletscode.ihavetofly;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.heyletscode.ihavetofly.Data.PokemonRetriever;
import com.heyletscode.ihavetofly.Model.Pokemon;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.heyletscode.ihavetofly.GameView.screenRatioX;
import static com.heyletscode.ihavetofly.GameView.screenRatioY;

//this class contains the player object
public class Player {

    int toShoot = 0;
    boolean isGoingUp = false;
    //size variables
    int x, y, width, height, wingCounter = 0, shootCounter = 1;
    Bitmap player1, player2, shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    private GameView gameView;
    int screenY;
    Resources res;

    //constructor for player
    Player (GameView gameView, int screenY, Resources res, Bitmap bitmap) {
        this.gameView = gameView;
        this.screenY = screenY;
        this.res = res;

        //player1 = BitmapFactory.decodeResource(res, R.drawable.player1);
        //player2 = BitmapFactory.decodeResource(res, R.drawable.player2);

        player1 = bitmap;
        player2 = bitmap;
        setupPlayer();

        //PokemonRetriever pokemonRetriever = new PokemonRetriever(context);
    }

    public void setupPlayer(){
        /* set sizes */

        width = player1.getWidth();
        height = player1.getHeight();

        //width /= 8;
        //height /= 8;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        //create bitmap for different player 'states'
        player1 = Bitmap.createScaledBitmap(player1, width, height, false);
        player2 = Bitmap.createScaledBitmap(player2, width, height, false);

        //create 5 shoot objects (5 at a time)
        /*
        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);
         */

        shoot1 = player1;
        shoot2 = player1;
        shoot3 = player1;
        shoot4 = player1;
        shoot5 = player1;
        dead = player1;

        y = screenY / 2;
        x = (int) (15 * screenRatioX);
    }

    //getter for player bitmap
    Bitmap getPlayer () {

        if (toShoot != 0) {

            /* return shoot object based on shootcounter */

            if (shootCounter == 1) {
                shootCounter++;
                return shoot1;
            }

            if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            }

            if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            }

            if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            }

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();

            return shoot5;
        }

        if (wingCounter == 0) {
            wingCounter++;
            return player1;
        }
        wingCounter--;

        return player2;
    }

    //get shape of player (used for tracking collision)
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    //return dead player bitmap
    Bitmap getDead () {
        return dead;
    }


}
