package com.ahsailabs.beritakita.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.ahsailabs.beritakita.R;
import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.login.models.LoginData;

import java.io.IOException;

/**
 * Created by ahmad s on 2019-10-04.
 */
public class SessionUtil {
    public static boolean isLoggedIn(Activity activity){
        /*SharedPreferences sharedPreferences = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Config.DATA_ISLOGGEDIN,false);*/

        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccountsByType(activity.getPackageName());
        return accounts.length > 0;
    }

    public static Intent login(Context context, LoginData loginData) {
        /*
        SharedPreferences.Editor editor = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(Config.DATA_TOKEN, loginData.getToken());
        editor.putString(Config.DATA_NAME, loginData.getName());
        editor.putString(Config.DATA_USERNAME, loginData.getUsername());
        editor.putBoolean(Config.DATA_ISLOGGEDIN, true);
        editor.apply();
         */

        final Intent authIntent = new Intent();
        authIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME, loginData.getUsername());
        authIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, context.getPackageName());
        authIntent.putExtra(AccountManager.KEY_AUTHTOKEN, loginData.getToken());

        final Account account = new Account(loginData.getUsername(), context.getPackageName());
        AccountManager mAccountManager = AccountManager.get(context);
        //if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)

        Bundle data = new Bundle();
        data.putString("name", loginData.getName());

        mAccountManager.addAccountExplicitly(account, loginData.getPassword(), data);
        mAccountManager.setAuthToken(account, loginData.getTokenType(), loginData.getToken());
        //} else {
        //    mAccountManager.setPassword(account, loginData.getPassword());
        //}

        return authIntent;
    }

    public static void logout(Activity activity) {
        /*SharedPreferences.Editor editor = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.remove(Config.DATA_TOKEN);
        editor.remove(Config.DATA_NAME);
        editor.remove(Config.DATA_USERNAME);
        editor.remove(Config.DATA_ISLOGGEDIN);
        editor.apply();*/

        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccountsByType(activity.getPackageName());
        if(accounts.length > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                am.removeAccount(accounts[0], activity, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {

                    }
                }, new Handler(Looper.getMainLooper()));
            } else {
                am.removeAccount(accounts[0], new AccountManagerCallback<Boolean>() {
                    @Override
                    public void run(AccountManagerFuture<Boolean> future) {

                    }
                }, new Handler(Looper.getMainLooper()));
            }
        }
    }

    public static LoginData getLoginData(Activity activity) throws AuthenticatorException, OperationCanceledException, IOException {
        /*SharedPreferences sharedPreferences = context.getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
        LoginData loginData = new LoginData();
        loginData.setToken(sharedPreferences.getString(Config.DATA_TOKEN, ""));
        loginData.setUsername(sharedPreferences.getString(Config.DATA_USERNAME, ""));
        loginData.setName(sharedPreferences.getString(Config.DATA_NAME, ""));
        return loginData;*/

        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccountsByType(activity.getPackageName());
        if(accounts.length > 0) {
            Account account = accounts[0];
            LoginData loginData = new LoginData();
            loginData.setUsername(account.name);
            loginData.setPassword(am.getPassword(account));
            loginData.setName(am.getUserData(account,"name"));
            loginData.setToken(am.peekAuthToken(account, "full"));
            return loginData;
        }

        return null;
    }
}
