package com.ahsailabs.beritakita.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ahsailabs.beritakita.MainActivity;
import com.ahsailabs.beritakita.R;
import com.ahsailabs.beritakita.utils.NotificationUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by ahmad s on 11/09/20.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        NotificationUtil.onMessageReceived(getBaseContext(), remoteMessage.getData(),notification!=null?notification.getTitle():null,notification!=null?notification.getBody():null, MainActivity.class,null,null, R.string.app_name, R.drawable.notif_icon,null, false);
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        Log.d("beritakita", "new token:" + newToken);

        sendTokenToServer(newToken);
    }

    private void sendTokenToServer(String newToken) {
        //kirim token via api, bisa pake retrofit atau androidnetworking
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
