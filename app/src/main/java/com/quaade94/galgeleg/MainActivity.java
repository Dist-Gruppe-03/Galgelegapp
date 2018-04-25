package com.quaade94.galgeleg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView wrongLetter, correctLetter;
    EditText input;
    ImageView image;
    Button guessButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input_view);
        guessButton = (Button) findViewById(R.id.guess_button);
        backButton = (Button) findViewById(R.id.back_button);
        wrongLetter = (TextView) findViewById(R.id.wrongLetter_view);
        correctLetter = (TextView) findViewById(R.id.correctLetter_view);
        image = (ImageView) findViewById(R.id.image_view);

        updateView();

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateView();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Frontpage.class));
            }
        });
    }

    private void updateView(){
        input.setText("");
        //TODO: insert strings
        wrongLetter.setText("");
        correctLetter.setText("");
        //TODO: insert correct value
        switch (0) {
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

