package com.connectutb.xfuel;

import android.app.Activity;
import android.widget.ArrayAdapter;

public class FuelReportListAdapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] fuelData;
	
	public FuelReportListAdapter(Activity context, String[] fuelData) {
		super(context, R.layout.fuel_report_row, fuelData);
		this.context = context;
		this.fuelData = fuelData;
	}
	
	

}
