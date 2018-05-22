package com.quaade94.galgeleg;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
    InputStream in;
    OutputStream out;
    InputStreamReader inReader;
    OutputStreamWriter outWriter;
    JsonReader jsonReader;
    JsonWriter jsonWriter;
    String userID;
    String name;
    String invisibleWord;
    String usedLetters;
    String response;
    String gameOver;
    String wrongLetters;
    String url;
    String urlParameters;
    DataOutputStream wr;
    boolean loginFailed;


    boolean connect(){
                try {
                    //url
                    Log.e("CONNECT","TRYING TO CONNECT WITH URL: " + url);
                    httpbinEndpoint = new URL(url);
                    // Create connection
                    myConnection = (HttpURLConnection) httpbinEndpoint.openConnection();
                    myConnection.setRequestMethod("GET");
                    myConnection.setRequestProperty("Content-Type", "application/json");
                    in = myConnection.getInputStream();
                    inReader = new InputStreamReader(in, "UTF-8");
                    jsonReader = new JsonReader(inReader);
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
            //set the start url
            url = "http://ubuntu4.saluton.dk:38055/web/api/login";
            httpbinEndpoint = new URL(url);
            // Create connection
            Log.e("loginRequest", "starter forbindelse med url: " + url);
            myConnection = (HttpURLConnection) httpbinEndpoint.openConnection();
            //set the request method
            myConnection.setRequestMethod("POST");
            //set the header
            myConnection.setRequestProperty("Content-Type", "application/json");
            // Enable writing
            myConnection.setDoOutput(true);
            //output
            outWriter = new OutputStreamWriter(myConnection.getOutputStream());
            //create JSON object to write to
            jsonWriter = new JsonWriter(outWriter);
            // Write the data
            jsonWriter.beginObject();
            jsonWriter.name("username").value(user);
            jsonWriter.name("password").value(pass);
            jsonWriter.endObject();
            outWriter.write(jsonWriter.toString());
            jsonWriter.close();
            outWriter.close();
            in = myConnection.getInputStream();
            inReader = new InputStreamReader(in, "UTF-8");
            jsonReader = new JsonReader(inReader);
            try {
                jsonReader.beginObject();
                //retrieve url from JSON object
                url = JsonReader("gamepath");
                jsonReader.close();
            }catch(EOFException e){
                e.printStackTrace();
            }
            myConnection.disconnect();
            Log.e("ASYNC Connection","CONNECTION DISCONNECTED");
            //check to see if url is null
            if(!url.equals("http://ubuntu4.saluton.dk:38055/web/api/login")){
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
    }

    boolean resetAndLetter(String resetOrLetter, String letter){
        try{
            //set the start url
            httpbinEndpoint = new URL(url);
            // Create connection
            Log.e("resetAndLetter", "starter forbindelse med url: " + url);
            myConnection = (HttpURLConnection) httpbinEndpoint.openConnection();
            //set the request method
            myConnection.setRequestMethod("POST");
            //set the header
            myConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // Enable writing
            myConnection.setDoOutput(true);
            //output
            //outWriter = new OutputStreamWriter(myConnection.getOutputStream());
            wr = new DataOutputStream(myConnection.getOutputStream());
            if(resetOrLetter.equals("reset")){
                urlParameters = "reset=true";
            }else if (resetOrLetter.equals("letter")){
                urlParameters = "letter=" + letter;
            }else{
                Log.e("resetAndLetter", "Noget gik galt med valg af reset/letter");
            }
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            wr.write(postData);

            //input
            InputStream responseBody = myConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody));
            response = reader.readLine();
            Log.e("reserAndLetter","response: " + response);
            myConnection.disconnect();
            return true;

        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }
    }

    String JsonReader(String theKey){
        String value = "";
        try {

            while (jsonReader.hasNext()) {
                String key = jsonReader.nextName();
                if (key.equals(theKey)) {
                    value = jsonReader.nextString();
                    Log.e("JSON Reader","Read from JSON: " + value);
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
        userID = JsonReader("userid");
        name = JsonReader("name");
        invisibleWord = JsonReader("invisibleword");
        usedLetters = JsonReader("usedletters");
        gameOver = JsonReader("gameover");
        wrongLetters = JsonReader("wrongletters");
        Log.e("VALUES SET: ",userID + " " + name  + " " + invisibleWord + " " + usedLetters + gameOver + " " + wrongLetters);
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
        resetAndLetter("letter",s);
    }

    public void resetGame(){
        resetAndLetter("reset","");
    }
}