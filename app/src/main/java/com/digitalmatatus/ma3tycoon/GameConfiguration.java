package com.digitalmatatus.ma3tycoon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalmatatus.ma3tycoon.Model.AppController;
import com.digitalmatatus.ma3tycoon.Model.MyShortcuts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameConfiguration extends MyBaseActivity {

    Button r1, r2, r3, login;
    EditText e1, e2, e3;
    JSONArray jsonArray = new JSONArray();
    ArrayList<String> route_list = new ArrayList<>();
    ArrayList<String> route_id_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_configuration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String extras = getIntent().getStringExtra("routes");
        e1 = findViewById(R.id.route_one);
        e2 = findViewById(R.id.route_two);
        e3 = findViewById(R.id.route_three);
        r1 = findViewById(R.id.btn_random_one);
        r2 = findViewById(R.id.btn_random_two);
        r3 = findViewById(R.id.btn_random_three);
        login = findViewById(R.id.btn_login);

        try {
            jsonArray = new JSONArray(extras);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject route_item = jsonArray.getJSONObject(i);
                route_list.add(route_item.getString("short_name"));
                route_id_list.add(route_item.getString("route_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e1.setText(getRandomRoute());
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e2.setText(getRandomRoute());
            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e3.setText(getRandomRoute());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getBaseContext(), Game.class);

                intent.putExtra("route1", route_id_list.get(route_list.indexOf(e1.getText().toString())));
                intent.putExtra("route2", route_id_list.get(route_list.indexOf(e2.getText().toString())));
                intent.putExtra("route3", route_id_list.get(route_list.indexOf(e3.getText().toString())));
                intent.putExtra("token", getIntent().getStringExtra("token"));

                MyShortcuts.setDefaults("route1", route_id_list.get(route_list.indexOf(e1.getText().toString())), getBaseContext());
                MyShortcuts.setDefaults("route2", route_id_list.get(route_list.indexOf(e2.getText().toString())), getBaseContext());
                MyShortcuts.setDefaults("route3", route_id_list.get(route_list.indexOf(e3.getText().toString())), getBaseContext());

                startActivity(intent);

            }
        });

    }

    private String getRandomRoute() {

        Random r = new Random();
        int i1 = r.nextInt(jsonArray.length() - 1);
        String route = null;
        try {
            route = jsonArray.getJSONObject(i1).getString("short_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return route;
    }

}
