package com.digitalmatatus.ma3tycoon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.digitalmatatus.ma3tycoon.Interface.ServerCallback;
import com.digitalmatatus.ma3tycoon.Model.Login;
import com.digitalmatatus.ma3tycoon.Model.MyShortcuts;
import com.digitalmatatus.ma3tycoon.controllers.GetData;
import com.digitalmatatus.ma3tycoon.controllers.PostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.digitalmatatus.ma3tycoon.utils.Utils.checkDefaults;
import static com.digitalmatatus.ma3tycoon.utils.Utils.getDefaults;
import static com.digitalmatatus.ma3tycoon.utils.Utils.getToken;
import static com.digitalmatatus.ma3tycoon.utils.Utils.hasInternetConnected;
import static com.digitalmatatus.ma3tycoon.utils.Utils.jwtAuthHeaders;
import static com.digitalmatatus.ma3tycoon.utils.Utils.set;
import static com.digitalmatatus.ma3tycoon.utils.Utils.setDefaults;
import static com.digitalmatatus.ma3tycoon.utils.Utils.showToast;

public class MainActivity extends MyBaseActivity {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }


    EditText _password, _username;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _password = findViewById(R.id.password);
        _username = findViewById(R.id.username);


        if (checkDefaults("username", this)) {
            _username.setText(getDefaults("username", getBaseContext()));
            _password.setText(getDefaults("password", getBaseContext()));
        }

        if (!hasInternetConnected(this)) {
            showToast("please turn on your internet connection to login", this);
            return;
        }

        if (checkDefaults("logged_in", this)) {
            startActivity(new Intent(getBaseContext(), SurveyQuestions.class));
        }

        // Automatically sign in if credentials are existing
//        signin();

        Button btn2 = findViewById(R.id.btn_login);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {
                    showToast("Please correct the above errors", getBaseContext());
                    return;
                }

                RadioGroup radioSexGroup=(RadioGroup)findViewById(R.id.radioGroup);
                int selectedId=radioSexGroup.getCheckedRadioButtonId();
                RadioButton radioButton=(RadioButton)findViewById(selectedId);
                Toast.makeText(MainActivity.this,radioButton.getText().toString(),Toast.LENGTH_SHORT).show();
                if (radioButton.getText().toString().equals("Irene")){
                    setDefaults("team","22",getBaseContext());
                } else {
                    setDefaults("team","23",getBaseContext());
                }



                //TODO remove these hardcoded credentials used for coding tests

                signin();


            }
        });

        Button btn = findViewById(R.id.register);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

//        Button survey = findViewById(R.id.survey_question);
//        survey.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getBaseContext(), SurveyQuestions.class);
//                startActivity(intent);
//            }
//        });
    }

    public boolean validate() {
        boolean valid = true;

        String password = _password.getText().toString();
        String username = _username.getText().toString();


        if (username.isEmpty()) {
            _username.setError("Enter your username");
            valid = false;
        } else {
            _username.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            _password.setError("Enter your password");
            valid = false;
        } else {
            _password.setError(null);
        }


        return valid;
    }

    private void signin() {
        set(_username.getText().toString(), _password.getText().toString(), getBaseContext());
        getToken(getBaseContext(), new ServerCallback() {
            @Override
            public void onSuccess(String result) {
//                    TODO remove this log - insecure
                Log.e("jwt token", result);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);

                    set(_username.getText().toString(), _password.getText().toString(), getBaseContext());
                    setDefaults("logged_in", "true", getBaseContext());

                    startActivity(new Intent(getBaseContext(), SurveyQuestions.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("getting token error", "token error");

                    showToast("Please enter the correct credentials", getBaseContext());

                    Intent intent = new Intent(getBaseContext(), Login.class);
                    intent.putExtra("error", e.toString());
                    startActivity(intent);
                }

            }

            @Override
            public void onSuccess(JSONObject response) {

            }
        });
    }

   /* private void signin() {
        if (checkDefaults("password", getBaseContext())) {
            getToken(getBaseContext(), new ServerCallback() {
                @Override
                public void onSuccess(String result) {
//                    TODO remove this log - insecure
                    Log.e("jwt token", result);
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(result);
                        String token = jsonObject.getString("token");

                        resumeDate = new Date();
                        getLeaderBoard(token);
                        getRoutes(token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("getting token error", "token error");

                        showToast("Please enter the correct credentials", getBaseContext());

                        Intent intent = new Intent(getBaseContext(), Login.class);
                        intent.putExtra("error", e.toString());
                        startActivity(intent);
                    }

                }

                @Override
                public void onSuccess(JSONObject response) {

                }
            });
        }
    }*/

    private void getRoutes(final String token) {
        GetData getData = new GetData(getBaseContext());
        getData.online_data("routes/", null, jwtAuthHeaders(token), new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        Log.e("routes", jsonArray.toString());
                        Intent intent = new Intent(getBaseContext(), GameConfiguration.class);
                        intent.putExtra("routes", jsonArray.toString());
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("e", e.toString());
                }
            }

            @Override
            public void onSuccess(JSONObject response) {

            }
        });
    }

    private void getLeaderBoard(final String token) {
        GetData getData = new GetData(getBaseContext());
        getData.online_data("questions/leaderboard/", null, jwtAuthHeaders(token), new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.e("json", jsonObject.toString());

//                    {"leaderboard":[{"transit":10,"trivia":0,"username":"test","rank":1,"total":10}],"my_score":[{"transit":10,"trivia":0,"username":"test","rank":1,"total":10}],"success":true}
                  /*  if (jsonObject.getString("success").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        Log.e("routes", jsonArray.toString());
                        Intent intent = new Intent(getBaseContext(), GameConfiguration.class);
                        intent.putExtra("routes", jsonArray.toString());
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("e", e.toString());
                }
            }

            @Override
            public void onSuccess(JSONObject response) {

            }
        });
    }

}
