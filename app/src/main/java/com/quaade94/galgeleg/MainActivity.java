package com.quaade94.galgeleg;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView wrongLetter, correctLetter, response, name;
    EditText input;
    ImageView image;
    Button guessButton;
    RESTAPI RA = RESTAPI.getInstance();
    private final long PERIOD = 1000L;
    private long lastTime = System.currentTimeMillis()-PERIOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input_view);
        guessButton = (Button) findViewById(R.id.guess_button);
        name = (TextView) findViewById(R.id.name);
        response = (TextView) findViewById(R.id.response);
        wrongLetter = (TextView) findViewById(R.id.wrongLetter_view);
        correctLetter = (TextView) findViewById(R.id.correctLetter_view);
        image = (ImageView) findViewById(R.id.image_view);

        long thisTime = System.currentTimeMillis();
        if((thisTime -lastTime) >= PERIOD){
            lastTime = thisTime;

            updateView();

        }
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(RA.getGameOver()){
                    RA.resetGame();
                }else{
                    RA.guessLetter(input.getText().toString());
                }

                    updateView();


            }
        });

    }

    private void updateView(){
        if(RA.getGameOver()){
            guessButton.setText("NYT SPIL");
            guessButton.setBackgroundColor(Color.BLACK);
        }else{
            guessButton.setText("GÆT!");
            guessButton.setBackgroundColor(Color.GREEN) ;
        }
        input.setText("");
        name.setText(RA.getUserID() + " " + RA.getName());
        response.setText(RA.getResponse());
        wrongLetter.setText(RA.usedLetters);
        correctLetter.setText(RA.getinvisibleWord());
        switch (RA.getWrongLetters()) {
            case 0:
                image.setImageResource(R.mipmap.galge);
                break;
            case 1:
                image.setImageResource(R.mipmap.forkert1);
                break;
            case 2:
                image.setImageResource(R.mipmap.forkert2);
                break;
            case 3:
                image.setImageResource(R.mipmap.forkert3);
                break;
            case 4:
                image.setImageResource(R.mipmap.forkert4);
                break;
            case 5:
                image.setImageResource(R.mipmap.forkert5);
                break;
            case 6:
                image.setImageResource(R.mipmap.forkert6);
                break;
        }
    }
}

