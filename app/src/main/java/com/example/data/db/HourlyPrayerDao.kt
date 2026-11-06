package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.HourlyPrayerCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HourlyPrayerDao {
    @Query("SELECT * FROM hourly_prayer_completions ORDER BY timestamp DESC")
    fun getAllCompletions(): Flow<List<HourlyPrayerCompletionEntity>>

    @Query("SELECT * FROM hourly_prayer_completions WHERE dateString = :dateString ORDER BY timestamp ASC")
    fun getCompletionsForDate(dateString: String): Flow<List<HourlyPrayerCompletionEntity>>

    @Query("SELECT * FROM hourly_prayer_completions WHERE dateString = :dateString ORDER BY timestamp ASC")
    suspend fun getCompletionsForDateDirect(dateString: String): List<HourlyPrayerCompletionEntity>

    @Query("SELECT * FROM hourly_prayer_completions WHERE year = :year AND weekOfYear = :weekOfYear ORDER BY timestamp ASC")
    fun getCompletionsForWeek(year: Int, weekOfYear: Int): Flow<List<HourlyPrayerCompletionEntity>>

    @Query("SELECT * FROM hourly_prayer_completions WHERE dateString IN (:dates) ORDER BY timestamp ASC")
    fun getCompletionsForDates(dates: List<String>): Flow<List<HourlyPrayerCompletionEntity>>

    @Query("SELECT * FROM hourly_prayer_completions WHERE dateString BETWEEN :startDateString AND :endDateString ORDER BY timestamp ASC")
    fun getCompletionsBetweenDates(startDateString: String, endDateString: String): Flow<List<HourlyPrayerCompletionEntity>>

    @Query("SELECT * FROM hourly_prayer_completions WHERE prayerCode = :prayerCode AND dateString = :dateString LIMIT 1")
    suspend fun getCompletion(prayerCode: String, dateString: String): HourlyPrayerCompletionEntity?

    @Query("SELECT COUNT(*) FROM hourly_prayer_completions WHERE dateString = :dateString")
    fun getCompletedCountForDate(dateString: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM hourly_prayer_completions WHERE year = :year AND weekOfYear = :weekOfYear")
    fun getCompletedCountForWeek(year: Int, weekOfYear: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HourlyPrayerCompletionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletions(completions: List<HourlyPrayerCompletionEntity>)

    @Query("DELETE FROM hourly_prayer_completions WHERE prayerCode = :prayerCode AND dateString = :dateString")
    suspend fun deleteCompletion(prayerCode: String, dateString: String)

    @Query("DELETE FROM hourly_prayer_completions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM hourly_prayer_completions")
    suspend fun clearAll()
}
