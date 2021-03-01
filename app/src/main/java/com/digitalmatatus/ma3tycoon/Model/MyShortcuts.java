package com.digitalmatatus.ma3tycoon.Model;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalmatatus.ma3tycoon.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by stephineosoro on 07/06/16.
 */
public class MyShortcuts {

    public static String baseURL() {
        return "http://41.204.186.47:8000/";
    }

    public static void set(String username, String password, Context context) {
        setDefaults("username", username, context);
        setDefaults("password", password, context);
    }


    public static HashMap<String, String> AunthenticationHeaders(Context context) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String creds = String.format("%s:%s", "web@oneshoppoint.com", "spr0iPpQAiwS8u");
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        headers.put("Authorization", auth);
        return headers;
    }

    public static HashMap<String, String> AunthenticationHeadersAdmin(Context context) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        String creds = String.format("%s:%s", getDefaults("username", context), getDefaults("password", context));
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        headers.put("Authorization", auth);
        return headers;
    }


    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }


    public static void showToast(String text, Context context) {

        /*Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();*/

        Toast ToastMessage = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        View toastView = ToastMessage.getView();
//        toastView.setBackgroundResource(R.drawable.toast_background_color);
        ToastMessage.show();
    }

    public static boolean hasInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    public static Boolean checkDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(key);
    }

    public static void Delete(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    // 41.204.186.47:8000/twiga/reports/addImpressions
    public static void save(Context context) {

        if (SessionEnded("Impressions")) {
            Impressions impressions = new Impressions();
            impressions.setDate(System.currentTimeMillis() + "");
            impressions.setSessionStart(System.currentTimeMillis() + "");
            impressions.setImpression("");
            impressions.save();
        } else {
//           TODO Only store the sessionEnd time to the last record
//            Getting id of the last record
            List<String> stringList = new ArrayList<String>();
            Cursor cursor = null;

            try {
                cursor = Connector.getDatabase().rawQuery("select * from impressions order by id DESC LIMIT 1",
                        null);
                if (cursor.getCount() < 1) {
                    Log.e("No data", "No data");

                    // MyShortcuts.showToast("You don't have any favorite paybill. Once you add a favoirte it would appear here", context);
                }

                if (cursor.moveToFirst()) {
                    do {


                        String id = cursor.getString(cursor.getColumnIndex("id"));
                        saveWithID(id);

                    } while (cursor.moveToNext());
                    Log.e("StringList", stringList.toString() + stringList.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
//            Updating local database based on that id
            }

        }
    }

    private static void saveWithID(String id) {

        Impressions impressions = new Impressions();
        impressions.setSessionEnd(System.currentTimeMillis() + "");
        impressions.update(Long.parseLong(id));
    }

    private void upload(Context context) {
//        String imei, duration, useDate, appName, impression;
        // TODO Functions for getting data
        Utils.getMACAddress("wlan0");
        Utils.getMACAddress("eth0");
        Utils.getIPAddress(true); // IPv4
        String imei = getImei(context);
        String ip = Utils.getIPAddress(false); // IPv6

    }

    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String uuid = UUID.randomUUID().toString();
            MyShortcuts.setDefaults("device_uuid", uuid, context);

            return uuid;
        }
        return telephonyManager.getDeviceId();
    }

    private void getLastID() {
        List<String> stringList = new ArrayList<String>();
        Cursor cursor = null;

        try {
            cursor = Connector.getDatabase().rawQuery("select * from impressions order by id DESC LIMIT 1",
                    null);
            if (cursor.getCount() < 1) {
                Log.e("No data", "No data");

                // MyShortcuts.showToast("You don't have any favorite paybill. Once you add a favoirte it would appear here", context);
            }

            if (cursor.moveToFirst()) {
                do {


                    String impression = cursor.getString(cursor.getColumnIndex("impression"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String sessionEnd = cursor.getString(cursor.getColumnIndex("sessionEnd"));
                    String sessionStart = cursor.getString(cursor.getColumnIndex("sessionStart"));


                    stringList.add(impression);

                } while (cursor.moveToNext());
                Log.e("StringList", stringList.toString() + stringList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
/*
            mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

            CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }*/
        }
    }


    private void getSqlite(String table_name, Context context) {
        List<String> stringList = new ArrayList<String>();
        Cursor cursor = null;

        try {
            cursor = Connector.getDatabase().rawQuery("select * from " + table_name + " order by id",
                    null);
            if (cursor.getCount() < 1) {
                Log.e("No data", "No data");

                // MyShortcuts.showToast("You don't have any favorite paybill. Once you add a favoirte it would appear here", context);
            }

            if (cursor.moveToFirst()) {
                do {


                    String impression = cursor.getString(cursor.getColumnIndex("impression"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    String sessionEnd = cursor.getString(cursor.getColumnIndex("sessionEnd"));
                    String sessionStart = cursor.getString(cursor.getColumnIndex("sessionStart"));


                    stringList.add(impression);

                } while (cursor.moveToNext());
                Log.e("StringList", stringList.toString() + stringList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
/*
            mCardArrayAdapter = new CardGridArrayAdapter(getBaseContext(), cards);

            CardGridView listView = (CardGridView) findViewById(R.id.carddemo_grid_base1);
            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }*/
        }
    }


    //    TODO if sessionEnded false means session did not end so append session end to complete the record
    private static boolean SessionEnded(String table_name) {
        Cursor cursor = null;
        Boolean ret = false;

        try {
//            + " where name='" + name + "'
            cursor = Connector.getDatabase().rawQuery("select sessionEnd from " + table_name + " order by id DESC LIMIT 1",
                    null);
            if (cursor.getCount() < 1) {

                Log.e("data", "No data");

                ret = true;
            } /*else {
                ret = false;
            }
*/
            if (cursor.moveToFirst()) {
                do {
                    String sessionEnd = cursor.getString(cursor.getColumnIndex("sessionEnd"));
                    if (sessionEnd.isEmpty()) {
                        ret = false;
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return ret;
    }

    public static void Test() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonObject1.put("imei", "test");
            jsonObject1.put("appName", "test");
            jsonObject1.put("impression", "test");
            jsonObject1.put("duration", "test");
            jsonObject1.put("useDate", "test");


            jsonArray.put(jsonObject1);
            jsonObject.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("send", jsonObject.toString());

        Post2.PostData("/impressions/add", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());
            }
        });
    }

//        Post2.PostString("/impressions/add","");

    public static void TestUrl() {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                MyShortcuts.baseURL() + "twiga/impressions/add/" + "imei/1212/appName/23232/impression/1223/duration/2332/useDate/12-12-12", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("All Data", "response from the server is: " + response.toString());


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Getting data error group", "Error: " + error.getMessage());


            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }

        };

        Log.e("str", strReq.toString());
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public static void saveEnd(Context context) {
        List<String> stringList = new ArrayList<String>();
        Cursor cursor = null;

        try {
            cursor = Connector.getDatabase().rawQuery("select * from impressions order by id DESC LIMIT 1",
                    null);
            if (cursor.getCount() < 1) {
                Log.e("No data", "No data");

                // MyShortcuts.showToast("You don't have any favorite paybill. Once you add a favoirte it would appear here", context);
            }

            if (cursor.moveToFirst()) {
                do {


                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    saveWithID(id);

                } while (cursor.moveToNext());
                Log.e("StringList", stringList.toString() + stringList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
//            Updating local database based on that id
        }
    }


//    Getting ip address, call them in the following ways
// Get all local data and upload it to the server


    //TODO Call this function to upload all impressions to the database and truncate the local database
    public static void upload(String impression, String date, String sessionEnd, String sessionStart, Context context) {
        if (hasInternetConnected(context)) {
            getAllImpressions(impression, date, sessionEnd, sessionStart);

        } else {
            MyShortcuts.showToast("Cannot backup the impressions at the moment", context);
        }
    }

    private static boolean getAllImpressions(String impression, String date, String sessionEnd, String sessionStart) {
        Cursor cursor = null;
        Boolean ret = false;

        try {
//            + " where name='" + name + "'
            cursor = Connector.getDatabase().rawQuery("select * from impressions order by id DESC",
                    null);
            if (cursor.getCount() < 1) {

                Log.e("data", "No data");


                ret = true;
            } /*else {
                ret = false;
            }
*/
            if (cursor.moveToFirst()) {
                do {

                    impression = cursor.getString(cursor.getColumnIndex("impression"));
                    date = cursor.getString(cursor.getColumnIndex("date"));
                    sessionEnd = cursor.getString(cursor.getColumnIndex("sessionEnd"));
                    sessionStart = cursor.getString(cursor.getColumnIndex("sessionStart"));

                    UploadAllImpressions(impression, date, sessionEnd, sessionStart);


                    if (sessionEnd.isEmpty()) {
                        ret = false;
                    }
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            truncateImpressions();
            if (cursor != null) {
                cursor.close();

            }
        }

        return ret;
    }

    private static void UploadAllImpressions(String impression, String date, String sessionEnd, String sessionStart) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            jsonObject1.put("impression", impression);
            jsonObject1.put("date", date);
            jsonObject1.put("sessionStart", sessionStart);
            jsonObject1.put("sessionEnd", sessionEnd);
            jsonArray.put(jsonObject1);
            jsonObject.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("send", jsonObject.toString());

        Post2.PostData("/impressions/add", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response.toString());
            }
        });
    }


    public static void truncateImpressions() {
        Cursor cursor = null;
        Cursor cursor2 = null;
        Boolean ret = false;

        try {
            cursor = Connector.getDatabase().rawQuery("Delete from impressions",
                    null);
            cursor2 = Connector.getDatabase().rawQuery("DELETE FROM SQLITE_SEQUENCE WHERE name='paybill'",
                    null);
            Log.e("delete row count", cursor.getCount() + " 1 and 2 is  " + cursor2.getCount());
            if (cursor.getCount() < 1) {

                Log.e("data", "No data");
                ret = true;

            } else {
                ret = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        }
    }


}
