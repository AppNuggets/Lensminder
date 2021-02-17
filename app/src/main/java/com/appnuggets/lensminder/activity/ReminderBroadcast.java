package com.appnuggets.lensminder.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.appnuggets.lensminder.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                context.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.ic_lens)
                .setContentTitle(context.getString(R.string.expiration_notification_title))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(context.getString(R.string.expiration_notification_message)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
