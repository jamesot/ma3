package com.digitalmatatus.ma3tycoon;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.digitalmatatus.ma3tycoon.MainActivity;
import com.digitalmatatus.ma3tycoon.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class ThankYou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simulateDayNight(/* DAY */ 0);
        Intent intent = new Intent(this, MainActivity.class);

        Element intentElement = new Element();
        intentElement.setIntent(intent);
        intentElement.setTitle("Click to play again!");

  /*      setContentView(R.layout.activity_thank_you);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(" ")
                .setImage(R.drawable.thank)
                .addItem(intentElement)
                .addGroup("Connect with us")
                .addEmail("digitalmatatus2017@gmail." +
                        "com")
                .addWebsite("http://www.digitalmatatus.com")
                .addFacebook("nairobidigitalmatatus")
                .addPlayStore("com.ma3tycoon.digitalmatatus")
                .create();
        setContentView(aboutPage);

    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

}

