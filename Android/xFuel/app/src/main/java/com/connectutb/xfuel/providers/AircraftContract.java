package com.connectutb.xfuel.providers;

import android.net.Uri;
import android.provider.BaseColumns;


public interface AircraftContract  extends BaseColumns {

    String AIRCRAFT_AUTHORITY = "com.connectutb.xfuel.aircraft";

    Uri AIRCRAFT_CONTENT_URI = Uri.parse("content//" + AIRCRAFT_AUTHORITY + "/item");

    String AIRCRAFT_ITEM = "aircraft";

    // SQL Table fields
    String TABLE_AIRCRAFT = "aircraft";
    String AIRCRAFT_ID = BaseColumns._ID;
    String AIRCRAFT_NAME = "name";
    String AIRCRAFT_CODE = "code";

    // Queries
    Uri QUERY_AIRCRAFT_ITEM = Uri.parse("content://" + AIRCRAFT_AUTHORITY + "/" + AIRCRAFT_ITEM);

}
