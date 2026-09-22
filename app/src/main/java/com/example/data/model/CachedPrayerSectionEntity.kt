package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.localization.AppLanguage

/**
 * Room Database entity for caching canonical Agpeya prayer sections offline.
 * Guarantees that all prayers, psalms, gospels, and litanies are instantly accessible
 * without an internet connection.
 */
@Entity(tableName = "cached_prayer_sections")
data class CachedPrayerSectionEntity(
    @PrimaryKey
    val id: String, // format: "${prayerCode}_${languageCode}_${sectionOrder}"
    val prayerCode: String,
    val languageCode: String,
    val sectionOrder: Int,
    val sectionId: String,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String? = null,
    val subtitleEn: String? = null,
    val contentAr: String,
    val contentEn: String,
    val copticIntro: String? = null,
    val rubricAr: String? = null,
    val rubricEn: String? = null,
    val isGospel: Boolean = false,
    val isDoxologyOrAmen: Boolean = false,
    val cachedTimestamp: Long = System.currentTimeMillis()
) {
    fun toPrayerSectionItem(): PrayerSectionItem {
        return PrayerSectionItem(
            id = sectionId,
            titleAr = titleAr,
            titleEn = titleEn,
            subtitleAr = subtitleAr,
            subtitleEn = subtitleEn,
            contentAr = contentAr,
            contentEn = contentEn,
            copticIntro = copticIntro,
            rubricAr = rubricAr,
            rubricEn = rubricEn,
            isGospel = isGospel,
            isDoxologyOrAmen = isDoxologyOrAmen
        )
    }

    companion object {
        fun fromPrayerSectionItem(
            item: PrayerSectionItem,
            prayerCode: String,
            languageCode: String,
            order: Int
        ): CachedPrayerSectionEntity {
            return CachedPrayerSectionEntity(
                id = "${prayerCode}_${languageCode}_$order",
                prayerCode = prayerCode,
                languageCode = languageCode,
                sectionOrder = order,
                sectionId = item.id,
                titleAr = item.titleAr,
                titleEn = item.titleEn,
                subtitleAr = item.subtitleAr,
                subtitleEn = item.subtitleEn,
                contentAr = item.contentAr,
                contentEn = item.contentEn,
                copticIntro = item.copticIntro,
                rubricAr = item.rubricAr,
                rubricEn = item.rubricEn,
                isGospel = item.isGospel,
                isDoxologyOrAmen = item.isDoxologyOrAmen
            )
        }
    }
}
