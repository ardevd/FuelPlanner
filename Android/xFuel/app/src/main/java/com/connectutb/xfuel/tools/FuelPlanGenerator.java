package com.connectutb.xfuel.tools;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.connectutb.xfuel.MainFragment;
import com.connectutb.xfuel.R;
import com.connectutb.xfuel.providers.HistoryContract;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


public class FuelPlanGenerator implements HistoryContract {
    private Context context;
    private ProgressDialog progressFuelPlan;

    public FuelPlanGenerator(Context context){
        this.context = context;
    }

    public void generateFuelPlan(final String departure, final String arrival, final String aircraft, final HashMap<String, String> advancedOptions, final boolean wantMetric){

        String url = context.getString(R.string.post_url);
        // Configure progressDialog
        progressFuelPlan = ProgressDialog.show(context, context.getString(R.string.progress_fuelplan_title),
                context.getString(R.string.progress_fuelplan_text), true);
        // Save to History
        saveToHistory(departure, arrival, aircraft, advancedOptions, wantMetric);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parse(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressFuelPlan.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progressFuelPlan.dismiss();
                        ErrorDialog ed = new ErrorDialog();
                        ed.showErrorDialog(context.getString(R.string.error_title), context.getString(R.string.error_fuel_plan), context);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("QUERY", "FUEL");
                params.put("USER", context.getString(R.string.email));
                params.put("ACCOUNT", context.getString(R.string.account));
                params.put("LICENSE", context.getString(R.string.license));
                params.put("ORIG", departure);
                params.put("DEST", arrival);
                params.put("EQPT", aircraft);
                params.put("METAR", "YES");
                if (wantMetric){
                    params.put("UNITS", "METRIC");
                }

                if (advancedOptions.size()>0){
                    for (Map.Entry<String, String> entry : advancedOptions.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        params.put(key, value);
                    }
                }

                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }

    private void saveToHistory(final String departure, final String arrival, final String aircraft, final HashMap<String, String> advancedOptions, final boolean wantMetric){
        ContentValues planVals = new ContentValues();
        planVals.put(HISTORY_AIRCRAFT, aircraft);
        planVals.put(HISTORY_ARRIVAL, arrival);
        planVals.put(HISTORY_DEPARTURE, departure);
        if (wantMetric) {
            planVals.put(HISTORY_UNIT, 1);
        } else{
            planVals.put(HISTORY_UNIT, 0);
        }

        if (advancedOptions.size()>0) {
            for (Map.Entry<String, String> entry : advancedOptions.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                // TODO: Is there a better way of doing this?
                if (key.equals("TTL")){
                    planVals.put(HISTORY_TTL, value);
                } else if (key.equals("OEW")){
                    planVals.put(HISTORY_OEW, value);
                } else if (key.equals("MTANK")){
                    planVals.put(HISTORY_MTANK, value);
                } else if (key.equals("TANKER")){
                    planVals.put(HISTORY_TANKER, value);
                }
            }
        }

        // Insert history item
        Uri res = context.getContentResolver().insert(INSERT_HISTORY_ITEM, planVals);
        Log.d("xFuel", res.toString());
    }

    private void parse(String data) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(data));
        int eventType = xpp.getEventType();
        boolean fuelPlanBegins = false;
        HashMap fphm = new HashMap();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == XmlPullParser.END_DOCUMENT) {
            } else if(eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("MESSAGES")){
                    break;
                } else if (fuelPlanBegins){
                    String tagValue = xpp.nextText();
                    if (tagValue.length()>0) {
                        fphm.put(xpp.getName(), tagValue.toString());
                    }

                } else if (xpp.getName().equals("DESCRIP")){
                    fuelPlanBegins = true;
                }
            } else if(eventType == XmlPullParser.END_TAG) {
            } else if(eventType == XmlPullParser.TEXT) {

            }
            eventType = xpp.next();

        }

        int count = fphm.size();
        progressFuelPlan.dismiss();
        Intent intent = new Intent("fuelPlan");
        intent.putExtra("data", fphm);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
