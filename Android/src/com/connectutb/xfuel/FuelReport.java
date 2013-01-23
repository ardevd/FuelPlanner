package com.connectutb.xfuel;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class FuelReport extends ListActivity{
	
	private String[] fuelData = new String[0];
	private String aircraft;
	private String origin;
	private String destination;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    //Get Extras
	    fuelData = getIntent().getStringArrayExtra("fuelData");
	    aircraft = getIntent().getStringExtra("aircraft");
	    origin = getIntent().getStringExtra("origin");
	    destination = getIntent().getStringExtra("destination");
	    //Add header view
	    View header = getLayoutInflater().inflate(R.layout.fuel_report_header, null);
	    ListView listView = getListView();
	    listView.addHeaderView(header);
	    TextView headerTv = (TextView)findViewById(R.id.textViewFuelReportHeader);
	    TextView subHeaderTv = (TextView)findViewById(R.id.textViewFuelReportSubHeader);
	    Typeface tf = Typeface.createFromAsset(getAssets(),
	            "fonts/Roboto-Light.ttf");
	    headerTv.setTypeface(tf);
	    subHeaderTv.setTypeface(tf);
	    headerTv.setText(getString(R.string.load_sheet));
	    subHeaderTv.setText(origin + "->" + destination + " (" + aircraft + ")");
	    setListAdapter(new FuelReportListAdapter(this, fuelData));
	}

	
	/* Action on menu selection */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	//Go home
    	case android.R.id.home:
    		Intent i = new Intent(this, MainActivity.class);
        	startActivity(i);	 
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
