package com.connectutb.xfuel;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.connectutb.xfuel.tools.ICAOManager;

import java.util.HashMap;

/**
 * Created by eholst on 31.08.15.
 */
public class FuelPlanFragment extends Fragment{

    private ListView fuelPlanListView;
    private TextView textDistance;
    private TextView textDep;
    private TextView textDepName;
    private TextView textArr;
    private TextView textArrName;
    private TextView textAircraft;

    private String depIcao;
    private String arrIcao;
    private String aircraft;

    private ShareActionProvider mShareActionProvider;
    private SharedPreferences settings;

    private HashMap<String, String> fuelData;

    public static FuelPlanFragment newInstance(HashMap<String, String> fuelData) {
        FuelPlanFragment fragment = new FuelPlanFragment();
        Bundle args = new Bundle();
        args.putSerializable("data", fuelData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        setHasOptionsMenu(true);
        fuelData = (HashMap<String, String>)extras.getSerializable("data");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fuelplan, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.share_fuelplan);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private BroadcastReceiver mIcaoDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isDeparture = intent.getBooleanExtra("isDeparture", true);
            String icaoName = intent.getStringExtra("name");
            if (isDeparture){
                textDepName.setText(icaoName);
            } else {
                textArrName.setText(icaoName);
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fuel_plan, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        fuelPlanListView = (ListView) rootView.findViewById(R.id.listViewFuelPlan);
        textDistance = (TextView) rootView.findViewById(R.id.textViewReportDistanceValue);
        textDep = (TextView) rootView.findViewById(R.id.textViewReportDeparture);
        textDepName = (TextView) rootView.findViewById(R.id.textViewReportDepartureName);
        textArr = (TextView) rootView.findViewById(R.id.textViewReportArrival);
        textArrName = (TextView) rootView.findViewById(R.id.textViewReportArrivalName);
        textAircraft = (TextView) rootView.findViewById(R.id.textViewPlanAircraft);
        textDistance.setText(fuelData.get("NM") + " NM");
        ICAOManager im = new ICAOManager(getActivity());

        aircraft = settings.getString("aircraft", "");
        depIcao = settings.getString("dep_icao", "");
        arrIcao = settings.getString("arr_icao", "");
        textDep.setText(depIcao);
        textArr.setText(arrIcao);
        textAircraft.setText(aircraft);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mIcaoDataReceiver, new IntentFilter("icaoData"));

        im.icaoToName(depIcao, true);
        im.icaoToName(arrIcao, false);

        // Configure Adapter
        FuelPlanAdapter adapter = new FuelPlanAdapter(getActivity(), fuelData);
        fuelPlanListView.setAdapter(adapter);

        return rootView;
    }

    private Intent createShareIntent(){
        /** Compile a nicely formatted load sheet **/
        String load_sheet = "xFuel Load Sheet" + System.getProperty("line.separator");
        load_sheet += depIcao + "->" + arrIcao + " (" + aircraft + ")" + System.getProperty("line.separator");
        for (int i = 0; i < fuelPlanListView.getCount(); i++){
            Object o = fuelPlanListView.getAdapter().getItem(i);
            load_sheet += o.toString() + System.getProperty("line.separator");
        }
        Intent I= new Intent(Intent.ACTION_SEND);
        I.setType("text/plain");
        I.putExtra(android.content.Intent.EXTRA_SUBJECT, "xFuel - Fuel Plan: " + depIcao + "->" + arrIcao + " (" + aircraft + ")");
        I.putExtra(android.content.Intent.EXTRA_TEXT, load_sheet);
        return I;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share_fuelplan:
                // Delete items from history
                mShareActionProvider.setShareIntent(createShareIntent());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
