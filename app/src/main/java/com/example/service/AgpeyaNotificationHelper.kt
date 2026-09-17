package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.model.PrayerId
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage

object AgpeyaNotificationHelper {
    const val CHANNEL_ID = "agpeya_prayers_channel"
    private const val CHANNEL_NAME = "Agpeya Prayer Alerts"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val existing = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existing == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Timely reminders for Coptic Agpeya canonical prayers"
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 350, 200, 350, 200, 500)
                    lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    fun showPrayerNotification(
        context: Context,
        prayerId: PrayerId,
        lang: AppLanguage,
        soundEnabled: Boolean = true,
        vibrateEnabled: Boolean = true,
        soundId: String = "church_bells"
    ) {
        createNotificationChannel(context)

        val prayerName = prayerId.getDisplayName(lang)
        val title = AgpeyaStrings.alarmNotificationTitle(prayerName, lang)
        val verseText = prayerId.getKeyVerse(lang)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("EXTRA_PRAYER_CODE", prayerId.code)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            prayerId.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(verseText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(verseText + "\n\n" + prayerId.getSpiritualTheme(lang)))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (soundEnabled) {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            builder.setSound(defaultSoundUri)
            // Also trigger spiritual synthesizer chime if custom sound selected
            val soundEnum = com.example.audio.SpiritualSound.fromId(soundId)
            com.example.audio.SpiritualAudioPlayer.playPreview(context, soundEnum)
        }
        if (vibrateEnabled) {
            builder.setVibrate(longArrayOf(0, 400, 250, 400))
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(prayerId.ordinal + 100, builder.build())
    }

    fun showTestNotification(context: Context, lang: AppLanguage) {
        createNotificationChannel(context)

        val title = "✝ " + AgpeyaStrings.appTitle(lang) + " - " + AgpeyaStrings.testAlert(lang)
        val body = AgpeyaStrings.alarmNotificationBody(lang)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            999,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 300, 200, 300))

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(999, builder.build())
    }
}
