package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity to persist Arrow Prayer (الصلوات السهمية / صلاة يسوع) sessions and counts
 * across Daily, Monthly, Quarterly, Semi-Annual, and Annual reports.
 */
@Entity(
    tableName = "arrow_prayer_logs",
    indices = [
        Index(value = ["dateString"]),
        Index(value = ["year", "month"]),
        Index(value = ["year", "quarter"]),
        Index(value = ["year", "halfYear"]),
        Index(value = ["year"])
    ]
)
data class ArrowPrayerLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val count: Int, // Number of repetitions (e.g., 33, 50, 100 beads)
    val prayerText: String, // e.g. "يارب يسوع المسيح، ابن الله، ارحمني أنا الخاطئ"
    val timestamp: Long = System.currentTimeMillis(),
    val dateString: String, // format: "yyyy-MM-dd"
    val year: Int,
    val month: Int, // 1..12
    val quarter: Int, // 1..4 (Q1: 1-3, Q2: 4-6, Q3: 7-9, Q4: 10-12)
    val halfYear: Int, // 1..2 (H1: 1-6, H2: 7-12)
    val day: Int,
    val hour: Int,
    val minute: Int
)
