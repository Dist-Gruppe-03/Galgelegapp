package com.quaade94.galgeleg;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Quaade94 on 15/01/2017.
 */

public class Frontpage extends ListActivity {

    TextView title, scoreView;
    ListView list;
    Button button;
    private String score;
    RESTAPI RA = new RESTAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        title = (TextView) findViewById(R.id.title);
        scoreView = (TextView) findViewById(R.id.score);
        button = (Button) findViewById(R.id.button);

        score = "";
        scoreView.setText("Score: " + score);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Activtiy","STARTER");
                button.setText("Arbejder");
                RA.connect();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }
}
