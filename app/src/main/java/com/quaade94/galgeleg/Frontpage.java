package com.quaade94.galgeleg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Quaade94 on 15/01/2017.
 */

public class Frontpage extends Activity {

    EditText user, pass;
    Button button;
    RESTService RS = RESTService.getInstance();
    boolean connectionSuccess;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        button = (Button) findViewById(R.id.button);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        button.setText("Login");
        alertDialog = new AlertDialog.Builder(Frontpage.this).create();
        alertDialog.setTitle("Fejl");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setClickable(false);
                String input = user.getText().toString();
                    if ((input.contains("c") || input.toLowerCase().contains("s")) && (input.length() == 7 || input.length() == 3) && pass.getText() != null) {
                        Log.e("FrontActivity", "Input korrekt, starter første forbindelse");
                        button.setText("Arbejder..");
                        class AsyncTask1 extends AsyncTask {
                            @Override
                            protected Object doInBackground(Object... arg0) {
                                try {
                                    //connectionSuccess = RS.connect(user.getText().toString(), "");
                                    connectionSuccess = RS.loginRequest(user.getText().toString(), pass.getText().toString());
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object result) {
                                if (!connectionSuccess) {
                                    alertDialog.setMessage("Der kan ikke oprettes forbindelse til serveren, prøv igen senere");
                                    alertDialog.show();
                                    button.setText("Prøv igen");
                                    button.setClickable(true);
                                } else if (RS.loginFailed) {
                                    Toast errorToast = Toast.makeText(Frontpage.this, "Forkert bruger/adgangskode", Toast.LENGTH_SHORT);
                                    errorToast.show();
                                    button.setText("Login");
                                    button.setClickable(true);
                                } else {
                                    class AsyncTask2 extends AsyncTask {
                                        @Override
                                        protected Object doInBackground(Object... arg0) {
                                            try {
                                                connectionSuccess = RS.connect();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Object result) {
                                            button.setText("Færdig");
                                            Log.e("Frontpage", "Login complete, launching game");
                                            if (connectionSuccess) {
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            } else {
                                                alertDialog.setMessage("Der kan ikke oprettes forbindelse til serveren, prøv igen senere");
                                                alertDialog.show();
                                                button.setText("Prøv igen");
                                            }
                                            button.setClickable(true);
                                        }
                                    }
                                    AsyncTask2 a2 = new AsyncTask2();
                                    a2.execute();
                                }
                            }
                        }
                        AsyncTask1 a = new AsyncTask1();
                        a.execute();
                    } else {
                        alertDialog.setMessage("Brugernavn skal have formen: 'sXXXXXX' eller 'ccc'");
                        alertDialog.show();
                        button.setClickable(true);
                    }

            }
        });
    }
}
