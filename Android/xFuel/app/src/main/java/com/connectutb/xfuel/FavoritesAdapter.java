package com.connectutb.xfuel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FavoritesAdapter extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] favorites;
	
	public FavoritesAdapter(Activity context, String[] favorites){
		super(context, R.layout.favorites_row, favorites);
		this.context = context;
		this.favorites = favorites;
	}
	
	static class ViewHolder{
		public TextView textViewName;
		public TextView textViewRoute;
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
			rowView = inflater.inflate(R.layout.favorites_row, null, true);
			holder = new ViewHolder();
			holder.textViewName = (TextView) rowView.findViewById(R.id.textViewFavoritesName);
			holder.textViewRoute = (TextView) rowView.findViewById(R.id.textViewFavoritesRoute);
			holder.textViewAircraft = (TextView) rowView.findViewById(R.id.textViewFavoritesAircraft);
			holder.textViewRules = (TextView) rowView.findViewById(R.id.textViewFavoritesRules);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		String[] arrayString = favorites[position].split(";");
		//ID - ORIG - DEST - AIRCRAFT - RULES
		String orig = arrayString[1];
		String dest = arrayString[2];
		String aircraft = arrayString[3];
		String rules = arrayString[4];
		String name = "N/A";
		try{
		 name = arrayString[7];
		}catch(Exception ex){
			
		}
		
		holder.textViewName.setText(name);
		holder.textViewRoute.setText(orig + " --> " + dest);
		holder.textViewAircraft.setText(aircraft);
		holder.textViewRules.setText(rules);
		
		return rowView;
	}
}
