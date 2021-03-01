package com.digitalmatatus.ma3tycoon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.digitalmatatus.ma3tycoon.Interface.ServerCallback;
import com.digitalmatatus.ma3tycoon.Model.Login;
import com.digitalmatatus.ma3tycoon.Model.MyShortcuts;
import com.digitalmatatus.ma3tycoon.controllers.GetData;
import com.digitalmatatus.ma3tycoon.controllers.PostData;
import com.digitalmatatus.ma3tycoon.utils.Utils;
import com.google.android.gms.location.DetectedActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

import static com.digitalmatatus.ma3tycoon.utils.Utils.checkDefaults;
import static com.digitalmatatus.ma3tycoon.utils.Utils.getDefaults;
import static com.digitalmatatus.ma3tycoon.utils.Utils.getToken;
import static com.digitalmatatus.ma3tycoon.utils.Utils.jwtAuthHeaders;
import static com.digitalmatatus.ma3tycoon.utils.Utils.set;
import static com.digitalmatatus.ma3tycoon.utils.Utils.showPermissionDialog;
import static com.digitalmatatus.ma3tycoon.utils.Utils.showToast;


public class SurveyQuestions extends AppCompatActivity implements OnLocationUpdatedListener, OnActivityUpdatedListener {


    private Uri.Builder b;
    private Bitmap bm;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> answer = new ArrayList<>();
    ArrayList<Integer> answer_id = new ArrayList<>();
    ArrayList<String> other_list = new ArrayList<>();
    ArrayList<Integer> other_list_id = new ArrayList<>();
    ArrayList<Integer> answered_other_list_id = new ArrayList<>();
    ArrayList<Integer> choice_id = new ArrayList<>();
    ArrayList<String> choice_text = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    private static int questionNumber = 0, total = 0;
    private static Location last_location;
    private static final int REQUEST_FINE_LOCATION = 0;


    LinearLayout layout;
    LinkedList<Button> buttons = new LinkedList<Button>();
    ;
    TextView tv;
    protected static LocationGooglePlayServicesProvider provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_questions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Survey");
        layout = findViewById(R.id.layoutDescription);
        tv = findViewById(R.id.question);

