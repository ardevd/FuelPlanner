package com.connectutb.xfuel;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class FuelReport extends ListActivity{
	
	private String[] fuelData = new String[0];
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    fuelData =getIntent().getStringArrayExtra("fuelData");
	    
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
