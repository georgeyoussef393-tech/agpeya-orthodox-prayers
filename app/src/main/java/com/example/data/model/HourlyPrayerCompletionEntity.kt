package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room Database Entity to track completed hourly prayers (الأجبية) for each day and week.
 */
@Entity(
    tableName = "hourly_prayer_completions",
    indices = [
        Index(value = ["dateString"]),
        Index(value = ["year", "weekOfYear"]),
        Index(value = ["prayerCode", "dateString"], unique = true)
    ]
)
data class HourlyPrayerCompletionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val prayerCode: String,
    val dateString: String, // Format: YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val dayOfWeek: Int, // Calendar.SUNDAY (1) to Calendar.SATURDAY (7)
    val weekOfYear: Int,
    val durationMinutes: Int = 0,
    val isCompleted: Boolean = true,
    val notes: String? = null
)

/**
 * Summary of hourly prayers progress for a single calendar day.
 */
data class HourlyPrayerDailySummary(
    val dateString: String,
    val dayNameEn: String,
    val dayNameAr: String,
    val dayOfMonth: Int,
    val dayOfWeek: Int,
    val isToday: Boolean,
    val completedCount: Int,
    val totalPossibleCount: Int = 7,
    val completedPrayerCodes: Set<String> = emptySet()
) {
    val completionFraction: Float
        get() = if (totalPossibleCount > 0) {
            (completedCount.toFloat() / totalPossibleCount.toFloat()).coerceIn(0f, 1f)
        } else 0f

    val percentage: Int
        get() = (completionFraction * 100).toInt()
}

/**
 * Summary of hourly prayers progress across the 7 days of the current week.
 */
data class HourlyPrayerWeeklyProgress(
    val year: Int,
    val weekOfYear: Int,
    val days: List<HourlyPrayerDailySummary>,
    val totalWeeklyCompleted: Int,
    val totalWeeklyPossible: Int = 49, // 7 days * 7 canonical prayers
    val weeklyCompletionRate: Float,
    val currentStreakDays: Int,
    val bestDayNameAr: String? = null,
    val mostPrayedHourCode: String? = null
)
