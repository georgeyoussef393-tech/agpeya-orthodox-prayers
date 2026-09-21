package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Eye-comfort Reading Themes tailored for prayer, spiritual contemplation, and night reading.
 */
enum class PrayerReadingTheme(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val backgroundColor: Color,
    val surfaceColor: Color,
    val textColor: Color,
    val textSecondaryColor: Color,
    val accentColor: Color,
    val rubricColor: Color,
    val rubricBgColor: Color,
    val borderColor: Color
) {
    DEFAULT(
        id = "default",
        titleAr = "الافتراضي",
        titleEn = "Default",
        backgroundColor = Color.Unspecified,
        surfaceColor = Color.Unspecified,
        textColor = Color.Unspecified,
        textSecondaryColor = Color.Unspecified,
        accentColor = GoldPrimary,
        rubricColor = BurgundyPrimary,
        rubricBgColor = BurgundyDeep.copy(alpha = 0.08f),
        borderColor = GoldPrimary.copy(alpha = 0.35f)
    ),

    PARCHMENT(
        id = "parchment",
        titleAr = "مخطوطة بردي",
        titleEn = "Parchment",
        backgroundColor = Color(0xFFF7F1E1),
        surfaceColor = Color(0xFFEFE6D0),
        textColor = Color(0xFF2C1810),
        textSecondaryColor = Color(0xFF6B4423),
        accentColor = Color(0xFFB06F23),
        rubricColor = Color(0xFF8B0000),
        rubricBgColor = Color(0xFFE5D5BC),
        borderColor = Color(0xFFD4AF37)
    ),

    WARM_SEPIA(
        id = "sepia",
        titleAr = "سيبيا دافئ",
        titleEn = "Warm Sepia",
        backgroundColor = Color(0xFFFBF0D9),
        surfaceColor = Color(0xFFF3E4C0),
        textColor = Color(0xFF3B2314),
        textSecondaryColor = Color(0xFF7A4E2D),
        accentColor = Color(0xFFA65B1A),
        rubricColor = Color(0xFF9E2A2B),
        rubricBgColor = Color(0xFFE9D5AF),
        borderColor = Color(0xFFC99757)
    ),

    NIGHT_OBSIDIAN(
        id = "night",
        titleAr = "تهجد ليلي",
        titleEn = "Night Obsidian",
        backgroundColor = Color(0xFF121214),
        surfaceColor = Color(0xFF1E1E24),
        textColor = Color(0xFFE8DFD8),
        textSecondaryColor = Color(0xFFB0A8A0),
        accentColor = Color(0xFFE5B869),
        rubricColor = Color(0xFFFF8A80),
        rubricBgColor = Color(0xFF2E1C1F),
        borderColor = Color(0xFFE5B869).copy(alpha = 0.4f)
    )
}
