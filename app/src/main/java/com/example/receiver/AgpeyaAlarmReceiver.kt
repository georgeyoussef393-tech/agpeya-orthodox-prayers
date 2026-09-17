package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alarm.AgpeyaAlarmScheduler
import com.example.data.db.AgpeyaDatabase
import com.example.data.model.PrayerId
import com.example.data.repository.AgpeyaRepository
import com.example.service.AgpeyaNotificationHelper

class AgpeyaAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prayerCode = intent.getStringExtra("EXTRA_PRAYER_CODE") ?: return
        val hour = intent.getIntExtra("EXTRA_HOUR", -1)
        val minute = intent.getIntExtra("EXTRA_MINUTE", -1)

        val prayerId = PrayerId.fromCode(prayerCode)
        Log.d("AgpeyaAlarmReceiver", "Received alarm for prayer: $prayerCode")

        val database = AgpeyaDatabase.getDatabase(context)
        val repository = AgpeyaRepository(database.prayerLogDao(), context)
        val setting = repository.getAlarmSetting(prayerId)
        val lang = repository.getSavedLanguage()

        if (setting.isEnabled) {
            AgpeyaNotificationHelper.showPrayerNotification(
                context = context,
                prayerId = prayerId,
                lang = lang,
                soundEnabled = setting.soundEnabled,
                vibrateEnabled = setting.vibrateEnabled,
                soundId = setting.soundId
            )

            // Reschedule for next day at the configured hour and minute
            val targetHour = if (hour >= 0) hour else setting.hour
            val targetMinute = if (minute >= 0) minute else setting.minute
            AgpeyaAlarmScheduler.scheduleAlarm(context, prayerId, targetHour, targetMinute)
        }
    }
}
