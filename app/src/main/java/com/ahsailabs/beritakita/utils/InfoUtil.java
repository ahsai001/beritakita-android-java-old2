package com.ahsailabs.beritakita.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by ahmad s on 2019-09-26.
 */
public class InfoUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showSnackBar(View view, String message){
        Snackbar.make(view,message, Snackbar.LENGTH_LONG).show();
    }
}
