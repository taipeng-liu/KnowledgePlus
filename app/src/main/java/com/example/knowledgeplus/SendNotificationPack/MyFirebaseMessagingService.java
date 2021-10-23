package com.example.knowledgeplus.SendNotificationPack;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.knowledgeplus.MainActivity;
import com.example.knowledgeplus.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.app.TaskStackBuilder;

// keeps the service for 10 seconds after app is closed
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title, message, receiver;
    private static final String CHANNEL_NAME = "Main Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notification description";
    private static final String CHANNEL_ID = "MyNotificationChannel";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("TAG", "the token refreshed " + s);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "technoWeb")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setColor(0xffff7700)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(defaultSoundUri);
        Intent resultIntent = new Intent(this, MainActivity.class);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(1, notificationBuilder.build());
    }
}
