package com.tuwien.buildinginteractioninterfaces.prototype.threading;

import android.os.Handler;
import android.os.Looper;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.MainThread;

import java.lang.annotation.Annotation;


/**
 * Created by duarte on 18-12-2017.
 */
public class MainThreadImpl implements MainThread {
    private static MainThread sMainThread;

    private Handler mHandler;

    private MainThreadImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static MainThread getInstance() {
        if (sMainThread == null) {
            sMainThread = new MainThreadImpl();
        }

        return sMainThread;
    }

}
