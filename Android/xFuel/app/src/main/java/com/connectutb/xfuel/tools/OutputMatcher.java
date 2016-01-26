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
        switch (key) {
            case "HEADING_TC":
                title = context.getString(R.string.heading_tc);
                desc = context.getString(R.string.heading_tc_summary);
                break;
            case "OEW":
                title = context.getString(R.string.oew);
                desc = context.getString(R.string.oew_summary);
                break;
            case "TIME_BLK":
                title = context.getString(R.string.time_blk);
                desc = context.getString(R.string.time_blk_summary);
                break;
            case "TIME_TTE":
                title = context.getString(R.string.time_tte);
                desc = context.getString(R.string.time_tte_summary);
                break;
            case "FUEL_RSV":
                title = context.getString(R.string.fuel_rsv);
                desc = context.getString(R.string.fuel_rsv_summary);
                break;
            case "TIME_RSV":
                title = context.getString(R.string.time_rsv);
                desc = context.getString(R.string.time_rsv_summary);
                break;
            case "NM":
                desc = context.getString(R.string.nm_summary);
                break;
            case "TOW":
                desc = context.getString(R.string.tow_summary);
                break;
            case "UNDERLOAD":
                desc = context.getString(R.string.underload_summary);
                break;
            case "ZFW":
                desc = context.getString(R.string.zfw_summary);
                break;
            case "TTL":
                desc = context.getString(R.string.ttl_summary);
                break;
            case "LWT":
                desc = context.getString(R.string.lwt_summary);
                break;
            case "FUEL_EFU":
                desc = context.getString(R.string.fuel_efu_summary);
                break;
            case "FUEL_TOF":
                desc = context.getString(R.string.fuel_tof_summary);
                break;
        }

        values.put("title", title);
        values.put("desc", desc);

        return values;
    }

}
