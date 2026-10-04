package com.example.service

import android.content.Context
import android.util.Log
import com.example.alarm.AgpeyaAlarmScheduler
import com.example.data.db.AgpeyaDatabase
import com.example.data.repository.AgpeyaRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class TimezoneSyncResult(
    val previousTimezoneId: String?,
    val currentTimezoneId: String,
    val currentTimezoneDisplayName: String,
    val gmtOffsetString: String,
    val isDaylightSaving: Boolean,
    val localTimeFormatted: String,
    val scheduledAlarmsCount: Int,
    val hasTimezoneChanged: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Service that automatically detects the user's current timezone and adjusts
 * the hourly Agpeya prayer reminders accordingly.
 */
object AgpeyaTimezoneService {
    private const val TAG = "AgpeyaTimezoneService"
    private const val PREFS_NAME = "agpeya_timezone_service_prefs"
    private const val KEY_LAST_TZ_ID = "last_known_timezone_id"
    private const val KEY_LAST_TZ_OFFSET = "last_known_timezone_offset"
    private const val KEY_LAST_SYNC_TIMESTAMP = "last_timezone_sync_timestamp"

    /**
     * Automatically detects device timezone, verifies against last known state,
     * adjusts all active Agpeya canonical prayer alarms accordingly, and posts
     * a system notification if the timezone changed during travel.
     */
    fun detectAndAdjustTimezone(
        context: Context,
        isSystemBroadcast: Boolean = false,
        forceReschedule: Boolean = false
    ): TimezoneSyncResult {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastTzId = prefs.getString(KEY_LAST_TZ_ID, null)
        val lastOffset = prefs.getInt(KEY_LAST_TZ_OFFSET, Int.MIN_VALUE)

        val currentTz = TimeZone.getDefault()
        val currentTzId = currentTz.id
        val currentOffset = currentTz.rawOffset
        val isDst = currentTz.inDaylightTime(Date())
        val now = Date()

        val offsetHours = (currentOffset + (if (isDst) currentTz.dstSavings else 0)) / (1000 * 60 * 60)
        val sign = if (offsetHours >= 0) "+$offsetHours" else "$offsetHours"
        val gmtOffsetStr = "UTC$sign"
        val displayName = currentTz.getDisplayName(isDst, TimeZone.LONG, Locale.getDefault())

        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val localTimeStr = sdf.format(now)

        val hasChanged = (lastTzId != null && lastTzId != currentTzId) ||
                (lastOffset != Int.MIN_VALUE && lastOffset != currentOffset)

        Log.d(TAG, "Timezone check: current=$currentTzId ($gmtOffsetStr), previous=$lastTzId, changed=$hasChanged, isSystemBroadcast=$isSystemBroadcast")

        // Save new state
        prefs.edit()
            .putString(KEY_LAST_TZ_ID, currentTzId)
            .putInt(KEY_LAST_TZ_OFFSET, currentOffset)
            .putLong(KEY_LAST_SYNC_TIMESTAMP, System.currentTimeMillis())
            .apply()

        // Reschedule alarms if timezone changed or explicitly requested
        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val alarmSettings = repository.getAllAlarmSettings()

        if (hasChanged || forceReschedule || lastTzId == null) {
            // Update repository selected timezone if auto-sync enabled
            if (repository.isAutoTimezoneSyncEnabled()) {
                repository.saveSelectedTimezoneId(currentTzId)
            }

            // Reschedule all exact prayer alarms in the newly detected timezone
            AgpeyaAlarmScheduler.rescheduleAllActiveAlarms(context)

            // If triggered by system broadcast (e.g. user flew to another country) and timezone changed, notify user
            if (hasChanged && (isSystemBroadcast || repository.isAutoTimezoneSyncEnabled())) {
                val lang = repository.getSavedLanguage()
                AgpeyaNotificationHelper.showTimezoneChangedNotification(
                    context = context,
                    tzDisplayName = displayName,
                    gmtOffset = gmtOffsetStr,
                    lang = lang
                )
            }
        }

        val activeCount = alarmSettings.count { it.isEnabled }

        return TimezoneSyncResult(
            previousTimezoneId = lastTzId,
            currentTimezoneId = currentTzId,
            currentTimezoneDisplayName = displayName,
            gmtOffsetString = gmtOffsetStr,
            isDaylightSaving = isDst,
            localTimeFormatted = localTimeStr,
            scheduledAlarmsCount = activeCount,
            hasTimezoneChanged = hasChanged
        )
    }

    /**
     * Retrieves current timezone metadata without force rescheduling unless needed.
     */
    fun getCurrentTimezoneInfo(context: Context): TimezoneSyncResult {
        val currentTz = TimeZone.getDefault()
        val isDst = currentTz.inDaylightTime(Date())
        val offsetHours = (currentTz.rawOffset + (if (isDst) currentTz.dstSavings else 0)) / (1000 * 60 * 60)
        val sign = if (offsetHours >= 0) "+$offsetHours" else "$offsetHours"
        val gmtOffsetStr = "UTC$sign"
        val displayName = currentTz.getDisplayName(isDst, TimeZone.LONG, Locale.getDefault())
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastTzId = prefs.getString(KEY_LAST_TZ_ID, null)

        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val activeCount = repository.getAllAlarmSettings().count { it.isEnabled }

        return TimezoneSyncResult(
            previousTimezoneId = lastTzId,
            currentTimezoneId = currentTz.id,
            currentTimezoneDisplayName = displayName,
            gmtOffsetString = gmtOffsetStr,
            isDaylightSaving = isDst,
            localTimeFormatted = sdf.format(Date()),
            scheduledAlarmsCount = activeCount,
            hasTimezoneChanged = false
        )
    }
}
