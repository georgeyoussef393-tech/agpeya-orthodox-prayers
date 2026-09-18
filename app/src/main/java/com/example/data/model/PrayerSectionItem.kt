package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Represents an individual section of a canonical Agpeya prayer.
 */
data class PrayerSectionItem(
    val id: String,
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
    val isDoxologyOrAmen: Boolean = false
) {
    fun getTitle(lang: AppLanguage): String = if (lang == AppLanguage.ARABIC) titleAr else titleEn
    fun getSubtitle(lang: AppLanguage): String? = if (lang == AppLanguage.ARABIC) subtitleAr else subtitleEn
    fun getContent(lang: AppLanguage): String = if (lang == AppLanguage.ARABIC) contentAr else contentEn
    fun getRubric(lang: AppLanguage): String? = if (lang == AppLanguage.ARABIC) rubricAr else rubricEn
}
