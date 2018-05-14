package com.quaade94.galgeleg;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Quaade94 on 24/04/2018.
 */

public class RESTService {

    private static RESTService instance;

    public static RESTService getInstance(){
        if(instance == null){
            instance = new RESTService();
            System.out.println("RESTService.instance var NULL - frisk start! Opretter en instance");
        }
        return instance;
    }

    URL httpbinEndpoint;
    HttpURLConnection myConnection;
    InputStream responseBody;
    InputStreamReader responseBodyReader;
    OutputStreamWriter responseBodyWriter;
    JsonReader jsonReader;
    JsonWriter jsonWriter;
    String userID;
    String name;
    String invisibleWord;
    String usedLetters;
    String guessedLetter;
    String response;
    String gameOver;
    String wrongLetters;
    String url;
    boolean loginFailed;


    boolean connect(String request){
                try {
                    //url
                    httpbinEndpoint = new URL(url + request);
                    Log.e("CONNECT","TRYING TO CONNECT WITH URL: " + url + " WITH REQUEST: " + request);
                    // Create connection
                    myConnection = (HttpURLConnection) httpbinEndpoint.openConnection();
                    myConnection.setRequestMethod("GET");
                    // Test connection
                    if (myConnection.getResponseCode() == 200) {
                        Log.e("ASYNC Connection","CONNECTION SUCCESS");
                    } else {
                        Log.e("ASYNC Connection","CONNECTION FAILED");
                        return false;
                    }
                    responseBody = myConnection.getInputStream();
                    responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    jsonReader = new JsonReader(responseBodyReader);

                    jsonReader.beginObject();
                    setValues();
                    jsonReader.close();
                    myConnection.disconnect();
                    Log.e("ASYNC Connection","CONNECTION DISCONNECTED");
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
    }

    boolean loginRequest(String user, String pass){
        try{
            this.userID = user;
            httpbinEndpoint = new URL("http://ubuntu4.saluton.dk:38055/RestServer/hangman/login/");
            // Create connection
            myConnection = (HttpURLConnection) httpbinEndpoint.openConnection();
            myConnection.setRequestMethod("POST");
            myConnection.setRequestProperty("Content-Type", "application/json");
            // Enable writing
            myConnection.setDoOutput(true);
            // Write the data
            myConnection.getOutputStream().write(("{\"username\" : \"" + user + "\", \"password\" : \"" + pass + "\"}").getBytes());
            InputStream responseBody = myConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));
            url = reader.readLine();
            if(url!=null){
                Log.e("ENDPOINT","ENDPOINT SET TO: " + url);
                loginFailed = false;
            }else{
                loginFailed = true;
                Log.e("ENDPOINT FAIL","ENDPOINT SET TO: " + url);
            }
            myConnection.disconnect();
            return true;

        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }
        //TODO: Implement method
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

    boolean getLoginFailed(){
        return loginFailed;
    }

    boolean getGameOver(){
        if(gameOver.equals("true")) return true;
        if(gameOver.equals("false")) return false;
        else{
            Log.e("GAMEOVER","SOMETHING WENT WRONG, SEE RESTService.java");
            return false;
        }
    }

    int getWrongLetters(){
        try{
            return Integer.parseInt(wrongLetters);
        }catch (Exception e){
            Log.e("ERROR","TRYING TO GET A VARIABLE THAT IS NOT YET SET");
            return 0;
        }
    }

    public void guessLetter(String s) {
        connect("?letter=" + s);
    }

    public void resetGame(){
        connect("?reset=true");
    }
}