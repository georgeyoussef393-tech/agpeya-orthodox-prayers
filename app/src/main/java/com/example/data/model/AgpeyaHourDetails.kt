package com.example.data.model

import com.example.localization.AppLanguage

/**
 * Detailed Psalms and Specific Prayers for the Agpeya hours.
 */
object AgpeyaHourDetails {

    /**
     * Prime (صلاة باكر):
     * Complete purified liturgical order with all 19 Psalms, Gospel, Litanies, and Absolutions.
     */
    fun getPrimeSections(lang: AppLanguage): List<PrayerSectionItem> {
        return PrimePrayerData.getSections(lang)
    }

    /**
     * Terce (صلاة الساعة الثالثة - ٩ صباحاً):
     * Descent of Holy Spirit, complete unabridged Psalms (19, 20, 21, 25, 28, 29), Gospel, Litanies, Absolution.
     */
    fun getTerceSections(lang: AppLanguage): List<PrayerSectionItem> {
        return TercePrayerData.getSections(lang)
    }

    /**
     * Sext (صلاة الساعة السادسة - ١٢ ظهراً):
     * The Crucifixion at Golgotha, unabridged Psalms (53, 54, 55, 66, 69, 85, 90, 92), Gospel of Beatitudes, Litanies, Absolution.
     */
    fun getSextSections(lang: AppLanguage): List<PrayerSectionItem> {
        return SextPrayerData.getSections(lang)
    }

    /**
     * None (صلاة الساعة التاسعة - ٣ ظهراً):
     * Death of Christ in the Flesh, Repentance of the Right Hand Thief, unabridged 12 Psalms (95, 96, 97, 98, 99, 100, 109, 110, 111, 112, 114, 115), Gospel, Litanies, Absolution.
     */
    fun getNoneSections(lang: AppLanguage): List<PrayerSectionItem> {
        return NonePrayerData.getSections(lang)
    }

    /**
     * Vespers (صلاة الغروب - الساعة الحادية عشرة - ٥ مساءً):
     * The Sunset hour, taking down Christ from the Cross, unabridged 12 Psalms (116, 117, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128), Gospel, Litanies, Absolution.
     */
    fun getVespersSections(lang: AppLanguage): List<PrayerSectionItem> {
        return VespersPrayerData.getSections(lang)
    }

    /**
     * Compline (صلاة النوم - الساعة الثانية عشرة - ٩ مساءً):
     * Burial of Christ, Readiness for Eternity, unabridged 12 Psalms (129, 130, 131, 132, 133, 136, 137, 140, 141, 145, 146, 147), Gospel, Litanies, Absolution.
     */
    fun getComplineSections(lang: AppLanguage): List<PrayerSectionItem> {
        return ComplinePrayerData.getSections(lang)
    }

    /**
     * Midnight (صلاة نصف الليل):
     * The Three Services (الخدمة الأولى، الثانية، الثالثة).
     */
    fun getMidnightSections(lang: AppLanguage): List<PrayerSectionItem> {
        return MidnightPrayerData.getSections(lang)
    }

    /**
     * Veil (صلاة الستار - الخاصة بالرهبان):
     * Complete unabridged 29 Psalms (4, 6, 12, 15, 24, 26, 66, 69, 22, 29, 42, 56, 85, 90, 96, 109, 114, 115, 120, 128, 129, 130, 131, 132, 133, 136, 140, 145, 118),
     * Gospel of St. John, Litanies of contrition and tears, Absolution.
     */
    fun getVeilSections(lang: AppLanguage): List<PrayerSectionItem> {
        return VeilPrayerData.getSections(lang)
    }

    /**
     * Get the complete, detailed list of liturgical sections for any canonical hour.
     */
    fun getFullPrayerSections(prayerId: PrayerId, lang: AppLanguage): List<PrayerSectionItem> = when (prayerId) {
        PrayerId.PRIME -> getPrimeSections(lang)
        PrayerId.TERCE -> getTerceSections(lang)
        PrayerId.SEXT -> getSextSections(lang)
        PrayerId.NONE -> getNoneSections(lang)
        PrayerId.VESPERS -> getVespersSections(lang)
        PrayerId.COMPLINE -> getComplineSections(lang)
        PrayerId.MIDNIGHT -> getMidnightSections(lang)
        PrayerId.VEIL -> getVeilSections(lang)
    }
}
