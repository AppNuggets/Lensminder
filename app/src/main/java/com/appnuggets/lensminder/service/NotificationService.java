package com.appnuggets.lensminder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.appnuggets.lensminder.activity.ReminderBroadcast;
import com.appnuggets.lensminder.model.NotificationCode;

import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class NotificationService {
    public static void createNotification(Context context, Date fireDate, NotificationCode notificationId){
        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                notificationId.getCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        long timeAtButtonClick = System.currentTimeMillis();
        long tenSeconds = 1000 * 3;

        alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtonClick + tenSeconds, pendingIntent );
    }

    public static void cancelNotification(Context context, NotificationCode notificationId) {
        Intent myIntent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                notificationId.getCode(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
