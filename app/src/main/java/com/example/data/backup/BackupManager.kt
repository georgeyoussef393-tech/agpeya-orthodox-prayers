package com.example.data.backup

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.data.model.PrayerAlarmSetting
import com.example.data.model.PrayerLogEntity
import com.example.data.repository.AgpeyaRepository
import com.example.localization.AppLanguage
import com.example.data.model.PrayerId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BackupManager {
    private const val TAG = "BackupManager"

    suspend fun createBackupJsonFile(
        context: Context,
        repository: AgpeyaRepository
    ): File = withContext(Dispatchers.IO) {
        val rootObj = JSONObject()
        rootObj.put("app", "AgpeyaOrthodoxApp")
        rootObj.put("version", 1)
        rootObj.put("backupDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()))
        rootObj.put("backupTimestamp", System.currentTimeMillis())
        rootObj.put("churchName", repository.getChurchName() ?: "")
        rootObj.put("churchEmblem", repository.getChurchEmblem())
        rootObj.put("language", repository.getSavedLanguage().code)
        rootObj.put("notificationLeadTimeMinutes", repository.getNotificationLeadTimeMinutes())
        rootObj.put("quietHoursEnabled", repository.isQuietHoursEnabled())

        // Alarm Settings
        val alarmsArray = JSONArray()
        for (prayerId in PrayerId.canonicalPrayers) {
            val setting = repository.getAlarmSetting(prayerId)
            val alarmObj = JSONObject().apply {
                put("prayerCode", setting.prayerCode)
                put("hour", setting.hour)
                put("minute", setting.minute)
                put("isEnabled", setting.isEnabled)
                put("soundEnabled", setting.soundEnabled)
                put("vibrateEnabled", setting.vibrateEnabled)
                put("soundId", setting.soundId)
            }
            alarmsArray.put(alarmObj)
        }
        rootObj.put("alarmSettings", alarmsArray)

        // Prayer Logs
        val logs = repository.getAllLogsDirect()
        val logsArray = JSONArray()
        for (log in logs) {
            val logObj = JSONObject().apply {
                put("prayerCode", log.prayerCode)
                put("timestamp", log.timestamp)
                put("dateString", log.dateString)
                put("year", log.year)
                put("month", log.month)
                put("day", log.day)
                put("hour", log.hour)
                put("minute", log.minute)
            }
            logsArray.put(logObj)
        }
        rootObj.put("prayerLogs", logsArray)

        val fileTimestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "agpeya_backup_$fileTimestamp.json"
        val cacheDir = File(context.cacheDir, "backups")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        val backupFile = File(cacheDir, fileName)
        FileOutputStream(backupFile).use { out ->
            out.write(rootObj.toString(2).toByteArray(Charsets.UTF_8))
        }
        Log.i(TAG, "Created backup file: ${backupFile.absolutePath} with ${logs.size} logs")
        backupFile
    }

    suspend fun restoreBackupFromUri(
        context: Context,
        repository: AgpeyaRepository,
        uri: Uri
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val contentResolver = context.contentResolver
            val jsonString = contentResolver.openInputStream(uri)?.use { stream ->
                InputStreamReader(stream, Charsets.UTF_8).readText()
            } ?: return@withContext Result.failure(Exception("Could not open backup file"))

            val rootObj = JSONObject(jsonString)
            if (!rootObj.has("app") || rootObj.optString("app") != "AgpeyaOrthodoxApp") {
                return@withContext Result.failure(Exception("Invalid backup file format"))
            }

            // Restore Church Name
            if (rootObj.has("churchName")) {
                val churchName = rootObj.optString("churchName")
                repository.saveChurchName(if (churchName.isBlank()) null else churchName)
            }

            // Restore Church Emblem
            if (rootObj.has("churchEmblem")) {
                repository.saveChurchEmblem(rootObj.optString("churchEmblem", "coptic_cross"))
            }

            // Restore Language
            if (rootObj.has("language")) {
                val langCode = rootObj.optString("language")
                AppLanguage.fromCode(langCode)?.let { repository.saveLanguage(it) }
            }

            // Restore Notification Lead Time
            if (rootObj.has("notificationLeadTimeMinutes")) {
                repository.saveNotificationLeadTimeMinutes(rootObj.optInt("notificationLeadTimeMinutes", 0))
            }

            // Restore Alarm Settings
            if (rootObj.has("alarmSettings")) {
                val alarmsArray = rootObj.getJSONArray("alarmSettings")
                for (i in 0 until alarmsArray.length()) {
                    val alarmObj = alarmsArray.getJSONObject(i)
                    val setting = PrayerAlarmSetting(
                        prayerCode = alarmObj.getString("prayerCode"),
                        hour = alarmObj.getInt("hour"),
                        minute = alarmObj.getInt("minute"),
                        isEnabled = alarmObj.optBoolean("isEnabled", true),
                        soundEnabled = alarmObj.optBoolean("soundEnabled", true),
                        vibrateEnabled = alarmObj.optBoolean("vibrateEnabled", true),
                        soundId = alarmObj.optString("soundId", "church_bells")
                    )
                    repository.saveAlarmSetting(setting)
                }
            }

            // Restore Prayer Logs
            var restoredCount = 0
            if (rootObj.has("prayerLogs")) {
                val logsArray = rootObj.getJSONArray("prayerLogs")
                val restoredLogs = mutableListOf<PrayerLogEntity>()
                for (i in 0 until logsArray.length()) {
                    val logObj = logsArray.getJSONObject(i)
                    restoredLogs.add(
                        PrayerLogEntity(
                            id = 0, // Auto-generate new IDs
                            prayerCode = logObj.getString("prayerCode"),
                            timestamp = logObj.optLong("timestamp", System.currentTimeMillis()),
                            dateString = logObj.getString("dateString"),
                            year = logObj.getInt("year"),
                            month = logObj.getInt("month"),
                            day = logObj.getInt("day"),
                            hour = logObj.optInt("hour", 0),
                            minute = logObj.optInt("minute", 0)
                        )
                    )
                }

                if (restoredLogs.isNotEmpty()) {
                    repository.importBackupLogs(restoredLogs)
                    restoredCount = restoredLogs.size
                }
            }

            Log.i(TAG, "Successfully restored $restoredCount logs from backup")
            Result.success(restoredCount)
        } catch (e: Exception) {
            Log.e(TAG, "Error restoring backup: ${e.message}", e)
            Result.failure(e)
        }
    }
}
