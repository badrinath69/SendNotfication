package com.example.sendnotification.utils


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object NotificationScheduler {
    fun scheduleNotification(context: Context, title: String, message: String, timeInMillis: Long, notificationId: String) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }
}
