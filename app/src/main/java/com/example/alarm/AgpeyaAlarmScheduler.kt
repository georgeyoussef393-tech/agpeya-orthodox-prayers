package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.MainActivity
import com.example.data.db.AgpeyaDatabase
import com.example.data.model.PrayerId
import com.example.data.repository.AgpeyaRepository
import com.example.localization.AppLanguage
import com.example.receiver.AgpeyaAlarmReceiver
import com.example.service.AgpeyaNotificationHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Metadata representation of a prayer alarm scheduled with user-defined timezone.
 */
data class ScheduledPrayerAlarmInfo(
    val prayerId: PrayerId,
    val prayerDisplayName: String,
    val scheduledHour: Int,
    val scheduledMinute: Int,
    val targetTimezoneId: String,
    val targetTimezoneDisplayName: String,
    val deviceTimezoneId: String,
    val triggerTimeMillis: Long,
    val formattedTimeInTargetTimezone: String,
    val formattedTimeInDeviceTimezone: String,
    val isDeviceTimezoneDifferent: Boolean,
    val timeRemainingMillis: Long,
    val formattedTimeRemaining: String,
    val isExactAlarmAllowed: Boolean,
    val isEnabled: Boolean
)

/**
 * Production-grade AlarmManager scheduler for Coptic Agpeya canonical prayers
 * with complete support for user-defined timezones, daylight saving time (DST),
 * lead-time alerts, and graceful exact-alarm fallback.
 */
object AgpeyaAlarmScheduler {
    private const val TAG = "AgpeyaAlarmScheduler"
    const val ACTION_PRAYER_ALARM = "com.example.agpeya.ACTION_PRAYER_ALARM"

