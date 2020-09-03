package com.ahsailabs.beritakita.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ahsailabs.beritakita.configs.Config;

/**
 * Created by ahmad s on 2019-10-04.
 */
public class SessionUtil {
    public static boolean isLoggedIn(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Config.DATA_ISLOGGEDIN,false);
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.remove(Config.DATA_TOKEN);
        editor.remove(Config.DATA_NAME);
        editor.remove(Config.DATA_USERNAME);
        editor.remove(Config.DATA_ISLOGGEDIN);
        editor.apply();
    }
/*
    public static void login(Context context, LoginData loginData) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(Config.DATA_TOKEN, loginData.getToken());
        editor.putString(Config.DATA_NAME, loginData.getName());
        editor.putString(Config.DATA_USERNAME, loginData.getUsername());
        editor.putBoolean(Config.DATA_ISLOGGEDIN, true);
        editor.apply();
    }

    public static LoginData getLoginData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
        LoginData loginData = new LoginData();
        loginData.setToken(sharedPreferences.getString(Config.DATA_TOKEN,""));
        loginData.setName(sharedPreferences.getString(Config.DATA_NAME,""));
        loginData.setUsername(sharedPreferences.getString(Config.DATA_USERNAME,""));
        return loginData;
    }*/
}