        if (Utils.checkPermission(getBaseContext())) {
            startLocation();
            Log.e("picking location", "location");
        } else {
            showPermissionDialog(this);
        }
//        sendSurvey();
//  TODO      Pick location attribute
        signin();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendSurvey();
            }
        });


    }


    private void signin() {
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

                        GetData qRequest = new GetData(getBaseContext());

                        qRequest.online_data("questions/trivia", null, jwtAuthHeaders(token), new ServerCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("result string", result);
                                try {
                                    JSONObject jsonObject1 = new JSONObject(result);
                                    JSONArray questions = jsonObject1.getJSONArray("result");
                                    setData(questions);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onSuccess(JSONObject response) {
                                Log.e("result,", response.toString());
                            }
                        });


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
    }


    private void setData(JSONArray data) {

        for (int i = 0; i < data.length(); i++) {

            try {
                JSONObject item = data.getJSONObject(i);

                if (item.getInt("persona") == 21 || Integer.parseInt(getDefaults("team", getBaseContext())) == item.getInt("persona")) {
                    TextView tv = new TextView(getBaseContext());
                    tv.setText(item.getString("question_text"));
                    tv.setTextColor(Color.BLACK);
                    tv.setTextSize(20);
                    tv.setId(item.getInt("id"));
//                    Log.e("item id", item.getInt("id") + "");
                    layout.addView(tv);

//                Iterating through the choices
                    final JSONArray choices = item.getJSONArray("choices");
                    RadioGroup group = new RadioGroup(this);
                    group.setOrientation(RadioGroup.VERTICAL);
                    final RadioButton[] btnTag = new RadioButton[choices.length()];

                    for (int j = 0; j < choices.length(); j++) {

                        JSONObject choice = choices.getJSONObject(j);

//                    RadioButton btnTag = new RadioButton(this);
                        btnTag[j] = new RadioButton(this);
                        btnTag[j].setId(choice.getInt("id"));
                        btnTag[j].setText(item.getInt("id") + ": " + choice.getString("choice_text"));

                        if (choice.getString("choice_text").equals("Please specify below")) {
                            btnTag[j].setChecked(true);
                            other_list_id.add(item.getInt("id"));
                        }
//                    btnTag.setOnClickListener(this);

                   /* final Button btnTag = new Button(getBaseContext());
                    btnTag.setText(item.getInt("id") + ": " + choice.getString("choice_text"));
                    btnTag.setId(choice.getInt("id"));*/
//                        Log.e("choice id", choice.getInt("id") + "");


                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(10, 5, 10, 5);
                        btnTag[j].setLayoutParams(params);
                        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            public void onCheckedChanged(RadioGroup group, int checkedId) {

                                for (int i = 0; i < choices.length(); i++) {
                                    btnTag[i].setChecked(false);
                                    int index = answer.indexOf(btnTag[i].getText().toString());

                                    if (index != -1) {
                                        Log.e("index", index + "");
                                        choice_id.remove(index);
                                        answer.remove(index);
                                        choice_text.remove(index);
                                    }

                                }
                                //
                                Log.v("id", "" + checkedId);
                                for (int i = 0; i < choices.length(); i++) {
                                    if (btnTag[i].getId() == checkedId) {
                                        btnTag[i].setChecked(true);
                                        String[] arr = btnTag[i].getText().toString().split(":");
                                        choice_id.add(btnTag[i].getId());
                                        choice_text.add(arr[1]);
                                        answer.add(btnTag[i].getText().toString());
                                    }
                                }
                            }
                        });
                        group.addView(btnTag[j]);

                    }

                    layout.addView(group);
                    final EditText other = new EditText(getBaseContext());
                    other.setId(item.getInt("id"));
                    other.setHint("Type to specify");
                    other.setHintTextColor(Color.parseColor("#A9A9A9"));
                    other.setTextColor(Color.BLACK);
                    other.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() != 0)
                                other_list.add(s.toString() + ":" + other.getId());
                            if (!answered_other_list_id.contains(other.getId())) {
                                answered_other_list_id.add(other.getId());
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    layout.addView(other);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendSurvey() {
        if (checkDefaults("password", getBaseContext())) {
            getToken(getBaseContext(), new ServerCallback() {
                @Override
                public void onSuccess(String result) {
//                    TODO remove this log - insecure
                    Log.e("jwt token", result);
                    JSONObject jsonObject;


                    StringBuilder sb = new StringBuilder();
                    ListIterator<Integer> it = answered_other_list_id.listIterator(answered_other_list_id.size());
                    while (it.hasPrevious()) {
                        sb.append(Integer.toString(it.previous()));
                    }
                    StringBuilder sb2 = new StringBuilder();
                    ListIterator<Integer> it2 = other_list_id.listIterator(other_list_id.size());
                    while (it2.hasPrevious()) {
                        sb2.append(Integer.toString(it2.previous()));
                    }

                    System.out.println(sb.toString());
                    System.out.println(sb2.toString());

//                    if (!sb.toString().equals(sb2.toString())) {
                    if (!answered_other_list_id.containsAll(other_list_id)) {
                        MyShortcuts.showToast("Please fill all the open ended questions", getBaseContext());
                        return;
                    }


                    try {
                        jsonObject = new JSONObject(result);
                        String token = jsonObject.getString("token");

                        JSONObject params = new JSONObject();
                        JSONArray jsonArray = new JSONArray();

                        //    {"data": [{ "question": 1, "choice": 1 }, { "question": 4, "choice": 10 } ] }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Log.e("answer", String.join(", ", answer));
                        }


                        for (int i = 0; i < answer.size(); i++) {
                            JSONObject item = new JSONObject();
                            String[] arr = answer.get(i).split(":");
                            String choice_text = arr[1].substring(1);
                            item.put("other", "NA");
                            boolean found = false;
                            answer_id.add(Integer.parseInt(arr[0]));

//                             other_list_id - ids of specify;
//


                            for (int j = 0; j < other_list.size(); j++) {
                                String[] array = other_list.get(j).split(":");

//                                if (answered_other_list_id.contains(Integer.parseInt(arr[0]))) {
//                                    item.put("other", array[0]);
//                                    found = true;
//                                }

                                if ((arr[0]).equals(array[1])) {
                                    item.put("other", array[0]);
                                    found = true;
                                }
                            }

                            System.out.println(arr[1] + " and " + arr[0]);
                            item.put("question", arr[0]);
                            item.put("choice_text", choice_text);
                            item.put("choice_id", choice_id.get(i));
                            item.put("latitude", "NA");
                            item.put("longitude", "NA");

//                            if (choice)

                            if (last_location != null) {
                                item.put("latitude", last_location.getLatitude());
                                item.put("longitude", last_location.getLongitude());
                            }

                            jsonArray.put(item);
                        }


                        params.put("data", jsonArray);

                        Log.e("sent", params.toString());

                        if ((jsonArray.length() != 16 && Integer.parseInt(getDefaults("team", getBaseContext())) == 23) || (jsonArray.length() != 19 && Integer.parseInt(getDefaults("team", getBaseContext())) == 22)) {
                            MyShortcuts.showToast("Please fill all the questions!", getBaseContext());
                            return;
                        }


                        PostData postData = new PostData(getBaseContext());
                        postData.post("questions/trivia/", null, params, jwtAuthHeaders(token), new ServerCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("result data", result);
                            }

                            @Override
                            public void onSuccess(JSONObject response) {
                                Log.e("result data json", response.toString());
                                choice_id.clear();
                                answer.clear();
                                showToast("Data uploaded successfully", getBaseContext());

                                Intent intent = new Intent(getBaseContext(), SurveyQuestions.class);
                                startActivity(intent);
                            }
                        });


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
    }

    protected void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);


    }

    @Override
    public void onLocationUpdated(Location location) {
        last_location = location;
        final String text = String.format("Latitude %.6f, Longitude %.6f",
                location.getLatitude(),
                location.getLongitude());
        Log.e("text loc", text);
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted

                    if (Utils.hasInternetConnected(getBaseContext())) {
                        startLocation();
                        Log.e("picking location", "location");
                    }

                } else {
                    // not granted
                    Utils.showToast("You cannot get geo coordinates for the survey without accepting this permission!", getBaseContext());
//                    showPermissionDialog();
                }
                return;
            }

        }

    }
}