    /**
     * Checks whether the application is currently allowed to schedule exact alarms.
     */
    fun canScheduleExactAlarms(context: Context): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    /**
     * Calculates the exact next trigger time in epoch milliseconds (UTC)
     * given the prayer's target hour and minute in a specified target timezone.
     */
    fun calculateNextTriggerTimeMillis(
        hour: Int,
        minute: Int,
        targetTimezone: TimeZone,
        leadTimeMinutes: Int = 0,
        referenceNowMillis: Long = System.currentTimeMillis()
    ): Long {
        val nowInTz = Calendar.getInstance(targetTimezone).apply {
            timeInMillis = referenceNowMillis
        }
        val targetInTz = Calendar.getInstance(targetTimezone).apply {
            timeInMillis = referenceNowMillis
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Apply lead-time offset if user configured advance notice
        if (leadTimeMinutes > 0) {
            targetInTz.add(Calendar.MINUTE, -leadTimeMinutes)
        }

        // If target time is in the past or right now, roll over to the next day in the target timezone
        if (targetInTz.timeInMillis <= nowInTz.timeInMillis) {
            targetInTz.add(Calendar.DAY_OF_YEAR, 1)
        }

        return targetInTz.timeInMillis
    }

    /**
     * Schedules a single canonical prayer alarm using AlarmManager in the user-defined timezone.
     *
     * @param context Application or activity context
     * @param prayerId The canonical prayer to schedule
     * @param hour Hour of day (0-23) in target timezone
     * @param minute Minute (0-59) in target timezone
     * @param timezoneId Optional user-defined timezone ID (e.g. "Africa/Cairo"). If omitted, uses active setting.
     * @param leadTimeMinutes Optional advance alert minutes. If omitted, uses repository preference.
     */
    fun scheduleAlarm(
        context: Context,
        prayerId: PrayerId,
        hour: Int,
        minute: Int,
        timezoneId: String? = null,
        leadTimeMinutes: Int? = null
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, AgpeyaAlarmReceiver::class.java).apply {
            action = ACTION_PRAYER_ALARM
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

        // Resolve active target timezone (user-defined or auto-detected)
        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)

        val activeTzId = timezoneId ?: if (repository.isAutoTimezoneSyncEnabled()) {
            TimeZone.getDefault().id
        } else {
            repository.getSelectedTimezoneId()
        }
        val targetTimezone = TimeZone.getTimeZone(activeTzId)
        val effectiveLeadTime = leadTimeMinutes ?: repository.getNotificationLeadTimeMinutes()

        val triggerTimeMillis = calculateNextTriggerTimeMillis(
            hour = hour,
            minute = minute,
            targetTimezone = targetTimezone,
            leadTimeMinutes = effectiveLeadTime
        )

        try {
            val showIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("EXTRA_PRAYER_CODE", prayerId.code)
            }
            val showPendingIntent = PendingIntent.getActivity(
                context,
                prayerId.ordinal + 1000,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    val clockInfo = AlarmManager.AlarmClockInfo(triggerTimeMillis, showPendingIntent)
                    alarmManager.setAlarmClock(clockInfo, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTimeMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val clockInfo = AlarmManager.AlarmClockInfo(triggerTimeMillis, showPendingIntent)
                alarmManager.setAlarmClock(clockInfo, pendingIntent)
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            }
            Log.d(TAG, "Scheduled alarm for ${prayerId.code} at $triggerTimeMillis in timezone ${targetTimezone.id}")
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException scheduling exact alarm: ${e.message}")
            try {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMillis,
                    pendingIntent
                )
            } catch (ex: Exception) {
                Log.e(TAG, "Failed fallback alarm: ${ex.message}")
            }
        }
    }

    /**
     * Cancels any pending alarm for the given canonical prayer.
     */
    fun cancelAlarm(context: Context, prayerId: PrayerId) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, AgpeyaAlarmReceiver::class.java).apply {
            action = ACTION_PRAYER_ALARM
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

    /**
     * Cancels all scheduled prayer alarms.
     */
    fun cancelAllAlarms(context: Context) {
        PrayerId.canonicalPrayers.forEach { prayerId ->
            cancelAlarm(context, prayerId)
        }
    }

    /**
     * Reschedules all active prayer alarms according to the user-defined timezone.
     */
    fun rescheduleAllActiveAlarms(context: Context, overrideTimezoneId: String? = null) {
        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val settings = repository.getAllAlarmSettings()

        for (setting in settings) {
            val prayerId = PrayerId.fromCode(setting.prayerCode)
            if (setting.isEnabled) {
                scheduleAlarm(
                    context = context,
                    prayerId = prayerId,
                    hour = setting.hour,
                    minute = setting.minute,
                    timezoneId = overrideTimezoneId
                )
            } else {
                cancelAlarm(context, prayerId)
            }
        }
    }

    /**
     * Retrieves detailed scheduling info for a specific prayer alarm.
     */
    fun getNextScheduledAlarm(
        context: Context,
        prayerId: PrayerId,
        lang: AppLanguage = AppLanguage.ARABIC
    ): ScheduledPrayerAlarmInfo? {
        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val setting = repository.getAlarmSetting(prayerId)
        if (!setting.isEnabled) return null

        val activeTzId = if (repository.isAutoTimezoneSyncEnabled()) {
            TimeZone.getDefault().id
        } else {
            repository.getSelectedTimezoneId()
        }
        val targetTz = TimeZone.getTimeZone(activeTzId)
        val deviceTz = TimeZone.getDefault()
        val isExactAllowed = canScheduleExactAlarms(context)
        val leadTime = repository.getNotificationLeadTimeMinutes()
        val now = System.currentTimeMillis()

        val triggerMillis = calculateNextTriggerTimeMillis(
            hour = setting.hour,
            minute = setting.minute,
            targetTimezone = targetTz,
            leadTimeMinutes = leadTime,
            referenceNowMillis = now
        )
        val diff = triggerMillis - now

        val tzFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply {
            timeZone = targetTz
        }
        val deviceFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply {
            timeZone = deviceTz
        }

        return ScheduledPrayerAlarmInfo(
            prayerId = prayerId,
            prayerDisplayName = prayerId.getDisplayName(lang),
            scheduledHour = setting.hour,
            scheduledMinute = setting.minute,
            targetTimezoneId = targetTz.id,
            targetTimezoneDisplayName = targetTz.getDisplayName(targetTz.inDaylightTime(Date(triggerMillis)), TimeZone.SHORT, Locale.getDefault()),
            deviceTimezoneId = deviceTz.id,
            triggerTimeMillis = triggerMillis,
            formattedTimeInTargetTimezone = tzFormatter.format(Date(triggerMillis)),
            formattedTimeInDeviceTimezone = deviceFormatter.format(Date(triggerMillis)),
            isDeviceTimezoneDifferent = targetTz.id != deviceTz.id,
            timeRemainingMillis = diff,
            formattedTimeRemaining = formatTimeRemaining(diff, lang),
            isExactAlarmAllowed = isExactAllowed,
            isEnabled = true
        )
    }

    /**
     * Returns a list of all currently scheduled prayer alarms sorted chronologically by next trigger time.
     */
    fun getAllScheduledAlarms(
        context: Context,
        lang: AppLanguage = AppLanguage.ARABIC
    ): List<ScheduledPrayerAlarmInfo> {
        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val settings = repository.getAllAlarmSettings()

        val activeTzId = if (repository.isAutoTimezoneSyncEnabled()) {
            TimeZone.getDefault().id
        } else {
            repository.getSelectedTimezoneId()
        }
        val targetTz = TimeZone.getTimeZone(activeTzId)
        val deviceTz = TimeZone.getDefault()
        val isExactAllowed = canScheduleExactAlarms(context)
        val leadTime = repository.getNotificationLeadTimeMinutes()
        val now = System.currentTimeMillis()

        val tzFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply {
            timeZone = targetTz
        }
        val deviceFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault()).apply {
            timeZone = deviceTz
        }

        return settings.filter { it.isEnabled }.map { setting ->
            val prayerId = PrayerId.fromCode(setting.prayerCode)
            val triggerMillis = calculateNextTriggerTimeMillis(
                hour = setting.hour,
                minute = setting.minute,
                targetTimezone = targetTz,
                leadTimeMinutes = leadTime,
                referenceNowMillis = now
            )
            val diff = triggerMillis - now

            ScheduledPrayerAlarmInfo(
                prayerId = prayerId,
                prayerDisplayName = prayerId.getDisplayName(lang),
                scheduledHour = setting.hour,
                scheduledMinute = setting.minute,
                targetTimezoneId = targetTz.id,
                targetTimezoneDisplayName = targetTz.getDisplayName(targetTz.inDaylightTime(Date(triggerMillis)), TimeZone.SHORT, Locale.getDefault()),
                deviceTimezoneId = deviceTz.id,
                triggerTimeMillis = triggerMillis,
                formattedTimeInTargetTimezone = tzFormatter.format(Date(triggerMillis)),
                formattedTimeInDeviceTimezone = deviceFormatter.format(Date(triggerMillis)),
                isDeviceTimezoneDifferent = targetTz.id != deviceTz.id,
                timeRemainingMillis = diff,
                formattedTimeRemaining = formatTimeRemaining(diff, lang),
                isExactAlarmAllowed = isExactAllowed,
                isEnabled = setting.isEnabled
            )
        }.sortedBy { it.triggerTimeMillis }
    }

    /**
     * Returns the very next upcoming scheduled prayer alert across all active prayers.
     */
    fun getNextUpcomingPrayerAlarm(
        context: Context,
        lang: AppLanguage = AppLanguage.ARABIC
    ): ScheduledPrayerAlarmInfo? {
        val all = getAllScheduledAlarms(context, lang)
        return all.minByOrNull { it.triggerTimeMillis }
    }

    /**
     * Directly triggers an immediate test notification with haptic vibration
     * and audio chime for a given canonical prayer so the user can verify their settings.
     */
    fun testImmediateAlarmNotification(context: Context, prayerId: PrayerId) {
        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val setting = repository.getAlarmSetting(prayerId)
        val lang = repository.getSavedLanguage()

        AgpeyaNotificationHelper.showPrayerNotification(
            context = context,
            prayerId = prayerId,
            lang = lang,
            soundEnabled = setting.soundEnabled,
            vibrateEnabled = setting.vibrateEnabled,
            soundId = setting.soundId
        )
    }

    /**
     * Formats remaining time into human-friendly representation.
     */
    fun formatTimeRemaining(diffMillis: Long, lang: AppLanguage): String {
        if (diffMillis <= 0) {
            return if (lang == AppLanguage.ARABIC) "الآن" else "Now"
        }
        val totalMinutes = (diffMillis / (1000 * 60)).coerceAtLeast(1)
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return if (lang == AppLanguage.ARABIC) {
            when {
                hours > 0 && minutes > 0 -> "بعد $hours س و $minutes د"
                hours > 0 -> "بعد $hours ساعة"
                else -> "بعد $minutes دقيقة"
            }
        } else {
            when {
                hours > 0 && minutes > 0 -> "in ${hours}h ${minutes}m"
                hours > 0 -> "in ${hours}h"
                else -> "in ${minutes}m"
            }
        }
    }
}
