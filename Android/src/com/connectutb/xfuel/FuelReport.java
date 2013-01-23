package com.connectutb.xfuel;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class FuelReport extends ListActivity{
	
	private String[] fuelData = new String[0];
	private String aircraft;
	private String origin;
	private String destination;
	
	private ShareActionProvider mShareActionProvider;
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate menu resource file.
	    getMenuInflater().inflate(R.menu.fuel_report, menu);

	    // Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.menu_item_share);

	    // Fetch and store ShareActionProvider
	    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
	    
	    mShareActionProvider.setShareIntent(createShareIntent());
	    // Return true to display menu
	    return true;
	}
	
	private Intent createShareIntent(){
		/** Compile a nicely formatted load sheet **/
		String load_sheet = "xFuel Load Sheet" + System.getProperty("line.separator");
		load_sheet += origin + "->" + destination + " (" + aircraft + ")" + System.getProperty("line.separator");
		for (int i = 0; i < getListView().getLastVisiblePosition(); i++){
			Object o = getListAdapter().getItem(i);
	    	Log.d("XFUEL", o.toString());
	    	load_sheet += o.toString().replace("-", ":") + System.getProperty("line.separator");
		}
		Intent I= new Intent(Intent.ACTION_SEND);
        I.setType("text/plain");
        I.putExtra(android.content.Intent.EXTRA_SUBJECT, "xFuel - Fuel Plan: " + origin + "->" + destination + " (" + aircraft + ")");
        I.putExtra(android.content.Intent.EXTRA_TEXT, load_sheet);
        return I;
	}

	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
	    if (mShareActionProvider != null) {
	        mShareActionProvider.setShareIntent(shareIntent);
	    }
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
