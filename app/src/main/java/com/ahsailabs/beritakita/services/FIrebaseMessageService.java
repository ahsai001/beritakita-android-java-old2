package com.ahsailabs.beritakita.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by ahmad s on 16/09/20.
 */
class FIrebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("beritakita", "title : "+remoteMessage.getNotification().getTitle());
        Log.d("beritakita", "body : "+remoteMessage.getNotification().getBody());
        Map<String, String> data = remoteMessage.getData();
        String nama = data.get("nama");
        Log.d("beritakita", "nama : "+nama);
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        Log.d("beritakita", "New Token : "+newToken);
    }
}
