package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "prayer_logs",
    indices = [
        Index(value = ["dateString"]),
        Index(value = ["year", "month"]),
        Index(value = ["year"])
    ]
)
data class PrayerLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val prayerCode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
)

data class PrayerAlarmSetting(
    val prayerCode: String,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrateEnabled: Boolean = true,
    val soundId: String = "church_bells"
)
