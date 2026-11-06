package com.example.ui.theme

import com.example.localization.AppLanguage

/**
 * App Theme Display Mode (Dark, Light, System Default)
 */
enum class AppThemeMode(val id: String, val titleAr: String, val titleEn: String) {
    SYSTEM("system", "تلقائي (حسب النظام)", "System Default"),
    LIGHT("light", "الوضع الفاتح ☀️", "Light Mode ☀️"),
    DARK("dark", "الوضع الداكن 🌙", "Dark Mode 🌙");

    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> titleAr
        AppLanguage.COPTIC -> when (this) {
            SYSTEM -> "Ⲕⲁⲧⲁ ⲡⲓⲥⲩⲥⲧⲏⲙⲁ"
            LIGHT -> "Ⲡⲓⲟⲩⲱⲓⲛⲓ ☀️"
            DARK -> "Ⲡⲓⲕⲁⲕⲓ 🌙"
        }
        AppLanguage.FRENCH -> when (this) {
            SYSTEM -> "Système par défaut"
            LIGHT -> "Mode Clair ☀️"
            DARK -> "Mode Sombre 🌙"
        }
        AppLanguage.SPANISH -> when (this) {
            SYSTEM -> "Predeterminado del sistema"
            LIGHT -> "Modo Claro ☀️"
            DARK -> "Modo Oscuro 🌙"
        }
        AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> when (this) {
            SYSTEM -> "Systemstandard"
            LIGHT -> "Heller Modus ☀️"
            DARK -> "Dunkler Modus 🌙"
        }
        AppLanguage.ITALIAN -> when (this) {
            SYSTEM -> "Predefinito di sistema"
            LIGHT -> "Modalità Chiara ☀️"
            DARK -> "Modalità Scura 🌙"
        }
        AppLanguage.CHINESE -> when (this) {
            SYSTEM -> "跟随系统默认"
            LIGHT -> "浅色模式 ☀️"
            DARK -> "深色夜间 🌙"
        }
        AppLanguage.JAPANESE -> when (this) {
            SYSTEM -> "システム設定に従う"
            LIGHT -> "ライトモード ☀️"
            DARK -> "ダークモード 🌙"
        }
        AppLanguage.KOREAN -> when (this) {
            SYSTEM -> "시스템 기본값"
            LIGHT -> "라이트 모드 ☀️"
            DARK -> "다크 모드 🌙"
        }
        AppLanguage.HINDI -> when (this) {
            SYSTEM -> "सिस्टम डिफ़ॉल्ट"
            LIGHT -> "लाइट मोड ☀️"
            DARK -> "डार्क मोड 🌙"
        }
        else -> titleEn
    }

    companion object {
        fun fromId(id: String?): AppThemeMode {
            return entries.firstOrNull { it.id == id } ?: SYSTEM
        }
    }
}
