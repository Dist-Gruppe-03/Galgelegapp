package com.quaade94.galgeleg;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Quaade94 on 24/04/2018.
 */

public class RESTAPI {


    URL githubEndpoint;
    HttpURLConnection myConnection;
    InputStream responseBody;
    InputStreamReader responseBodyReader;
    JsonReader jsonReader;
    String value;
    String userID;
    String name;
    String invisibleWord;
    String usedLetters;
    String guessedLetter;
    String response;
    String gameOver;
    String wrongLetters;



    void connect() {
        final String[] value = new String[1];

        class AsyncTask1 extends AsyncTask {
            @Override
            protected Object doInBackground(Object... arg0) {
                try {
                    // Create URL
                    githubEndpoint = new URL("http://ubuntu4.saluton.dk:38055/RestServer/hangman/play/json/s114992");

                    // Create connection
                    myConnection = (HttpURLConnection) githubEndpoint.openConnection();
                    if (myConnection.getResponseCode() == 200) {
                        // Success
                        // Further processing here

                        Log.e("Activity","CONNECTION WORKS");
                    } else {
                        Log.e("Activity","CONNECTION FAILED");

                        // Error handling code goes here
                    }
                    responseBody = myConnection.getInputStream();
                    responseBodyReader = new InputStreamReader(responseBody, "UTF-8");


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object result) {

                setValues();
                myConnection.disconnect();

            }
        }
        AsyncTask1 a = new AsyncTask1();
        a.execute();


    }

    String getValue(String theKey){
        String value = "";
        jsonReader = new JsonReader(responseBodyReader);
        try {
            jsonReader.beginObject(); // Start processing the JSON object
            while (jsonReader.hasNext()) { // Loop through all keys
                String key = jsonReader.nextName(); // Fetch the next key
                if (key.equals(theKey)) { // Check if desired key
                    // Fetch the value as a String
                    value = jsonReader.nextString();
                    Log.e("Activtiy","USER ID IS: " + value);
                    // Do something with the value
                    // ...
                    break; // Break out of the loop
                } else {
                    jsonReader.skipValue(); // Skip values of other keys
                }
            }
            jsonReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    void setValues(){
        userID = getValue("userid");
        name = getValue("name");
        invisibleWord = getValue("invisibleword");
        usedLetters = getValue("usedletters");
        guessedLetter = getValue("guessedletter");
        response = getValue("response");
        gameOver = getValue("gameover");
        wrongLetters = getValue("wrongletters");
        Log.e("VALUES SET: ",userID + " " + name  + " " + invisibleWord + " " + usedLetters + " " + guessedLetter + " " + response + " " + gameOver + " " + wrongLetters);
    }

    String getUserID(){
        return userID;
    }

    String getName(){
        return name;
    }

    String getinvisibleWord(){
        return invisibleWord;
    }

    String getUsedLetters(){
        return usedLetters;
    }


    String getGuessedLetter(){
        return guessedLetter;
    }

    String getResponse(){
        return response;
    }

    boolean getGameOver(){
        if(gameOver.equals("true")) return true;
        if(gameOver.equals("false")) return false;
        else{
            Log.e("RESTAPI","THE GAME IS NEITHER OVER OR NOT, SEE RESTAPI.java");
            return false;
        }
    }

    String getWrongLetters(){
        return wrongLetters;
    }

}
