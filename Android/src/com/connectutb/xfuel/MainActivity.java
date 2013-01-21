package com.connectutb.xfuel;

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
		
		//Assign spinner layout
	    Spinner localSpinner = (Spinner)findViewById(R.id.spinnerPlanes);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	                R.array.planetypes,
	                R.layout.spinner_white);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    localSpinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
