package com.connectutb.xfuel;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/** Prepare the Aircraft Array. We are splitting on ; **/
		String[] aircraftArray = getResources().getStringArray(R.array.planetypes);
		ArrayList<String> temp_array = new ArrayList<String>();
		for (int i = 0; i < aircraftArray.length; i++){
			//Loop through the aircraftArray and add the stripped strings to the temp_array
			String[] splittedAircraftString = aircraftArray[i].split(";");
			temp_array.add(splittedAircraftString[0]);
		}
		//Convert the ArrayList to String Array
		aircraftArray = (String[]) temp_array.toArray(aircraftArray);
		
		//Prepare the Aircraft Spinner
	    Spinner localSpinner = (Spinner)findViewById(R.id.spinnerPlanes);
	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   
	    		R.layout.spinner_white, aircraftArray);
	    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    localSpinner.setAdapter(spinnerArrayAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
