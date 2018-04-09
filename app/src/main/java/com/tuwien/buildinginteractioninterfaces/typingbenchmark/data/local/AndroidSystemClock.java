package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local;

import android.os.SystemClock;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.Clock;

public class AndroidSystemClock implements Clock {

    @Override
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
}
