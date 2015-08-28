package com.connectutb.xfuel.tools;

import android.content.ContentValues;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.connectutb.xfuel.R;
import com.connectutb.xfuel.providers.AircraftContract;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class AircraftManager implements AircraftContract {

    Context context;

    public AircraftManager(Context context){
        this.context = context;
    }

    public void updateAircraftList(){
        String url = context.getString(R.string.post_url);
        // Delete existing aircraft list
        context.getContentResolver().delete(QUERY_AIRCRAFT_ITEM, null, null);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("QUERY", "LIST_E");
                params.put("USER", context.getString(R.string.email));
                params.put("ACCOUNT", context.getString(R.string.account));
                params.put("LICENSE", context.getString(R.string.license));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }

    public void parse(String aircraftData) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(aircraftData));
        int eventType = xpp.getEventType();
        boolean aircraftBegins = false;
        ContentValues aircraftVals = new ContentValues();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.END_DOCUMENT) {
                System.out.println("End document");
            } else if(eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag "+xpp.getName());
                if (xpp.getName().equals("DESCRIP") && aircraftBegins){
                    aircraftVals.put(AIRCRAFT_NAME, xpp.nextText());
                    context.getContentResolver().insert(INSERT_AIRCRAFT_ITEM, aircraftVals);
                    aircraftVals = new ContentValues();
                } else if (xpp.getName().equals("ID") && aircraftBegins) {
                    aircraftVals.put(AIRCRAFT_CODE, xpp.nextText());
                }
            } else if(eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT) {
                System.out.println("Text "+xpp.getText());
                if (xpp.getText().equals("A300")) {
                    aircraftBegins = true;
                    aircraftVals.put(AIRCRAFT_CODE, xpp.getText());
                }

            }


            eventType = xpp.next();
        }

    }
}
