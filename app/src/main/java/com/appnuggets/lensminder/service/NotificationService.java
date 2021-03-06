package com.appnuggets.lensminder.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.appnuggets.lensminder.activity.ReminderBroadcast;
import com.appnuggets.lensminder.model.NotificationCode;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationService {

    public static void createNotification(Context context, long leftDays, NotificationCode notificationId){
        Intent intent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                notificationId.getCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.DATE, (int)leftDays);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 9);

        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTime().getTime(), pendingIntent );
    }

    public static void cancelNotification(Context context, NotificationCode notificationId) {
        Intent myIntent = new Intent(context, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                notificationId.getCode(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
