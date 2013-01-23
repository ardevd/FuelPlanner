package com.connectutb.xfuel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FuelHistoryAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] history;
	
	public FuelHistoryAdapter(Activity context, String[] history){
		super(context, R.layout.history_row, history);
		this.context = context;
		this.history = history;
	}
	
	static class ViewHolder{
		public TextView textViewOrig;
		public TextView textViewDest;
		public TextView textViewAircraft;
		public TextView textViewRules;
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
			rowView = inflater.inflate(R.layout.history_row, null, true);
			holder = new ViewHolder();
			holder.textViewOrig = (TextView) rowView.findViewById(R.id.textViewHistoryOrigin);
			holder.textViewDest = (TextView) rowView.findViewById(R.id.textViewHistoryDest);
			holder.textViewAircraft = (TextView) rowView.findViewById(R.id.textViewHistoryAircraft);
			holder.textViewRules = (TextView) rowView.findViewById(R.id.textViewHistoryRules);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		String[] arrayString = history[position].split(";");
		//ID - ORIG - DEST - AIRCRAFT - RULES
		String orig = arrayString[1];
		String dest = arrayString[2];
		String aircraft = arrayString[3];
		String rules = arrayString[4];
		String resIDString = arrayString[1];
		holder.textViewOrig.setText(orig);
		holder.textViewDest.setText(dest);
		holder.textViewAircraft.setText(aircraft);
		holder.textViewRules.setText(rules);
		
		return rowView;
	}

}
