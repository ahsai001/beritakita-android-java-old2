package com.ahsailabs.beritakita.bases;

import android.app.Application;
import android.util.Log;

/**
 * Created by ahmad s on 09/09/20.
 */
public class BaseApp extends Application {
    public String data = "ini data di BaseApp";
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
