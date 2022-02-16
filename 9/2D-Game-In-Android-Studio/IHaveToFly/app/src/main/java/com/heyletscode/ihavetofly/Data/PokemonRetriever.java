package com.heyletscode.ihavetofly.Data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.heyletscode.ihavetofly.Model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//this class handles the retrieval of pokemon data from an existing free API
public class PokemonRetriever {

    private static final String baseUrl = "https://pokeapi.co/api/v2/";
    private RequestQueue requestQueue;
    String pokemonName;
    private String pokemonImageUrl;

    //no args constructor
    public PokemonRetriever(Context context, final String pokemonName) {
        requestQueue = Volley.newRequestQueue(context);
        this.pokemonName = pokemonName;
    }

    //make call to poke API in order to find pokemon id and parse the sprite url (image), then return it
    public String getPokemonSpriteUrl(){
        //the string we will return
        final List<String> returnValues = new ArrayList<>();

        Thread retrieveThread = new Thread(new Runnable() {
            @Override
            public void run() {

        //parse requestURL with baseurl for poke API
        String requestURL = baseUrl + "pokemon/" + "pikachu";

        // try to send get request to given url
        try {
            HttpUtility.sendGetRequest(requestURL);
            String[] response = HttpUtility.readMultipleLinesRespone();
            int charPosition = 0;
            String resultString = "";

            //find pokemons id via response strings
            for(String s : response) {
                resultString = s;
                List<Integer> wordsIndexes = findWordIndexes(s, "\"id\":");
                System.out.println("wordsIndexes 1: " + wordsIndexes.get(0) + ", out of: " + wordsIndexes.size());
                charPosition = wordsIndexes.get(0);
            }

            //get idstring that we can later parse to int
            String idString = "";
            for(int i = 0; i < 8; i++) {
                System.out.print(resultString.charAt(charPosition+i));
                idString += resultString.charAt(charPosition+i);
            }

            //parse id to int
            String[] splitIdString = idString.split(":");
            String[] splitComma =splitIdString[1].split(",");
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
            if(gambleForShiny(10)){
                returnValues.add(spriteShinyUrl);
            }else{
                //else normal sprite
                returnValues.add(spriteUrl);
            }
            HttpUtility.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
            }
        });
        retrieveThread.start();
        return returnValues.get(0);
    }

    public String getPokemonImageUrl(){
        if(pokemonImageUrl == null) return null;
        return pokemonImageUrl;
    }

    //find the indexes of where the incoming parameter word appears in incoming parameter textString
    public List<Integer> findWordIndexes(String textString, String word) {
        List<Integer> indexes = new ArrayList<Integer>();
        StringBuilder output = new StringBuilder();
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();
        int wordLength = 0;

        int index = 0;
        while(index != -1){
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
        }else {
            return false;
        }
    }

    //this method gets JSON data from poke API
    public void loadPokemon() {
        //list that will contain the pokemon(s)
        final List<Pokemon> pokemons = new ArrayList<>();
        String url = "https://pokeapi.co/api/v2/pokemon?limit=151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            int id = 0;
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                String name = result.getString("name");
                                if(name.equalsIgnoreCase(pokemonName)){
                                    System.out.println("found pokemon: " + name);
                                    id = result.getInt("id");
                                    pokemons.add(new Pokemon(id, name));
                                }
                            }

                            for(Pokemon p : pokemons){
                                System.out.println("pokemon added: " + p.getId());
                            }

                            //with the id we can parse a valid sprite url and shiny sprite url
                            //example JSONS "front_default": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                            //"front_shiny": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png"

                            String baseSpriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";
                            String baseSpriteShinyUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/";

                            String spriteUrl = baseSpriteUrl + id + ".png";
                            String spriteShinyUrl = baseSpriteShinyUrl + id + ".png";
                            //test print to make sure url's are correct
                            System.out.println("spriteUrl : " + spriteUrl + "\n" + "spriteShinyUrl: " + spriteShinyUrl);

                            //10% chance to get a shiny pokemon sprite
                            if(gambleForShiny(10)){
                                pokemonImageUrl = spriteUrl;
                            }else{
                                //else normal sprite
                                pokemonImageUrl = spriteUrl;

                            }


                        } catch (JSONException e) {
                            Log.e("cs50", "onResponse: ", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("cs50", "onErrorResponse: pokemon list error");
                System.out.println("error: " + error);
            }
        });
        requestQueue.add(request);
    }
}
