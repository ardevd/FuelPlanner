package com.connectutb.xfuel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eholst on 31.08.15.
 */
public class FuelPlanAdapter extends ArrayAdapter<String> {
    private final Activity context;
    // private final HashMap<String, String> fuelData;
    private ArrayList fuelData;

    public FuelPlanAdapter(Activity context, HashMap<String, String> fuelMap) {
        super(context, R.layout.fuelplan_list_row);
        this.context = context;
        fuelData = new ArrayList();
        fuelData.addAll(fuelMap.entrySet());
    }

    // static to save the reference to the outer class and to avoid access to
    // any members of the containing class
    static class ViewHolder {
        public TextView textViewKey;
        public TextView textviewDesc;
        public TextView textViewValue;
    }

    @Override
    public int getCount(){
        return fuelData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder will buffer the assess to the individual fields of the row
        // layout

        ViewHolder holder;
        // Recycle existing view if passed as parameter
        // This will save memory and time on Android
        // This only works if the base layout for all classes are the same
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.fuelplan_list_row, parent, false);
            holder = new ViewHolder();
            holder.textViewKey = (TextView) rowView.findViewById(R.id.textViewPlanListTitle);
            holder.textviewDesc = (TextView) rowView.findViewById(R.id.textViewPlanListDesc);
            holder.textViewValue = (TextView) rowView.findViewById(R.id.textViewPlanListDetails);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Map.Entry<String, String> item = (Map.Entry) fuelData.get(position);
        if (item.getValue().length() > 0) {
            holder.textViewKey.setText(item.getKey());
            holder.textViewValue.setText(item.getValue());
        }

        return rowView;
    }
}