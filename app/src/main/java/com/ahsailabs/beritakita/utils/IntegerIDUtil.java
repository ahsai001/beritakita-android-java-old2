package com.ahsailabs.beritakita.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ahmad s on 2/24/2016.
 */
public class IntegerIDUtil {
    private static AtomicInteger atomicInteger = null;
    private static final String PREFERENCES = "IntegerIDUtil_Preferences";
    private static final String ATOMIC_INIT_VALUE_FOR_NOTIF = "atomic_init_value_for_increment_id";
    private static final int init_value = 0;
    private static final int max_value = 65535;
    public static int getID(Context context) {
        synchronized (IntegerIDUtil.class){
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

            if(atomicInteger == null){
                int init = sharedPreferences.getInt(ATOMIC_INIT_VALUE_FOR_NOTIF, init_value);
                atomicInteger = new AtomicInteger(init);
            }

            int nextValue = atomicInteger.incrementAndGet();

            //use limit value
            if(nextValue > max_value){
                nextValue = init_value+1;
                atomicInteger.set(nextValue);
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(ATOMIC_INIT_VALUE_FOR_NOTIF,nextValue);
            editor.apply();

            return nextValue;
        }
    }

    public static int getID() {
        synchronized (IntegerIDUtil.class){
            return (int) (System.currentTimeMillis()/1000);
        }
    }
}