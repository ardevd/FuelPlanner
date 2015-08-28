package com.connectutb.xfuel.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public interface HistoryContract extends BaseColumns {

    String HISTORY_AUTHORITY = "com.connectutb.xfuel.history";

    Uri HISTORY_CONTENT_URI = Uri.parse("content//" + HISTORY_AUTHORITY + "/item");

    String HISTORY_ITEM = "history";

    // SQL Table fields
    String TABLE_HISTORY = "history";
    String HISTORY_ID = BaseColumns._ID;
    String HISTORY_NAME = "name";
    String HISTORY_DEPARTURE = "departure";
    String HISTORY_ARRIVAL = "arrival";
    String HISTORY_AIRCRAFT = "aircraft";

    // Queries
    Uri QUERY_HISTORY_ITEM = Uri.parse("content://" + HISTORY_AUTHORITY + "/" + HISTORY_ITEM);

}