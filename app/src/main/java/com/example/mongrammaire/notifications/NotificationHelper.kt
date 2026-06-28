package com.example.mongrammaire.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.orhanobut.hawk.Hawk
import java.util.*

object NotificationHelper {
    private const val CHANNEL_ID = "daily_motivation"
    private const val PREF_NOTIFICATIONS_ENABLED = "notifications_enabled"

    @JvmStatic
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Motivations Quotidiennes"
            val descriptionText = "Notifications pour vous motiver à continuer vos leçons."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @JvmStatic
    fun scheduleDailyNotification(context: Context) {
        if (!isNotificationsEnabled()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 18) // Schedule for 6 PM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        // If time is in the past, add one day
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Use inexact alarm for motivational notifications to avoid SecurityException on Android 12+
        // and be more battery-efficient.
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    @JvmStatic
    fun cancelNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    @JvmStatic
    fun setNotificationsEnabled(enabled: Boolean) {
        Hawk.put(PREF_NOTIFICATIONS_ENABLED, enabled)
    }

    @JvmStatic
    fun isNotificationsEnabled(): Boolean {
        return Hawk.get(PREF_NOTIFICATIONS_ENABLED, true)
    }
}
