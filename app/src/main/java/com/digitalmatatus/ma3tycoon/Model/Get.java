package com.digitalmatatus.ma3tycoon.Model;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

//import org.apache.http.client.HttpClient;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.digitalmatatus.ma3tycoon.Interface.VolleyCallback;

/**
 * Created by stephineosoro on 30/03/2018.
 */

public class Get {


    public static void getResponse(int method, final String url, JSONObject jsonValue, final Context mCtx, final Map<String, String> parameters, final Map<String, String> headers, final VolleyCallback callback) {


        StringRequest req = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String Response) {
                callback.onSuccessResponse(Response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toast.makeText(mCtx, e + "error", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params
                Map<String, String> params = new HashMap<String, String>();
                 params = parameters;

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();

                setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
                setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
                header = headers;

                return header;
            }

        };
        AppController.getInstance().addToRequestQueue(req);
        Log.e("request", req.toString());

    }
}
