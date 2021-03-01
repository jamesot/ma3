package com.digitalmatatus.ma3tycoon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.digitalmatatus.ma3tycoon.Interface.ServerCallback;
import com.digitalmatatus.ma3tycoon.Model.MyShortcuts;
import com.digitalmatatus.ma3tycoon.controllers.GetData;
import com.digitalmatatus.ma3tycoon.controllers.PostData;
import com.digitalmatatus.ma3tycoon.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.digitalmatatus.ma3tycoon.utils.Utils.checkDefaults;
import static com.digitalmatatus.ma3tycoon.utils.Utils.getToken;
import static com.digitalmatatus.ma3tycoon.utils.Utils.jwtAuthHeaders;
import static com.digitalmatatus.ma3tycoon.utils.Utils.setDefaults;

public class RegisterActivity extends MyBaseActivity {

    EditText _password, _confirm, _email, _username;
    Button _signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final String token = getIntent().getStringExtra("token");

        _password = findViewById(R.id.password);
        _confirm = findViewById(R.id.confirm_password);
        _email = findViewById(R.id.email);
        _username = findViewById(R.id.username);
        _signupButton = findViewById(R.id.btn_register);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        Map<String, String> credentials = new HashMap<String, String>();

        credentials.put("username", _username.getText().toString());
        credentials.put("email", _email.getText().toString());
        credentials.put("password", _password.getText().toString());

        PostData postData = new PostData(getBaseContext());
        postData.post("auth/users/create/", credentials, null, null, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("response", result);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);


                    if (jsonObject.getString("id") != null) {
                        Utils.setDefaults("username", _username.getText().toString(), getBaseContext());
                        Utils.setDefaults("password", _password.getText().toString(), getBaseContext());
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Utils.showToast("Our server is busy, try again later", getBaseContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("getting token error", "token error");
                    Intent intent = new Intent(getBaseContext(), ErrorActivity.class);
                    intent.putExtra("error", e.toString());
                }
            }

            @Override
            public void onSuccess(JSONObject response) {

            }
        });
    }


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
                        resumeDate = new Date();
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

    public boolean validate() {
        boolean valid = true;

        String password = _password.getText().toString();
        String confirm = _confirm.getText().toString();
        String email = _email.getText().toString();
        String username = _username.getText().toString();


        if (username.isEmpty()) {
            _username.setError("Input a password");
            valid = false;
        } else {
            _username.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            _password.setError("Input a strong password");
            valid = false;
        } else {
            _password.setError(null);
        }
        if (confirm.isEmpty() || !confirm.equals(password)) {
            _confirm.setError("Passwords do not match");
            valid = false;
        } else {
            _confirm.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("input a valid email");
            valid = false;
        } else {
            _email.setError(null);
        }


        return valid;
    }

    public void onSignupFailed() {
        Utils.showToast("Please correct the above errors", getBaseContext());
        _signupButton.setEnabled(true);
    }

}
