package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CachedPrayerSectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTextDao {

    @Query("SELECT * FROM cached_prayer_sections WHERE prayerCode = :prayerCode AND languageCode = :languageCode ORDER BY sectionOrder ASC")
    fun getSectionsForPrayer(prayerCode: String, languageCode: String): Flow<List<CachedPrayerSectionEntity>>

    @Query("SELECT * FROM cached_prayer_sections WHERE prayerCode = :prayerCode AND languageCode = :languageCode ORDER BY sectionOrder ASC")
    suspend fun getSectionsForPrayerDirect(prayerCode: String, languageCode: String): List<CachedPrayerSectionEntity>

    @Query("SELECT COUNT(*) FROM cached_prayer_sections WHERE prayerCode = :prayerCode AND languageCode = :languageCode")
    suspend fun getSectionCount(prayerCode: String, languageCode: String): Int

    @Query("SELECT COUNT(*) FROM cached_prayer_sections")
    fun getTotalCachedSectionsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM cached_prayer_sections")
    suspend fun getTotalCachedSectionsCountDirect(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSections(sections: List<CachedPrayerSectionEntity>)

    @Query("DELETE FROM cached_prayer_sections WHERE prayerCode = :prayerCode AND languageCode = :languageCode")
    suspend fun clearSectionsForPrayer(prayerCode: String, languageCode: String)

    @Query("DELETE FROM cached_prayer_sections")
    suspend fun clearAllPrayerSections()
}
