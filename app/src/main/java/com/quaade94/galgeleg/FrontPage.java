package com.quaade94.galgeleg;

import android.app.ListActivity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.xml.namespace.QName;

/**
 * Created by Quaade94 on 15/01/2017.
 */

public class FrontPage extends ListActivity {

    TextView title, scoreView;
    ListView list;
    Button button;
    private String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frontpage);

        title = (TextView) findViewById(R.id.title);
        scoreView = (TextView) findViewById(R.id.score);
        list = (ListView) findViewById(android.R.id.list);
        button = (Button) findViewById(R.id.button);

        score = "";
        scoreView.setText("Score: " + score);

        URL url = null;
        try {
            url = new URL("http://ubuntu4.saluton.dk:9924/galgeleg?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        QName qname = new QName("http://galgeleg/", "GalgelogikService");
        Service service = Service.create(url, qname);

        GalgeI g = service.getPort(GalgeI.class);

        Galgelogik spil = new Galgelogik();
        spil.nulstil();
        spil.hentOrdFraDr();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setText("Arbejder");
                scoreView.setText("Score: " + score);
                class AsyncTask1 extends AsyncTask {
                    @Override
                    protected Object doInBackground(Object... arg0) {
                        try {
                            hentOrd();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        button.setText("Færdig");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(),  android.R.layout.simple_list_item_1, muligeOrd);
                        getListView().setAdapter(adapter);
                    }
                }
                AsyncTask1 a = new AsyncTask1();
                a.execute();
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                String  itemValue    = (String) list.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    public void hentOrd() throws Exception {

        String request = SoapObject();

    }

    public static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }

}
