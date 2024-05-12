package com.example.okmanyiroda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "okmanyiroda_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private NotificationManager mManager;
    private Context mContext;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(
            CHANNEL_ID,
            "Okmanyiroda_Notification",
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Okmanyiroda ertesites");
        this.mManager.createNotificationChannel(channel);
        Log.i(NotificationHandler.class.getName().toString(), "Channel created");
    }

    public void send(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Okmanyiroda")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_name);
        this.mManager.notify(NOTIFICATION_ID, builder.build());
        Log.i(NotificationHandler.class.getName().toString(), "message: " + message + " sent.");
    }
}
