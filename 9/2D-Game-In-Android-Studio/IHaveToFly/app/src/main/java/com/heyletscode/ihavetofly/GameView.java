package com.heyletscode.ihavetofly;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.heyletscode.ihavetofly.Data.HttpUtility;
import com.heyletscode.ihavetofly.Data.PokemonRetriever;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//this class contains the gameview (inhertits from SurfaceView)
public class GameView extends SurfaceView implements Runnable {

    /* variables for view */
    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Enemy[] enemys;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    private Player player;
    private GameActivity activity;
    private Background background1, background2;
    Bitmap bitmap = null;
    String chosenPokemon;

    //view constructor, takes activity and screen size
    public GameView(GameActivity activity, int screenX, int screenY, String chosenPokemon) {
        super(activity);
        this.activity = activity;
        this.chosenPokemon = chosenPokemon;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        //set soundpool (different depending on API > Marshmallow or not)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

            //else means lower version than Marshmallow API
        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        //load initialized soundpool
        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        //init background objects
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        //get url via chosenPokemon
        PokemonDataRetriever retriever = new PokemonDataRetriever(chosenPokemon);
        retriever.findPokemonSpriteUrl();

        //wait a bit before continuing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //start imageload which will call continueSetup when done
    public void startLoadImage(String pokemonImageUrl) {
        //get player image
        LoadImage loadImage = new LoadImage(bitmap);
        loadImage.execute(pokemonImageUrl);

        //wait a bit before continuing
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //this method is called asynchronously when async task is done
    public void continueSetup(Bitmap playerBitmap) {

        //most pokemon sprites need to be flipped
        if(chosenPokemon.equalsIgnoreCase("bulbasaur") ||
                chosenPokemon.equalsIgnoreCase("celebi") ||
                chosenPokemon.equalsIgnoreCase("charmander") ||
                chosenPokemon.equalsIgnoreCase("charizard") ||
                chosenPokemon.equalsIgnoreCase("deoxys-normal") ||
                chosenPokemon.equalsIgnoreCase("eevee") ||
                chosenPokemon.equalsIgnoreCase("lugia") ||
                chosenPokemon.equalsIgnoreCase("mew") ||
                chosenPokemon.equalsIgnoreCase("mewtwo") ||
                chosenPokemon.equalsIgnoreCase("pikachu") ||
                chosenPokemon.equalsIgnoreCase("squirtle")){
            playerBitmap = flip(playerBitmap);
        }

        //init player object with new player image from url
        player = new Player(this, screenY, getResources(), playerBitmap);
        //init bullets list
        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        //init enemies (4 at a time)
        enemys = new Enemy[4];

        //spawn 4 enemys
        for (int i = 0; i < 4; i++) {
            Enemy enemy = new Enemy(getResources());
            enemys[i] = enemy;
        }

        //handle spawning new enemies in separate thread
        Thread spawnEnemyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //spawn 4 new enemys every 2.5s seconds
                while (true) {

                    //wait for 2.5s
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //spawn 4 new enemys
                    for (int i = 0; i < 4; i++) {
                        Enemy enemy = new Enemy(getResources());
                        enemys[i] = enemy;
                    }
                }
            }
        });

