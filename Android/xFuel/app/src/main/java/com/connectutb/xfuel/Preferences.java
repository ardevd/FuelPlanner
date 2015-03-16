package com.connectutb.xfuel;

import java.util.List;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity{
	/* Load the preferences */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		 ActionBar actionBar = getActionBar();
		 actionBar.setDisplayHomeAsUpEnabled(true);
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
    
    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferences_headers, target);
    }
    
    @Override
    protected boolean isValidFragment (String fragmentName)
    {
      if(Preferences.class.getName().equals(fragmentName))
          return true;
      //TODO it should return false if fragment is not valid
      return true;

    }
    
    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}