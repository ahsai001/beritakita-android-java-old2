package com.ahsailabs.beritakita.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ahsailabs.beritakita.NewsDetailActivity;
import com.ahsailabs.beritakita.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by ahmad s on 11/09/20.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        String photo = data.get("photo");
        String newsId = data.get("news_id");

        Log.d("beritakita-notif", "news id : "+ newsId);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("beritakita-2", "berita kita 1", NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }

        Bitmap bigPicture = getBitmapFromURL(photo);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"beritakita-2");
        builder.setContentTitle(title).setContentText(body).setAutoCancel(false).setSmallIcon(R.drawable.notif_icon).setLargeIcon(bigPicture);
        //builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title).setSummaryText("summary"));
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).bigLargeIcon(null));


        //add content intent
        Intent mainIntent = new Intent(this, NewsDetailActivity.class);
        mainIntent.putExtra(NewsDetailActivity.PARAM_NEWS_ID, newsId);
        PendingIntent mainPendingIntent = PendingIntent.
                getActivity(this, 1,mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(mainPendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(1, notification);

        //RemoteMessage.Notification notification = remoteMessage.getNotification();
        //NotificationUtil.onMessageReceived(getBaseContext(), remoteMessage.getData(),notification!=null?notification.getTitle():null,notification!=null?notification.getBody():null, MainActivity.class,null,null, R.string.app_name, R.drawable.notif_icon,null, false);
    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
