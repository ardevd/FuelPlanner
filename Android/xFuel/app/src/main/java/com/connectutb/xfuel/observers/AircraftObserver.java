package com.connectutb.xfuel.observers;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * Created by eholst on 28.08.15.
 */
public class AircraftObserver extends ContentObserver {

    public AircraftObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange){
        super.onChange(selfChange);
    }
}
