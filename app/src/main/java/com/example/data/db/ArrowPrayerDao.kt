package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ArrowPrayerLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArrowPrayerDao {
    @Query("SELECT * FROM arrow_prayer_logs ORDER BY timestamp DESC")
    fun getAllArrowLogs(): Flow<List<ArrowPrayerLogEntity>>

    @Query("SELECT * FROM arrow_prayer_logs ORDER BY timestamp DESC")
    suspend fun getAllArrowLogsList(): List<ArrowPrayerLogEntity>

    @Query("SELECT * FROM arrow_prayer_logs WHERE dateString = :dateString ORDER BY timestamp DESC")
    fun getArrowLogsForDate(dateString: String): Flow<List<ArrowPrayerLogEntity>>

    @Query("SELECT * FROM arrow_prayer_logs WHERE year = :year AND month = :month ORDER BY timestamp DESC")
    fun getArrowLogsForMonth(year: Int, month: Int): Flow<List<ArrowPrayerLogEntity>>

    @Query("SELECT * FROM arrow_prayer_logs WHERE year = :year AND quarter = :quarter ORDER BY timestamp DESC")
    fun getArrowLogsForQuarter(year: Int, quarter: Int): Flow<List<ArrowPrayerLogEntity>>

    @Query("SELECT * FROM arrow_prayer_logs WHERE year = :year AND halfYear = :halfYear ORDER BY timestamp DESC")
    fun getArrowLogsForHalfYear(year: Int, halfYear: Int): Flow<List<ArrowPrayerLogEntity>>

    @Query("SELECT * FROM arrow_prayer_logs WHERE year = :year ORDER BY timestamp DESC")
    fun getArrowLogsForYear(year: Int): Flow<List<ArrowPrayerLogEntity>>

    @Query("SELECT COALESCE(SUM(count), 0) FROM arrow_prayer_logs")
    fun getTotalArrowPrayersCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM arrow_prayer_logs")
    fun getTotalSessionsCount(): Flow<Int>

    @Query("SELECT * FROM arrow_prayer_logs WHERE dateString = :dateString AND prayerText = :prayerText ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestArrowLogForDateAndText(dateString: String, prayerText: String): ArrowPrayerLogEntity?

    @Query("UPDATE arrow_prayer_logs SET count = count + :increment, timestamp = :timestamp WHERE id = :id")
    suspend fun incrementArrowCount(id: Long, increment: Int, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArrowLog(log: ArrowPrayerLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArrowLogs(logs: List<ArrowPrayerLogEntity>)

    @Query("DELETE FROM arrow_prayer_logs WHERE id = :id")
    suspend fun deleteArrowLogById(id: Long)

    @Query("DELETE FROM arrow_prayer_logs")
    suspend fun clearAllArrowLogs()
}
