package com.connectutb.xfuel;

import com.connectutb.xfuel.util.DbManager;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class FuelHistory extends ListActivity{

	//Define database manager
	DbManager db = new DbManager(this);
	
	private String[] history_array = new String[0];
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		history_array = db.listHistory();
		setListAdapter(new FuelHistoryAdapter(this, history_array));
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id){
    	super.onListItemClick(l, v, position, id);
    	// We retrieve the info for the item that was clicked
    	
    	Object o = this.getListAdapter().getItem(position);
    	Log.d("XFUEL", o.toString());
    	String[] keyword = o.toString().split(";");
    	
    	//ORIG - DEST - AIRCRAFT - RULES - UNITS - METAR
    	String orig = keyword[1];
    	String dest = keyword[2];
    	String aircraft = keyword[3];
    	String rules = keyword[4];
    	String units = keyword[5];
    	String metar = keyword[6];
    	boolean bMetar = false;
    	if (metar.equals("YES")){
    		bMetar = true;
    	}
    	
    	//Request Fuel Planner
    	ProgressBar progress = (ProgressBar)findViewById(R.id.progressHistory);
    	FuelPlanner fp = new FuelPlanner(this, aircraft, orig, dest, bMetar, rules, units, progress);
    	fp.wantFuelInfo();
    }
}