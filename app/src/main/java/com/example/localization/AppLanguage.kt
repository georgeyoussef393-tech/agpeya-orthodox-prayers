package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ARABIC("ar", "Arabic", "العربية"),
    ENGLISH("en", "English", "English"),
    COPTIC("cop", "Coptic", "Ϯⲁⲥⲡⲓ ⲛ̀ⲣⲉⲙⲛ̀ⲭⲏⲙⲓ"),
    FRENCH("fr", "French", "Français"),
    SPANISH("es", "Spanish", "Español"),
    GERMAN("de", "German", "Deutsch"),
    ITALIAN("it", "Italian", "Italiano");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ARABIC
        }
    }
}
