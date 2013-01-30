package com.connectutb.xfuel;

import com.connectutb.xfuel.util.DbManager;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;

public class Favorites extends ListActivity{
	
	//Define database manager
		DbManager db = new DbManager(this);
		
		private String[] fav_array = new String[0];
		
		@Override
		public void onCreate(Bundle icicle) {
			super.onCreate(icicle);
			ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
		    fav_array = db.listFavorites();
			setListAdapter(new FavoritesAdapter(this, fav_array));
		}

}
