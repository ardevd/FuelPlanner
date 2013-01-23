package com.connectutb.xfuel;

import com.connectutb.xfuel.util.DbManager;

import android.app.ListActivity;
import android.os.Bundle;

public class FuelHistory extends ListActivity{

	//Define database manager
	DbManager db = new DbManager(this);
	
	private String[] history_array = new String[0];
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		history_array = db.listHistory();
		setListAdapter(new FuelHistoryAdapter(this, history_array));
	}
}
