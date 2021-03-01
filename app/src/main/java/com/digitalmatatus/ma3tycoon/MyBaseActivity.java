package com.digitalmatatus.ma3tycoon;

/**
 * Created by stephineosoro on 05/07/2017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.digitalmatatus.ma3tycoon.Interface.ServerCallback;
import com.digitalmatatus.ma3tycoon.Model.MyShortcuts;
import com.digitalmatatus.ma3tycoon.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;

import static com.digitalmatatus.ma3tycoon.utils.Utils.checkDefaults;
import static com.digitalmatatus.ma3tycoon.utils.Utils.getToken;


//METHOD USED FOR INACTIVITY LOGOUT
public class MyBaseActivity extends AppCompatActivity {
    Date curDate, resumeDate = null;
    private static long start = System.currentTimeMillis(), end = 0;
    @SuppressLint("HandlerLeak")
    public static Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {

        }
    };


    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            Log.i("RootActivity:onRun()", "******resumeDate=******" + resumeDate);
            long diff = resumeDate.getTime() - curDate.getTime();
            long secInt = diff / 1000 % 60; //conversion of milliseconds into human readable form
            Log.i("RootActivity:onRun()", "******sectInt=******" + secInt);
            if (secInt > 100) {// SET EXIT SCREEN INTERVAL LOGOUT
//                IdleLogout();
            }
        }
    };

    private void IdleLogout() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    //METHOD TO CALL ON RESETDISCONNECT WHEN USER ACTIVITY RESUMES
    public void resetDisconnectTimer() {
        MyBaseActivity.disconnectHandler.removeCallbacks(disconnectCallback);
        // TODO Repeat the task after a specified time delay
        Log.e("reset", "reset");
        MyBaseActivity.disconnectHandler.postDelayed(disconnectCallback, 3000);//10seconds
    }

    //METHOD TO CALL ON STOPDISCONNECT WHEN USER PRESS BACK BUTTON
    public void stopDisconnectTimer() {
        // TODO Stop the repeated task
        Log.e("stop", "stop");
        MyBaseActivity.disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Timer needs to be stopped when user manually pressed BACK button
        end = System.currentTimeMillis();
        long duration = end - start;
//        TODO DB enabling - Also save the activity name
//        MyShortcuts.saveEnd(getBaseContext());
        start = System.currentTimeMillis();
        long minutesDuration = (duration / 1000) / 60;

        Log.e("RootActivity:()onStop()", "******curDate=******" + duration);


        stopDisconnectTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setloginButton();

        if (resumeDate == null)
            resumeDate = new Date();
        curDate = new Date();
        //        TODO DB enabling
        start = System.currentTimeMillis();
        Log.e("RootActivity:onResume()", "******curDate=******" + curDate);
        Log.i("RootActivity:onRun()", "******resumeDate=******" + resumeDate);
        long diff = curDate.getTime() - resumeDate.getTime();


        int secInt = (int) (diff / 1000 % 60); //conversion of milliseconds into human readable form
        Log.e("RootActivity:onRun()", "******sectInt=******" + secInt);

        if (secInt > 1800) {  // SET EXIT SCREEN INTERVAL LOGOUT
            if (checkDefaults("password", getBaseContext())) {
                // if user has logged in before, we get the tokens from their saved credentials
                getToken(getBaseContext(), new ServerCallback() {
                    @Override
                    public void onSuccess(String result) {
//                    TODO remove this log - insecure
                        Log.e("jwt token reset", result);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(result);
                            String token = jsonObject.getString("token");
                            resumeDate = new Date();
                            getIntent().putExtra("token", token);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Log.e("Activity is", Objects.requireNonNull(getIntent().getComponent()).getClassName()+"");
                            }

                            Log.e("TOKEN",getIntent().getStringExtra("token"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("getting token error", "token error");
                            Intent intent = new Intent(getBaseContext(), ErrorActivity.class);
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

        resetDisconnectTimer();
    }


}


