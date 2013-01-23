package com.connectutb.xfuel;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FuelReportListAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] fuelData;
	
	public FuelReportListAdapter(Activity context, String[] fuelData) {
		super(context, R.layout.fuel_report_row, fuelData);
		this.context = context;
		this.fuelData = fuelData;
	}
	
	// static to save the reference to the outer class and to avoid access to
		// any members of the containing class
		static class ViewHolder {
			public TextView textViewKey;
			public TextView textViewValue;
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
				rowView = inflater.inflate(R.layout.fuel_report_row, null, true);
				rowView.setBackgroundColor(0x1a1a1a);
				holder = new ViewHolder();
				holder.textViewKey = (TextView) rowView.findViewById(R.id.textViewKey);
				holder.textViewValue = (TextView) rowView.findViewById(R.id.textViewValue);
				rowView.setTag(holder);
			} else {
				holder = (ViewHolder) rowView.getTag();
			}
			String[] arrayString = fuelData[position].split("-");
			String fKey;
			String fValue;
			try{
			fKey = arrayString[0];
			}
			catch(Exception ex){
				fKey="ERROR";
			}
			try{
			fValue = arrayString[1];
			}
			catch(Exception ex){
				fValue="N/A";
			}
			//Set Roboto Font
			Typeface tf = Typeface.createFromAsset(context.getAssets(),
		            "fonts/Roboto-Light.ttf");
			holder.textViewKey.setTypeface(tf);
			holder.textViewValue.setTypeface(tf);
			holder.textViewKey.setText(fKey);
			holder.textViewValue.setText(fValue);
			
			return rowView;
		}

}
