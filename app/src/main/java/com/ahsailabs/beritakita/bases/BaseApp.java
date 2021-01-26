package com.ahsailabs.beritakita.bases;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

/**
 * Created by ahmad s on 09/09/20.
 */
public class BaseApp extends Application {
    public String data = "ini data di BaseApp";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("BaseApp", "oncreate");
        //inisialisasi beberap library

        //
    }

    @Override
    public void onTerminate() {
        //clean up
        super.onTerminate();
    }
}
