package com.quaade94.galgeleg;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Quaade94 on 15/01/2017.
 */

public class Frontpage extends Activity {

    EditText user, pass;
    Button button;
    RESTAPI RA = RESTAPI.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        button = (Button) findViewById(R.id.button);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Activtiy","STARTER");
                button.setText("Arbejder");
                class AsyncTask1 extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        try {
                            RA.connect("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object result) {
                        if(RA.loginRequest(user.getText().toString(),pass.getText().toString())){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        button.setText("Færdig");
                    }
                }
                AsyncTask1 a = new AsyncTask1();
                a.execute();
            }
        });
    }
}
