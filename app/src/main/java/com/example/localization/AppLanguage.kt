package com.example.localization

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flagEmoji: String = "",
    val isRtl: Boolean = false
) {
    ARABIC("ar", "العربية", "العربية (المصرية)", "🇪🇬", isRtl = true),
    ENGLISH("en", "الإنجليزية", "English (الإنجليزية)", "🇬🇧", isRtl = false),
    COPTIC("cop", "القبطية", "Ϯⲁⲥⲡⲓ ⲛ̀ⲣⲉⲙⲛ̀ⲭⲏⲙⲓ (القبطية)", "☦️", isRtl = false),
    GERMAN("de", "الألمانية", "Deutsch (الألمانية)", "🇩🇪", isRtl = false),
    AUSTRIAN_GERMAN("de-AT", "النمساوية", "Deutsch - Österreich (النمساوية)", "🇦🇹", isRtl = false),
    SPANISH("es", "الإسبانية", "Español (الإسبانية)", "🇪🇸", isRtl = false),
    HINDI("hi", "الهندية", "हिन्दी (الهندية)", "🇮🇳", isRtl = false),
    CHINESE("zh", "الصينية", "中文 (الصينية)", "🇨🇳", isRtl = false),
    ITALIAN("it", "الإيطالية", "Italiano (الإيطالية)", "🇮🇹", isRtl = false),
    FRENCH("fr", "الفرنسية", "Français (الفرنسية)", "🇫🇷", isRtl = false),
    SYRIAN_ARABIC("ar-SY", "العربية (سوريا)", "العربية (سوريا وبلاد الشام)", "🇸🇾", isRtl = true),
    SYRIAC("syc", "السريانية الآرامية", "ܠܫܢܐ ܣܘܪܝܝܐ (السريانية)", "✝️", isRtl = true),
    SWISS_GERMAN("de-CH", "السويسرية", "Schwiizerdütsch (السويسرية)", "🇨🇭", isRtl = false),
    JAPANESE("ja", "اليابانية", "日本語 (اليابانية)", "🇯🇵", isRtl = false),
    KOREAN("ko", "الكورية", "한국어 (الكورية)", "🇰🇷", isRtl = false);

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ARABIC
        }
    }
}

