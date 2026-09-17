package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.model.PrayerId
import com.example.localization.AgpeyaStrings
import com.example.localization.AppLanguage

object AgpeyaNotificationHelper {
    const val CHANNEL_ID = "agpeya_prayers_channel_v2"
    private const val CHANNEL_NAME = "Agpeya Prayer Alerts"

    private val PRAYER_VIBRATION_PATTERN = longArrayOf(0, 450, 200, 450, 200, 650)

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
                    vibrationPattern = PRAYER_VIBRATION_PATTERN
                    lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    /**
     * Directly triggers hardware haptic vibration so the phone vibrates reliably
     * alongside the spiritual audio chime even if notification channel settings were muted by the system.
     */
    fun triggerDeviceVibration(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                val effect = VibrationEffect.createWaveform(PRAYER_VIBRATION_PATTERN, -1)
                vibrator?.vibrate(effect)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                val effect = VibrationEffect.createWaveform(PRAYER_VIBRATION_PATTERN, -1)
                vibrator?.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(PRAYER_VIBRATION_PATTERN, -1)
            }
        } catch (e: Exception) {
            Log.w("AgpeyaNotificationHelper", "Could not trigger hardware vibrator: ${e.message}")
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
            builder.setVibrate(PRAYER_VIBRATION_PATTERN)
            // Directly trigger device vibrator to guarantee physical haptic vibration with sound
            triggerDeviceVibration(context)
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
            .setVibrate(PRAYER_VIBRATION_PATTERN)

        // Play sound and trigger physical vibration for test
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setSound(defaultSoundUri)
        com.example.audio.SpiritualAudioPlayer.playPreview(context, com.example.audio.SpiritualSound.CHURCH_BELLS)
        triggerDeviceVibration(context)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(999, builder.build())
    }
}
