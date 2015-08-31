package com.connectutb.xfuel;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashMap;

/**
 * Created by eholst on 31.08.15.
 */
public class FuelPlanFragment extends Fragment{

    private ListView fuelPlanListView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fuel_plan, container, false);

        fuelPlanListView = (ListView) rootView.findViewById(R.id.listViewFuelPlan);

        // Configure Adapter
        FuelPlanAdapter adapter = new FuelPlanAdapter(getActivity(), fuelData);
        fuelPlanListView.setAdapter(adapter);
        return rootView;
    }


}
