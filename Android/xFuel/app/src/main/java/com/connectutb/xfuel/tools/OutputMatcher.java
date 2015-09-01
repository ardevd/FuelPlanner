package com.connectutb.xfuel.tools;

import android.content.ContentValues;
import android.content.Context;

import com.connectutb.xfuel.R;

/**
 * Created by eholst on 01.09.15.
 */
public class OutputMatcher {

    private Context context;

    public OutputMatcher(Context context){
        this.context = context;
    }

    public ContentValues getTitleAndDesc(String key){

        ContentValues values= new ContentValues();
        String title = "";
        String desc = "";
        if (key.equals("HEADING_TC")){
            title = context.getString(R.string.heading_tc);
            desc = context.getString(R.string.heading_tc_summary);
        } else if (key.equals("OEW")){
            title = context.getString(R.string.oew);
        } else if (key.equals("TIME_BLK")){
            title = context.getString(R.string.time_blk);
            desc = context.getString(R.string.time_blk_summary);
        }

        values.put("title", title);
        values.put("desc", desc);

        return values;
    }

}
