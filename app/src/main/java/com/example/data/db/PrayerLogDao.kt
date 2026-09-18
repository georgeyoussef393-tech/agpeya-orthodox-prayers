package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PrayerLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerLogDao {
    @Query("SELECT * FROM prayer_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<PrayerLogEntity>>

    @Query("SELECT * FROM prayer_logs ORDER BY timestamp DESC")
    suspend fun getAllLogsList(): List<PrayerLogEntity>

    @Query("SELECT * FROM prayer_logs WHERE dateString = :dateString ORDER BY timestamp ASC")
    fun getLogsForDate(dateString: String): Flow<List<PrayerLogEntity>>

    @Query("SELECT * FROM prayer_logs WHERE year = :year AND month = :month ORDER BY timestamp DESC")
    fun getLogsForMonth(year: Int, month: Int): Flow<List<PrayerLogEntity>>

    @Query("SELECT * FROM prayer_logs WHERE year = :year ORDER BY timestamp DESC")
    fun getLogsForYear(year: Int): Flow<List<PrayerLogEntity>>

    @Query("SELECT COUNT(*) FROM prayer_logs")
    fun getTotalLogsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM prayer_logs WHERE dateString = :dateString AND prayerCode = :prayerCode")
    suspend fun getCountForPrayerOnDate(prayerCode: String, dateString: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: PrayerLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<PrayerLogEntity>)

    @Query("DELETE FROM prayer_logs WHERE id = :id")
    suspend fun deleteLogById(id: Long)

    @Query("DELETE FROM prayer_logs WHERE prayerCode = :prayerCode AND dateString = :dateString")
    suspend fun deletePrayerOnDate(prayerCode: String, dateString: String)

    @Query("DELETE FROM prayer_logs")
    suspend fun clearAllLogs()
}
