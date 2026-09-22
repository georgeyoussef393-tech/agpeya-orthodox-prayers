package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CachedMeditationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeditationDao {

    @Query("SELECT * FROM cached_meditations ORDER BY dayOfYear ASC, cachedTimestamp DESC")
    fun getAllMeditations(): Flow<List<CachedMeditationEntity>>

    @Query("SELECT * FROM cached_meditations ORDER BY cachedTimestamp DESC")
    suspend fun getAllMeditationsList(): List<CachedMeditationEntity>

    @Query("SELECT * FROM cached_meditations WHERE dayOfYear = :dayOfYear LIMIT 1")
    fun getMeditationForDay(dayOfYear: Int): Flow<CachedMeditationEntity?>

    @Query("SELECT * FROM cached_meditations WHERE dayOfYear = :dayOfYear LIMIT 1")
    suspend fun getMeditationForDayDirect(dayOfYear: Int): CachedMeditationEntity?

    @Query("SELECT * FROM cached_meditations WHERE associatedPrayerCode = :prayerCode ORDER BY cachedTimestamp DESC LIMIT 1")
    fun getMeditationForPrayer(prayerCode: String): Flow<CachedMeditationEntity?>

    @Query("SELECT * FROM cached_meditations WHERE associatedPrayerCode = :prayerCode ORDER BY cachedTimestamp DESC LIMIT 1")
    suspend fun getMeditationForPrayerDirect(prayerCode: String): CachedMeditationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeditation(meditation: CachedMeditationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeditations(meditations: List<CachedMeditationEntity>)

    @Query("SELECT COUNT(*) FROM cached_meditations")
    fun getTotalMeditationsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM cached_meditations")
    suspend fun getTotalMeditationsCountDirect(): Int

    @Query("DELETE FROM cached_meditations WHERE id = :id")
    suspend fun deleteMeditationById(id: String)

    @Query("DELETE FROM cached_meditations")
    suspend fun clearAllMeditations()
}
