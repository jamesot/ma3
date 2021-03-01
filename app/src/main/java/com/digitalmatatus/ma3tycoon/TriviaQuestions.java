package com.digitalmatatus.ma3tycoon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*import com.digitalmatatus.ma3tycoon.Model.MyShortcuts;
import com.digitalmatatus.ma3tycoon.Model.Post2*/;
import com.android.volley.Response;
import com.digitalmatatus.ma3tycoon.Model.MyShortcuts;
import com.digitalmatatus.ma3tycoon.Model.Post2;
import com.joooonho.SelectableRoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;


public class TriviaQuestions extends AppCompatActivity {


    private Uri.Builder b;
    private SelectableRoundedImageView selectableRoundedImageView;
    private Bitmap bm;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> answer = new ArrayList<>();
    ArrayList<String> link = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    private static int questionNumber = 0, total = 0;

    LinearLayout layout;
    LinkedList<Button> buttons = new LinkedList<Button>();
    ;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        layout = (LinearLayout) findViewById(R.id.layoutDescription);
        tv = (TextView) findViewById(R.id.question);
        selectableRoundedImageView = (SelectableRoundedImageView) findViewById(R.id.image);
        getTrivia();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "No email specified yet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void getTrivia() {
        Post2.getData(MyShortcuts.baseURL() + "twiga/game/triviaQuestions", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("questions");
                    String urlLink = null, path = null;
                    LinearLayout layout = (LinearLayout) findViewById(R.id.layoutDescription);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        urlLink = "'http://41.204.186.47:8000/twiga/trivia_photos/" + jsonObject1.getString("id") + ".jpg";
                        Log.e("url", urlLink);
                        String question = jsonObject1.getString("title");
                        String answers = jsonObject1.getString("answers");

                        id.add(jsonObject1.getString("id"));
                        link.add(urlLink);
                        title.add(question);
                        answer.add(answers);


                        /*path = "/twiga/trivia_photos/" + jsonObject1.getString("id") + ".jpg";
                        String[] arr = answers.split(",");
                        System.out.println(arr.length + " " + arr[0]);

                        for (int j = 0; j < arr.length; j++) {
                            System.out.println(arr[j]);
                            String[] choice = arr[j].split("\\|");

                            System.out.println(choice[0] + " " + choice[1]);
                            System.out.println(choice.length + "choice length");
                            System.out.println(arr.length + " " + choice[0]);

                            Button btnTag = new Button(getBaseContext());
//                        btnTag.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            btnTag.setText(choice[1]);
                            btnTag.setId(Integer.parseInt(choice[0]));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(10, 5, 10, 5);
                            btnTag.setLayoutParams(params);


                            layout.addView(btnTag);
                        }
*/
                    }

                    setData(0);

                  /*  b = Uri.parse("http://41.204.186.47:8000").buildUpon();
                    b.path(path);
                    final String url = b.build().toString();
                    Log.e("url", url);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final Bitmap bm = getImageBitmap(url);

                            // Do network action in this function
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    selectableRoundedImageView.setImageBitmap(bm);
                                }

                            });


                        }
                    }).start();
*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Bitmap error", "Error getting bitmap", e);
        }
        return bm;
    }


    private void setData(final int question) {
        if (questionNumber < title.size()) {
//        setTitle(title.get(question));

        tv.setText(title.get(question));

//        Setting up the image
        b = Uri.parse("http://41.204.186.47:8000").buildUpon();
        String path = "/twiga/trivia_photos/" + id.get(question) + ".jpg";
        b.path(path);
        final String url = b.build().toString();
        Log.e("url", url);

        new Thread(new Runnable() {
            @Override
            public void run() {

                final Bitmap bm = getImageBitmap(url);

                // Do network action in this function
                runOnUiThread(new Runnable() {
                    public void run() {
                        selectableRoundedImageView.setImageBitmap(bm);
                    }

                });


            }
        }).start();

//        Setting up the choices
        String[] arr = answer.get(question).split(",");

        for (int j = 0; j < arr.length; j++) {
            System.out.println(arr[j]);
            String[] choice = arr[j].split("\\|");


            Button btnTag = new Button(getBaseContext());
//                        btnTag.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btnTag.setText(choice[1]);
            btnTag.setId(Integer.parseInt(choice[0]));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(10, 5, 10, 5);
            btnTag.setLayoutParams(params);

            btnTag.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("TAG", "The id is " + v.getId());
                    String[] arr = answer.get(question).split(",");

//                    Removing already set up buttons
                    for (int i = 0; i < arr.length; i++) {
                        String[] choice = arr[i].split("\\|");
                        Button btn;
                        btn = buttons.get(i);
                        layout.removeView(btn);
                    }
                    questionNumber=questionNumber+1;
                    if (questionNumber <= title.size()) {
                        setData(questionNumber);

                    }
                }
            });


                buttons.add(btnTag);
                layout.addView(btnTag);
            }
        }else{
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
        }

    }


//    private void


//                    LinearLayout layout = (LinearLayout) findViewById(R.id.layoutDescription);
                    /*layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"

                    for (int i = 0; i < jsonArray.length(); i++) {
                        LinearLayout row = new LinearLayout(getBaseContext());
                        row.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT));

                        for (int j = 0; j < 4; j++ ){
                            Button btnTag = new Button(getBaseContext());
                            btnTag.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
                            btnTag.setText("Button " + (j + 1 + (i * 4)));
                            btnTag.setId(j + 1 + (i * 4));
                            row.addView(btnTag);
                        }

                        layout.addView(row);
                    }*/

    //the layout on which you are working

                    /*for (int j = 0; j < 4; j++) {
                        Button btnTag = new Button(getBaseContext());
//                        btnTag.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        btnTag.setText("Button");

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(10, 5, 10, 5);
                        btnTag.setLayoutParams(params);

                        btnTag.setId(j + 1 + (20 * 4));
                        layout.addView(btnTag);
                    }*/
    //set the properties for button


    //add button to the layout







                   /* Uri uri =  Uri.parse("https://www.thegreenage.co.uk/wp-content/uploads/2014/06/Mechanical_electricity_meter_1965_1-780x350.jpg");
                    selectableRoundedImageView.setImageURI(uri);*/


                         /*SelectableRoundedImageView sriv = new SelectableRoundedImageView(getBaseContext());
                            sriv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            sriv.setCornerRadiiDP(4, 4, 0, 0);
                            sriv.setBorderWidthDP(4);
                            sriv.setMaxHeight(250);
                            sriv.setBorderColor(Color.BLUE);
                            sriv.setImageBitmap(getImageBitmap(url));
                            sriv.setOval(true);*/


}
