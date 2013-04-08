package com.connectutb.xfuel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.connectutb.xfuel.util.DbManager;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final String TAG ="xFuel";

	/* Our preferences */
	public SharedPreferences settings;
	public SharedPreferences.Editor editor;
	/* Our view components */
	public Spinner aircraftSpinner;
	public Spinner rulesSpinner;
	public EditText orig;
	public EditText dest;
	public Switch metar;
	public RadioButton radioMetrics;
	public RadioButton radioImperial;
	
	public Map<String, String> mMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Views
		orig = (EditText)findViewById(R.id.editTextOrigin);
		dest = (EditText)findViewById(R.id.editTextDest);
		metar = (Switch) findViewById(R.id.switchWeather);
		
		//Prevent Keyboard from popping up on activity start
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		/** Prepare the Aircraft Array. We are splitting on ; **/
		mMap = new HashMap<String, String>();
		String[] aircraftArray = getResources().getStringArray(R.array.planetypes);
		ArrayList<String> temp_array = new ArrayList<String>();
		for (int i = 0; i < aircraftArray.length; i++){
			//Loop through the aircraftArray and add the stripped strings to the temp_array
			String[] splittedAircraftString = aircraftArray[i].split(";");
			temp_array.add(splittedAircraftString[0]);
			//Add the values to the hash map
			mMap.put(splittedAircraftString[0], splittedAircraftString[1]);
		}
		//Convert the ArrayList to String Array
		aircraftArray = (String[]) temp_array.toArray(aircraftArray);
		
		//Prepare the Aircraft Spinner
		aircraftSpinner = (Spinner)findViewById(R.id.spinnerPlanes);
	    ArrayAdapter<String> aircraftSpinnerArrayAdapter = new ArrayAdapter<String>(this,   
	    		R.layout.spinner_white, aircraftArray);
	    aircraftSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    aircraftSpinner.setAdapter(aircraftSpinnerArrayAdapter);
	    
	    //Prepare the Rules spinner
		rulesSpinner = (Spinner)findViewById(R.id.spinnerRules);
	    ArrayAdapter rulesSpinnerArrayAdapter = ArrayAdapter.createFromResource( this, R.array.rules_array , R.layout.spinner_white); 
	    rulesSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    rulesSpinner.setAdapter(rulesSpinnerArrayAdapter);
	    
	    //Init prefs and editor
	    settings = PreferenceManager.getDefaultSharedPreferences(this);
	    editor = settings.edit();
	    
	    //Grab Default values
	    grabDefaults();
	    
		//Set Fonts
		TextView xfuel_title = (TextView)findViewById(R.id.fuelTitle);
		TextView aircraft_title = (TextView)findViewById(R.id.textViewPlanesTitle);
		TextView orig_title = (TextView)findViewById(R.id.TextViewOrigin);
		TextView dest_title = (TextView)findViewById(R.id.TextViewDest);
		TextView rules = (TextView)findViewById(R.id.TextViewRules);
		Typeface tf = Typeface.createFromAsset(getAssets(),
	            "fonts/Roboto-Light.ttf");
		Typeface tfq = Typeface.createFromAsset(getAssets(),
	            "fonts/quartz.ttf");
		xfuel_title.setTypeface(tf);
		aircraft_title.setTypeface(tf);
		orig_title.setTypeface(tf);
		dest_title.setTypeface(tf);
		metar.setTypeface(tf);
		orig.setTypeface(tfq);
		dest.setTypeface(tfq);
		radioMetrics.setTypeface(tf);
		radioImperial.setTypeface(tf);
		rules.setTypeface(tf);
		
		//Auto fill default airports if set
		if (settings.getBoolean("use_default_airports", false) == true){
			orig.setText(settings.getString("default_origin", ""));
			dest.setText(settings.getString("default_destination", ""));
		}
		
		//Get last used ruleset
		int lastRuleSet = settings.getInt("last_ruleset", 0);
		rulesSpinner.setSelection(lastRuleSet);
		
		//Get last used aircraft
		int lastAircraft = settings.getInt("last_aircraft", 0);
		aircraftSpinner.setSelection(lastAircraft);
		
		//Get last used airports if set to do so
		if (settings.getBoolean("remember_airports", true) == true){
			orig.setText(settings.getString("last_origin", ""));
			dest.setText(settings.getString("last_dest", ""));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void grabDefaults(){
		radioMetrics = (RadioButton)findViewById(R.id.radioMetrics);
		radioImperial = (RadioButton)findViewById(R.id.radioImperial);
		Log.d(TAG, settings.getString("def_units", "Metrics"));
		if (settings.getString("def_units", "Metrics").equals("Metrics")){
				radioMetrics.setChecked(true);
				
		}else{
			radioImperial.setChecked(true);
		}
	}
	
	/* Action on menu selection */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	//Open prefs
    	case R.id.menu_settings:
    		Intent settingsIntent = new Intent(this, Preferences.class);
        	startActivity(settingsIntent);	 
    		return true;

    	case R.id.menu_history:
    		Intent historyIntent = new Intent(this, FuelHistory.class);
        	startActivity(historyIntent);	 
    		return true;
    	
    	case R.id.menu_favourites:
    		Intent favsIntent = new Intent(this, Favorites.class);
        	startActivity(favsIntent);	 
    		return true;
    	
    	case R.id.menu_rate:
    		/* Show app listing */
        	Intent intent = new Intent(Intent.ACTION_VIEW); 
        	intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.connectutb.xfuel")); 
        	startActivity(intent);
        	return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    //Post Fuel request
    public void postFuelRequest(View view){
    	//Check if user actually entered the required information
    	
    	if(orig.getText().toString().length()==4 && dest.getText().toString().length()==4){
    	//Do we request weather?
    	boolean getMetar = false;
    	if (metar.isChecked()){
    		getMetar = true;
    	}
    	//Determine units
    	String units = "LBS";
    	if (radioMetrics.isChecked()){
    		units = "METRIC";
    	}
    	
    	//Set last rule and aircraft setting
    	int selectedRule = rulesSpinner.getSelectedItemPosition();
    	editor.putInt("last_ruleset", selectedRule);
    	editor.putInt("last_aircraft", aircraftSpinner.getSelectedItemPosition());
    	editor.putString("last_origin", orig.getText().toString());
    	editor.putString("last_dest", dest.getText().toString());
    	editor.commit();
    	
        //Define progressDialog
        ProgressBar progress = (ProgressBar)findViewById(R.id.progress);
    	FuelPlanner fp = new FuelPlanner(this, mMap.get(aircraftSpinner.getSelectedItem().toString()).toString(), orig.getText().toString(), dest.getText().toString(), getMetar,rulesSpinner.getSelectedItem().toString(), units, progress);
    	//add to history
    	//Define our database manager
        DbManager db = new DbManager(this);
        
        db.addToHistory(mMap.get(aircraftSpinner.getSelectedItem().toString()).toString(), orig.getText().toString(), dest.getText().toString(), getMetar,rulesSpinner.getSelectedItem().toString(), units);
    	fp.wantFuelInfo();
    	}
    	else{
    		//Show toast to notify user that orig and destination ICAO needs to be filled
    		 Toast.makeText(this, R.string.error_empty_request, Toast.LENGTH_SHORT).show();
    	}
    }   
}