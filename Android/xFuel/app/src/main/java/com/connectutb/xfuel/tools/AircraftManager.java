package com.connectutb.xfuel.tools;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.android.volley.Request;
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

    private Context context;

    private ProgressDialog progressAircraftUpdate;

    public AircraftManager(Context context){
        this.context = context;

    }

    public void updateAircraftList() {

        Cursor s = context.getContentResolver().query(QUERY_AIRCRAFT_ITEM, null, null, null, null);
        int aircraftCount = s.getCount();
        progressAircraftUpdate = new ProgressDialog(context);
        if (aircraftCount == 0) {
            /**
             * If there is no aircraft data present, we should show a progress dialog
             */
            // Configure progressDialog
            progressAircraftUpdate = ProgressDialog.show(context, context.getString(R.string.progress_aircraft_title),
                    context.getString(R.string.progress_aircraft_text), true);
        }
        String url = context.getString(R.string.post_url);

        // Close cursor
        s.close();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressAircraftUpdate.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressAircraftUpdate.dismiss();
                        ErrorDialog ed = new ErrorDialog();
                        ed.showErrorDialog(context.getString(R.string.error_title), context.getString(R.string.error_aircraft_download), context);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
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
                // System.out.println("Start document");
            } else if(eventType == XmlPullParser.END_DOCUMENT) {
                // System.out.println("End document");
            } else if(eventType == XmlPullParser.START_TAG) {
                // System.out.println("Start tag "+xpp.getName());
                if (xpp.getName().equals("DESCRIP") && aircraftBegins){
                    aircraftVals.put(AIRCRAFT_NAME, xpp.nextText());
                    Uri res = context.getContentResolver().insert(INSERT_AIRCRAFT_ITEM, aircraftVals);
                    aircraftVals = new ContentValues();
                } else if (xpp.getName().equals("ID") && aircraftBegins) {
                    aircraftVals.put(AIRCRAFT_CODE, xpp.nextText());
                }
            } else if(eventType == XmlPullParser.END_TAG) {
                //System.out.println("End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT) {

                // System.out.println("Text "+xpp.getText());
                if (xpp.getText().equals("A300")) {
                    aircraftBegins = true;
                    // Found aircraft data, delete existing data.
                    context.getContentResolver().delete(QUERY_AIRCRAFT_ITEM, null, null);
                    aircraftVals.put(AIRCRAFT_CODE, xpp.getText());
                }
            }
            eventType = xpp.next();

        }

        progressAircraftUpdate.dismiss();
    }

}