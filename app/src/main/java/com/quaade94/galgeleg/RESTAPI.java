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

    private static RESTAPI instance;

    public static RESTAPI getInstance(){
        if(instance == null){
            instance = new RESTAPI();
            System.out.println("RESTAPI.instance var NULL - frisk start! Opretter en instance");
        }
        return instance;
    }

    URL githubEndpoint;
    HttpURLConnection myConnection;
    InputStream responseBody;
    InputStreamReader responseBodyReader;
    JsonReader jsonReader;
    String userID;
    String name;
    String invisibleWord;
    String usedLetters;
    String guessedLetter;
    String response;
    String gameOver;
    String wrongLetters;
    String request = "";
    boolean loading = false;




    void connect(String req){
        loading = true;
        setRequest(req);
        class AsyncTask1 extends AsyncTask {
            @Override
            protected Object doInBackground(Object... arg0) {
                try {
                        // Create URL
                        githubEndpoint = new URL("http://ubuntu4.saluton.dk:38055/RestServer/hangman/play/json/s114992" + request);
                        // Create connection
                        myConnection = (HttpURLConnection) githubEndpoint.openConnection();
                        // Test connection
                        if (myConnection.getResponseCode() == 200) {
                            Log.e("ASYNC Connection","CONNECTION SUCCESS");
                        } else {
                            Log.e("ASYNC Connection","CONNECTION FAILED");
                        }
                    responseBody = myConnection.getInputStream();
                    responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object result) {
                jsonReader = new JsonReader(responseBodyReader);
                try {
                    jsonReader.beginObject();
                    setValues();
                    jsonReader.close();
                    myConnection.disconnect();
                    loading = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("ASYNC Connection","CONNECTION DISCONNECTED");
            }
        }
        AsyncTask1 a = new AsyncTask1();
        a.execute();
    }

    private void setRequest(String request) {
        this.request = request;
    }


    boolean loginRequest(String user, String pass){

        //TODO: Implement method
        return true;
    }

    String getValue(String theKey){
        String value = "";
        try {

            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals(theKey)) {
                    value = jsonReader.nextString();
                    Log.e("JSON Reader","USER ID IS: " + value);
                    break;
                } else {
                    jsonReader.skipValue(); // Skip values of other keys
                }
            }
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
            Log.e("GAMEOVER","SOMETHING WENT WRONG, SEE RESTAPI.java");
            return false;
        }
    }

    int getWrongLetters(){
        return Integer.parseInt(wrongLetters);
    }

    public void guessLetter(String s) {
        connect("?letter=" + s);
    }

    public void resetGame(){
        connect("?reset=true");
    }

    public boolean isLoading(){
        return loading;
    }

    public void sleep(){


    }
}
