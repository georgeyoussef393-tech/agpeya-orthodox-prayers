package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.db.PrayerLogDao
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerId
import com.example.data.model.PrayerLogEntity
import com.example.data.sync.FirestoreSyncManager
import com.example.localization.AppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AgpeyaRepository(
    private val dao: PrayerLogDao,
    private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("agpeya_settings_prefs", Context.MODE_PRIVATE)

    val syncManager = FirestoreSyncManager(context)

    // Auth & Onboarding state
    fun isAuthOnboardingCompleted(): Boolean {
        return prefs.getBoolean("auth_onboarding_completed", false)
    }

    fun setAuthOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("auth_onboarding_completed", completed).apply()
    }

    fun getUserEmail(): String? {
        return syncManager.userEmail.value ?: prefs.getString("user_email", null)
    }

    fun setUserEmail(email: String?, onComplete: () -> Unit = {}) {
        syncManager.setUserEmail(email, onComplete)
    }

    suspend fun clearAllLocalLogs() {
        dao.clearAllLogs()
    }

    // Language
    fun getSavedLanguage(): AppLanguage {
        val code = prefs.getString("app_language_code", null)
        if (code != null) {
            return AppLanguage.fromCode(code)
        }
        // Fallback to device system language if matched
        val systemLang = Locale.getDefault().language
        return AppLanguage.fromCode(systemLang)
    }

    fun saveLanguage(language: AppLanguage) {
        prefs.edit().putString("app_language_code", language.code).apply()
        CoroutineScope(Dispatchers.IO).launch {
            syncManager.syncSettings(language.code, getAllAlarmSettings()) { _, _ -> }
        }
    }

    // Alarm Settings
    fun getAlarmSetting(prayerId: PrayerId): PrayerAlarmSetting {
        val prefix = "alarm_${prayerId.code}"
        val hour = prefs.getInt("${prefix}_hour", prayerId.defaultHour)
        val minute = prefs.getInt("${prefix}_minute", prayerId.defaultMinute)
        val isEnabled = prefs.getBoolean("${prefix}_enabled", prayerId != PrayerId.VEIL) // Veil disabled by default as it's monastic
        val sound = prefs.getBoolean("${prefix}_sound", true)
        val vibrate = prefs.getBoolean("${prefix}_vibrate", true)
        val soundId = prefs.getString("${prefix}_sound_id", "church_bells") ?: "church_bells"

        return PrayerAlarmSetting(
            prayerCode = prayerId.code,
            hour = hour,
            minute = minute,
            isEnabled = isEnabled,
            soundEnabled = sound,
            vibrateEnabled = vibrate,
            soundId = soundId
        )
    }

    fun saveAlarmSetting(setting: PrayerAlarmSetting) {
        val prefix = "alarm_${setting.prayerCode}"
        prefs.edit()
            .putInt("${prefix}_hour", setting.hour)
            .putInt("${prefix}_minute", setting.minute)
            .putBoolean("${prefix}_enabled", setting.isEnabled)
            .putBoolean("${prefix}_sound", setting.soundEnabled)
            .putBoolean("${prefix}_vibrate", setting.vibrateEnabled)
            .putString("${prefix}_sound_id", setting.soundId)
            .apply()

        CoroutineScope(Dispatchers.IO).launch {
            syncManager.syncSettings(getSavedLanguage().code, getAllAlarmSettings()) { _, _ -> }
        }
    }

    fun getAllAlarmSettings(): List<PrayerAlarmSetting> {
        return PrayerId.canonicalPrayers.map { getAlarmSetting(it) }
    }

    // Prayer Logs (Room + Cloud Sync)
    fun getAllLogs(): Flow<List<PrayerLogEntity>> = dao.getAllLogs()

    fun getLogsForDate(dateString: String): Flow<List<PrayerLogEntity>> = dao.getLogsForDate(dateString)

    fun getLogsForMonth(year: Int, month: Int): Flow<List<PrayerLogEntity>> = dao.getLogsForMonth(year, month)

    fun getLogsForYear(year: Int): Flow<List<PrayerLogEntity>> = dao.getLogsForYear(year)

    fun getTotalLogsCount(): Flow<Int> = dao.getTotalLogsCount()

    suspend fun isPrayerPrayedOnDate(prayerCode: String, dateString: String): Boolean {
        return dao.getCountForPrayerOnDate(prayerCode, dateString) > 0
    }

    suspend fun logPrayer(prayerId: PrayerId, timestamp: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        val dateString = formatDate(cal.time)
        val log = PrayerLogEntity(
            prayerCode = prayerId.code,
            timestamp = timestamp,
            dateString = dateString,
            year = cal.get(Calendar.YEAR),
            month = cal.get(Calendar.MONTH) + 1,
            day = cal.get(Calendar.DAY_OF_MONTH),
            hour = cal.get(Calendar.HOUR_OF_DAY),
            minute = cal.get(Calendar.MINUTE)
        )
        val id = dao.insertLog(log)
        // Upload to Firestore for cross-device sync
        syncManager.uploadPrayerLog(log.copy(id = id))
        return id
    }

    suspend fun removePrayerOnDate(prayerCode: String, dateString: String) {
        dao.deletePrayerOnDate(prayerCode, dateString)
        syncManager.deletePrayerLog(prayerCode, dateString)
    }

    suspend fun deleteLogById(id: Long) {
        dao.deleteLogById(id)
    }

    suspend fun performFullSync() {
        val localLogs = dao.getAllLogsList()
        val localKeys = localLogs.map { "${it.prayerCode}_${it.dateString}" }.toSet()
        syncManager.performFullBidirectionalSync(localLogs) { remoteLogs ->
            val toInsert = remoteLogs.filter { "${it.prayerCode}_${it.dateString}" !in localKeys }
            if (toInsert.isNotEmpty()) {
                dao.insertLogs(toInsert)
            }
        }
        syncManager.syncSettings(getSavedLanguage().code, getAllAlarmSettings()) { remoteLangCode, remoteAlarms ->
            if (remoteLangCode.isNotBlank()) {
                prefs.edit().putString("app_language_code", remoteLangCode).apply()
            }
            for (alarm in remoteAlarms) {
                saveAlarmSetting(alarm)
            }
        }
    }

    fun startRealtimeSync(scope: CoroutineScope) {
        syncManager.startRealtimeListener { remoteLog ->
            scope.launch(Dispatchers.IO) {
                val exists = dao.getCountForPrayerOnDate(remoteLog.prayerCode, remoteLog.dateString) > 0
                if (!exists) {
                    dao.insertLog(remoteLog)
                }
            }
        }
    }

    companion object {
        fun formatDate(date: Date): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(date)
        }

        fun formatReadableDate(date: Date, locale: Locale): String {
            val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", locale)
            return sdf.format(date)
        }

        fun getTodayString(): String {
            return formatDate(Date())
        }
    }
}
