package com.connectutb.xfuel;

import com.connectutb.xfuel.util.DbManager;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Favorites extends ListActivity{
	
	//Define database manager
		DbManager db = new DbManager(this);
		
		private String[] fav_array = new String[0];
		
		@Override
		public void onCreate(Bundle icicle) {
			super.onCreate(icicle);
			ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
		    loadFavsList();
		}
		
		public void loadFavsList(){
			fav_array = db.listFavorites();
			setListAdapter(new FavoritesAdapter(this, fav_array));
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.favs_menu, menu);
			return true;
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
	    	case R.id.menu_delete_favorite:
	    		//Delete selected favs
	    		deleteSelectedFavs();
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
	    	}
	    }
	    
	    private void deleteSelectedFavs(){
	    	/** Loop through the favorites and delete the entries that are checked **/
	    	
	    	for (int i = 0; i < getListView().getLastVisiblePosition() + 1; i++){
				Object o = getListAdapter().getItem(i);
		    	CheckBox cbox = (CheckBox) ((View)getListView().getChildAt(i)).findViewById(R.id.checkBoxFavs); 
		    		if( cbox.isChecked() ) { 
		    			String[] keywordArray = o.toString().split(";");
		    			db.deleteFavorite(keywordArray[0]);
		    			Log.d("XFUEL", "DELETED: " + keywordArray[0]);
		    		}
	    	}
	    	//Reload the list of favorites
	    	loadFavsList();
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
	    	ProgressBar progress = (ProgressBar)findViewById(R.id.progressFavorites);
	    	FuelPlanner fp = new FuelPlanner(this, aircraft, orig, dest, bMetar, rules, units, progress);
	    	fp.wantFuelInfo();
	    }

}
