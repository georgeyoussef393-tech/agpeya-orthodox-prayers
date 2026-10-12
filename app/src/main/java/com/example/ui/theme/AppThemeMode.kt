package com.example.ui.theme

/**
 * App Theme Display Mode (Dark, Light, System Default)
 */
enum class AppThemeMode(val id: String, val titleAr: String, val titleEn: String) {
    SYSTEM("system", "تلقائي (حسب النظام)", "System Default"),
    LIGHT("light", "الوضع الفاتح ☀️", "Light Mode ☀️"),
    DARK("dark", "الوضع الداكن 🌙", "Dark Mode 🌙");

    companion object {
        fun fromId(id: String?): AppThemeMode {
            return entries.firstOrNull { it.id == id } ?: SYSTEM
        }
    }
}
