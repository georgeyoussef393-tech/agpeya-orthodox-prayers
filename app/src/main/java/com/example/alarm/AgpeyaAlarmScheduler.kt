package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.db.AgpeyaDatabase
import com.example.data.model.PrayerId
import com.example.data.repository.AgpeyaRepository
import com.example.receiver.AgpeyaAlarmReceiver
import java.util.Calendar

object AgpeyaAlarmScheduler {
    private const val TAG = "AgpeyaAlarmScheduler"

    fun scheduleAlarm(
        context: Context,
        prayerId: PrayerId,
        hour: Int,
        minute: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AgpeyaAlarmReceiver::class.java).apply {
            action = "com.example.agpeya.ACTION_PRAYER_ALARM"
            putExtra("EXTRA_PRAYER_CODE", prayerId.code)
            putExtra("EXTRA_HOUR", hour)
            putExtra("EXTRA_MINUTE", minute)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prayerId.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculate trigger time in device's local timezone
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If target time is before or equal to now, schedule for tomorrow
        if (target.timeInMillis <= now.timeInMillis) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        target.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        target.timeInMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    target.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    target.timeInMillis,
                    pendingIntent
                )
            }
            Log.d(TAG, "Scheduled alarm for ${prayerId.code} at ${target.time}")
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException scheduling exact alarm: ${e.message}")
            try {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    target.timeInMillis,
                    pendingIntent
                )
            } catch (ex: Exception) {
                Log.e(TAG, "Failed fallback alarm: ${ex.message}")
            }
        }
    }

    fun cancelAlarm(context: Context, prayerId: PrayerId) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AgpeyaAlarmReceiver::class.java).apply {
            action = "com.example.agpeya.ACTION_PRAYER_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prayerId.ordinal,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
        Log.d(TAG, "Cancelled alarm for ${prayerId.code}")
    }

    fun rescheduleAllActiveAlarms(context: Context) {
        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val settings = repository.getAllAlarmSettings()

        for (setting in settings) {
            val prayerId = PrayerId.fromCode(setting.prayerCode)
            if (setting.isEnabled) {
                scheduleAlarm(context, prayerId, setting.hour, setting.minute)
            } else {
                cancelAlarm(context, prayerId)
            }
        }
    }
}
