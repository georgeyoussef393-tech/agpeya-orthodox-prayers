package com.example.localization

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flagEmoji: String = "",
    val isRtl: Boolean = false
) {
    ARABIC("ar", "Arabic", "العربية", "🇪🇬", isRtl = true),
    SYRIAN_ARABIC("ar-SY", "Syrian Arabic", "العربية (سوريا وبلاد الشام)", "🇸🇾", isRtl = true),
    SYRIAC("syc", "Syriac (Aramaic)", "ܠܫܢܐ ܣܘܪܝܝܐ (السريانية)", "✝️", isRtl = true),
    GERMAN("de", "German (Germany)", "Deutsch (Deutschland)", "🇩🇪", isRtl = false),
    AUSTRIAN_GERMAN("de-AT", "Austrian German", "Deutsch (Österreich)", "🇦🇹", isRtl = false),
    SWISS_GERMAN("de-CH", "Swiss German", "Schwiizerdütsch (Schweiz)", "🇨🇭", isRtl = false),
    ENGLISH("en", "English", "English", "🇬🇧", isRtl = false),
    COPTIC("cop", "Coptic", "Ϯⲁⲥⲡⲓ ⲛ̀ⲣⲉⲙⲛ̀ⲭⲏⲙⲓ", "☦️", isRtl = false),
    FRENCH("fr", "French", "Français", "🇫🇷", isRtl = false),
    SPANISH("es", "Spanish", "Español", "🇪🇸", isRtl = false),
    ITALIAN("it", "Italian", "Italiano", "🇮🇹", isRtl = false),
    CHINESE("zh", "Chinese", "中文 (简体)", "🇨🇳", isRtl = false),
    JAPANESE("ja", "Japanese", "日本語", "🇯🇵", isRtl = false),
    KOREAN("ko", "Korean", "한국어", "🇰🇷", isRtl = false);

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ARABIC
        }
    }
}

