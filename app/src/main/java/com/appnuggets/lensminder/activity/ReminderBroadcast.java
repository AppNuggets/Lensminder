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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "lensminderNotification")
                .setSmallIcon(R.drawable.ic_lens)
                .setContentTitle("Expiration warning")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Looks like one of your eye care products has expired... Navigate to Lensminder app and check it!"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
