package com.connectutb.xfuel.providers;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eholst on 27.08.15.
 */
public interface HistoryContract extends BaseColumns {

    String HISTORY_AUTHORITY = "com.connectutb.xfuel.history";

    Uri HISTORY_CONTENT_URI = Uri.parse("content//" + HISTORY_AUTHORITY + "/item");

    String HISTORY_ITEM = "aircraft";

    // SQL Table fields
    String TABLE_HISTORY = "aircraft";
    String HISTORY_ID = BaseColumns._ID;
    String HISTORY_NAME = "name";
    String HISTORY_DEPARTURE = "departure";
    String HISTORY_ARRIVAL = "arrival";
    String HISTORY_AIRCRAFT = "aircraft";

    // Queries
    Uri QUERY_HISTORY_ITEM = Uri.parse("content://" + HISTORY_AUTHORITY + "/" + HISTORY_ITEM);

}