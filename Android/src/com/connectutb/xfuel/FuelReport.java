package com.connectutb.xfuel;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class FuelReport extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
}
