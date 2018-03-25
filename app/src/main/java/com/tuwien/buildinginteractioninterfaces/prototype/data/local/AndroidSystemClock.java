package com.tuwien.buildinginteractioninterfaces.prototype.data.local;

import android.os.SystemClock;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.Clock;

public class AndroidSystemClock implements Clock {

    @Override
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
}
