package com.connectutb.xfuel.tools;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zygote on 24.11.2015.
 */
public class ICAOManager {
    Context context;
    private final static String ICAO_API_URL = "http://www.airport-data.com/api/ap_info.json?icao=";

    public ICAOManager(Context context) {
        this.context = context;
    }

    public void icaoToName(String icao, final boolean isDeparture) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ICAO_API_URL + icao,
                new Response.Listener() {


                    @Override
                    public void onResponse(Object o) {
                        try {
                            JSONObject obj = new JSONObject(o.toString());
                            String airportName = obj.getString("name");
                            String lon = obj.getString("longitude");
                            String lat = obj.getString("latitude");
                            Intent intent = new Intent("icaoData");
                            intent.putExtra("name", airportName);
                            intent.putExtra("lon", lon);
                            intent.putExtra("lat", lat);
                            intent.putExtra("isDeparture", isDeparture);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                if (error instanceof ServerError) {
                }

                if (error.getMessage() != null) {
                } else {
                }
            }

        });
        // Add the request to the RequestQueue.

        queue.add(stringRequest);
    }

}
