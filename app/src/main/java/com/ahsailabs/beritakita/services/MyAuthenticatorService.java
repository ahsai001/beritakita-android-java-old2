package com.ahsailabs.beritakita.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ahsailabs.beritakita.authenticators.MyAuthenticator;


/**
 * Created by ahmad s on 22/09/20.
 */
public class MyAuthenticatorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyAuthenticator authenticator = new MyAuthenticator(this);
        return authenticator.getIBinder();
    }
}
