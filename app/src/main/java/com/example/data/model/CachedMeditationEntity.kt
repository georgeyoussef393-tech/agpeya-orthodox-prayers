package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.localization.AppLanguage

/**
 * Room Database entity for caching daily scripture verses, patristic reflections,
 * and fetched spiritual meditations offline.
 */
@Entity(tableName = "cached_meditations")
data class CachedMeditationEntity(
    @PrimaryKey
    val id: String, // e.g. "day_1_PRIME" or custom ID
    val dayOfYear: Int,
    val associatedPrayerCode: String,
    val referenceArabic: String,
    val referenceCoptic: String,
    val referenceEnglish: String,
    val verseArabic: String,
    val verseCoptic: String,
    val verseEnglish: String,
    val explanationArabic: String,
    val explanationEnglish: String,
    val liturgicalThemeArabic: String,
    val liturgicalThemeEnglish: String,
    val isCustomOrFetched: Boolean = false,
    val source: String = "CANONICAL", // "CANONICAL", "PATRISTIC_ARCHIVE", "AI_INSIGHT", "COMMUNITY"
    val cachedTimestamp: Long = System.currentTimeMillis()
) {
    fun toDailyVerseMeditation(): DailyVerseMeditation {
        return DailyVerseMeditation(
            dayOfYear = dayOfYear,
            referenceArabic = referenceArabic,
            referenceCoptic = referenceCoptic,
            referenceEnglish = referenceEnglish,
            verseArabic = verseArabic,
            verseCoptic = verseCoptic,
            verseEnglish = verseEnglish,
            explanationArabic = explanationArabic,
            explanationEnglish = explanationEnglish,
            associatedPrayer = PrayerId.fromCode(associatedPrayerCode),
            liturgicalThemeArabic = liturgicalThemeArabic,
            liturgicalThemeEnglish = liturgicalThemeEnglish
        )
    }

    companion object {
        fun fromDailyVerseMeditation(
            meditation: DailyVerseMeditation,
            id: String = "day_${meditation.dayOfYear}_${meditation.associatedPrayer.code}",
            isCustomOrFetched: Boolean = false,
            source: String = "CANONICAL"
        ): CachedMeditationEntity {
            return CachedMeditationEntity(
                id = id,
                dayOfYear = meditation.dayOfYear,
                associatedPrayerCode = meditation.associatedPrayer.code,
                referenceArabic = meditation.referenceArabic,
                referenceCoptic = meditation.referenceCoptic,
                referenceEnglish = meditation.referenceEnglish,
                verseArabic = meditation.verseArabic,
                verseCoptic = meditation.verseCoptic,
                verseEnglish = meditation.verseEnglish,
                explanationArabic = meditation.explanationArabic,
                explanationEnglish = meditation.explanationEnglish,
                liturgicalThemeArabic = meditation.liturgicalThemeArabic,
                liturgicalThemeEnglish = meditation.liturgicalThemeEnglish,
                isCustomOrFetched = isCustomOrFetched,
                source = source
            )
        }
    }
}
