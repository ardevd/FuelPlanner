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
import android.view.LayoutInflater;
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

        fuelData = (HashMap<String, String>)extras.getSerializable("data");
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
        textDistance.setText(fuelData.get("NM") + " NM");
        ICAOManager im = new ICAOManager(getActivity());

        String depIcao = settings.getString("dep_icao", "");
        String arrIcao = settings.getString("arr_icao", "");
        textDep.setText(depIcao);
        textArr.setText(arrIcao);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mIcaoDataReceiver, new IntentFilter("icaoData"));

        im.icaoToName(depIcao, true);
        im.icaoToName(arrIcao, false);

        // Configure Adapter
        FuelPlanAdapter adapter = new FuelPlanAdapter(getActivity(), fuelData);
        fuelPlanListView.setAdapter(adapter);
        return rootView;
    }


}
