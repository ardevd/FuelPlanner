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
            desc = context.getString(R.string.oew_summary);
        } else if (key.equals("TIME_BLK")){
            title = context.getString(R.string.time_blk);
            desc = context.getString(R.string.time_blk_summary);
        } else if (key.equals("TIME_TTE")){
            title = context.getString(R.string.time_tte);
            desc = context.getString(R.string.time_tte_summary);
        } else if (key.equals("FUEL_RSV")){
            title = context.getString(R.string.fuel_rsv);
            desc = context.getString(R.string.fuel_rsv_summary);
        } else if (key.equals("TIME_RSV")){
            title = context.getString(R.string.time_rsv);
            desc = context.getString(R.string.time_rsv_summary);
        } else if (key.equals("NM")){
            desc = context.getString(R.string.nm_summary);
        } else if (key.equals("TOW")){
            desc = context.getString(R.string.tow_summary);
        } else if (key.equals("UNDERLOAD")){
            desc = context.getString(R.string.underload_summary);
        } else if (key.equals("ZFW")){
            desc = context.getString(R.string.zfw_summary);
        } else if (key.equals("TTL")){
            desc = context.getString(R.string.ttl_summary);
        } else if (key.equals("LWT")){
            desc = context.getString(R.string.lwt_summary);
        } else if (key.equals("FUEL_EFU")){
            desc = context.getString(R.string.fuel_efu_summary);
        } else if (key.equals("FUEL_TOF")){
            desc = context.getString(R.string.fuel_tof_summary);
        }

        values.put("title", title);
        values.put("desc", desc);

        return values;
    }

}