        //start thread
        spawnEnemyThread.start();
        random = new Random();
    }

    //run method for thread
    @Override
    public void run() {
        //update as long as isPlaying is true
        while (isPlaying) {
            if (player != null && enemys != null) {
                update();
                draw();
                sleep();
            }
        }
    }

    //this is called from game loop to update values
    private void update() {

        random = new Random();

        /* keep the game moving */

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (player.isGoingUp)
            player.y -= 30 * screenRatioY;
        else
            player.y += 3 * screenRatioY;

        if (player.y < 0)
            player.y = 0;

        if (player.y >= screenY - player.height)
            player.y = screenY - player.height;

        List<Bullet> trash = new ArrayList<>();

        //update bullet(s)
        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX;

            for (Enemy enemy : enemys) {
                //if projectile hits enemy
                if (Rect.intersects(enemy.getCollisionShape(),
                        bullet.getCollisionShape())) {
                    score++;
                    enemy.x = -500;
                    bullet.x = screenX + 500;
                    enemy.wasShot = true;
                }
            }
        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (Enemy enemy : enemys) {

            if (enemy != null) {

                enemy.x -= enemy.speed;

                if (enemy.x + enemy.width < 0) {

                    if (!enemy.wasShot) {
                        //you can set the game to be over here if you want the player to lose if (s)he doesn't hit every spawned enemy
                        //isGameOver = true;
                        return;
                    }

                    int bound = (int) (30 * screenRatioX);
                    enemy.speed = random.nextInt(bound);

                        if (enemy.speed < 10 * screenRatioX)
                            enemy.speed = (int) (10 * screenRatioX);

                        enemy.x = screenX;
                        enemy.y = random.nextInt(screenY - enemy.height);

                        enemy.wasShot = false;
                }

                //if player hits an enemy, the game is over
                if (Rect.intersects(enemy.getCollisionShape(), player.getCollisionShape())) {
                    //isGameOver = true;
                    return;
                }
            }
        }
    }

    //draw method that is used throughout the games lifecycle
    private void draw() {

        if (getHolder().getSurface().isValid()) {
            //draw canvas on bitmap (background)
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Enemy enemy : enemys)
                canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);

            //update score text
            canvas.drawText(score + "", screenX / 2f, 164, paint);
            //if player loses, set isPlaying to false and update values
            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(player.getDead(), player.x, player.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(player.getPlayer(), player.x, player.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    //method that waits 2s before exiting game
    private void waitBeforeExiting() {
        try {
            Thread.sleep(2000);
            //go back to main activity (restarts)
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //save new high score (if score is higher than previous score)
    private void saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    //sleep for 17ms method
    private void sleep() {
        //attempt to sleep
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //when user resumes game
    public void resume() {
        //resume play state
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    //when user pauses
    public void pause() {
        //try to pause game
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //when user clicks screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            //first horizontal half of screen
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    player.isGoingUp = true;
                }
                break;
            //first horizontal half of screen
            case MotionEvent.ACTION_UP:
                player.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    player.toShoot++;
                break;
        }
        return true;
    }

    //create new bullet method
    public void newBullet() {
        //play sound if sound is unmuted
        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);

        //setup bullet
        Bullet bullet = new Bullet(getResources());
        bullet.x = player.x + player.width;
        bullet.y = player.y + (player.height / 2);
        bullets.add(bullet);
    }

    Bitmap flip(Bitmap d) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap dst = Bitmap.createBitmap(d, 0, 0, d.getWidth(), d.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }


    //class for loading image
    private class LoadImage extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;

        //constructor
        public LoadImage(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            String urlLink = strings[0];
            InputStream inputStream = null;
            try {
                inputStream = new java.net.URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        //resize bitmap method
        public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //resize bitmap before continuing setup
            if(chosenPokemon.equalsIgnoreCase("lugia") ||
                    chosenPokemon.equalsIgnoreCase("deoxys-normal") ||
                    chosenPokemon.equalsIgnoreCase("charizard") ||
                    chosenPokemon.equalsIgnoreCase("entei")){
                continueSetup(getResizedBitmap(bitmap, 150, 150));
            }else {
                continueSetup(getResizedBitmap(bitmap, 200, 200));
            }
        }
    }

    //this class handles the retrieval of pokemon data from an existing free API
    private class PokemonDataRetriever {

        private static final String baseUrl = "https://pokeapi.co/api/v2/";
        String pokemonName;

        //no args constructor
        public PokemonDataRetriever(final String pokemonName) {
            this.pokemonName = pokemonName;
        }

        //make call to poke API in order to find pokemon id and parse the sprite url (image), then return it
        public void findPokemonSpriteUrl() {
            //the string we will return
            final List<String> returnValues = new ArrayList<>();

            Thread retrieveThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    //parse requestURL with baseurl for poke API
                    String requestURL = baseUrl + "pokemon/" + pokemonName;

                    // try to send get request to given url
                    try {
                        HttpUtility.sendGetRequest(requestURL);
                        String[] response = HttpUtility.readMultipleLinesRespone();
                        int charPosition = 0;
                        String resultString = "";

                        //find pokemons id via response strings
                        for (String s : response) {
                            resultString = s;
                            List<Integer> wordsIndexes = findWordIndexes(s, "\"id\":");
                            System.out.println("wordsIndexes 1: " + wordsIndexes.get(0) + ", out of: " + wordsIndexes.size());
                            charPosition = wordsIndexes.get(0);
                        }

                        //get idstring that we can later parse to int
                        String idString = "";
                        for (int i = 0; i < 8; i++) {
                            System.out.print(resultString.charAt(charPosition + i));
                            idString += resultString.charAt(charPosition + i);
                        }

                        //parse id to int
                        String[] splitIdString = idString.split(":");
                        String[] splitComma = splitIdString[1].split(",");
                        int pokemonId = Integer.parseInt(splitComma[0]);
                        System.out.println("\nid: " + pokemonId);

                        //with the id we can parse a valid sprite url and shiny sprite url
                        //example JSONS "front_default": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                        //"front_shiny": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png"

                        String baseSpriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
                        String baseSpriteShinyUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/";

                        String spriteUrl = baseSpriteUrl + pokemonId + ".png";
                        String spriteShinyUrl = baseSpriteShinyUrl + pokemonId + ".png";
                        //test print to make sure url's are correct
                        System.out.println("spriteUrl : " + spriteUrl + "\n" + "spriteShinyUrl: " + spriteShinyUrl);

                        //10% chance to get a shiny pokemon sprite
                        if (gambleForShiny(10)) {
                            startLoadImage(spriteShinyUrl);
                        } else {
                            //else normal sprite
                            startLoadImage(spriteUrl);
                        }
                        HttpUtility.disconnect();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            retrieveThread.start();
        }

        //find the indexes of where the incoming parameter word appears in incoming parameter textString
        public List<Integer> findWordIndexes(String textString, String word) {
            List<Integer> indexes = new ArrayList<Integer>();
            StringBuilder output = new StringBuilder();
            String lowerCaseTextString = textString.toLowerCase();
            String lowerCaseWord = word.toLowerCase();
            int wordLength = 0;

            int index = 0;
            while (index != -1) {
                index = lowerCaseTextString.indexOf(lowerCaseWord, index + wordLength);  // Slight improvement
                if (index != -1) {
                    indexes.add(index);
                }
                wordLength = word.length();
            }
            return indexes;
        }

        //if parameter chance is 10, true is returned 10% of the time
        private boolean gambleForShiny(int chance) {
            Random random = new Random();
            int nextInt = random.nextInt(chance);
            if (nextInt == 0) {
                //happens chance% of the time...
                return true;
            } else {
                return false;
            }
        }
    }
}
