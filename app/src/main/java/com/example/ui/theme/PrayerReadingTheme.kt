package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.localization.AppLanguage

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
    ),

    DARK_WOOD(
        id = "dark_wood",
        titleAr = "خشب أبنوس دافئ 🪵",
        titleEn = "Dark Ebony Wood 🪵",
        backgroundColor = Color(0xFF1A110B),
        surfaceColor = Color(0xFF271A12),
        textColor = Color(0xFFF2E3D5),
        textSecondaryColor = Color(0xFFC7A583),
        accentColor = Color(0xFFE5AD68),
        rubricColor = Color(0xFFE86B63),
        rubricBgColor = Color(0xFF381B16),
        borderColor = Color(0xFF8C5A2B)
    ),

    LIGHT_CEDAR(
        id = "light_cedar",
        titleAr = "خشب أرز كنسي 🪵",
        titleEn = "Light Church Cedar 🪵",
        backgroundColor = Color(0xFFF5EBE1),
        surfaceColor = Color(0xFFEADAC9),
        textColor = Color(0xFF362215),
        textSecondaryColor = Color(0xFF755038),
        accentColor = Color(0xFFB86B27),
        rubricColor = Color(0xFF9E1B1B),
        rubricBgColor = Color(0xFFF2D8D8),
        borderColor = Color(0xFFC49261)
    );

    fun getTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC, AppLanguage.SYRIAN_ARABIC, AppLanguage.SYRIAC -> titleAr
        AppLanguage.COPTIC -> when (this) {
            DEFAULT -> "Ⲡⲓⲁⲣⲭⲏ"
            PARCHMENT -> "Ⲡⲓⲭⲁⲣⲧⲏⲥ"
            WARM_SEPIA -> "Ⲥⲏⲡⲓⲁ ⲛ̀ⲑⲙⲟ"
            NIGHT_OBSIDIAN -> "Ⲛⲓⲉⲩⲭⲏ ⲛ̀ϫⲱⲣϩ"
            DARK_WOOD -> "Ⲡⲓϣⲉ ⲛ̀ⲕⲁⲕⲓ 🪵"
            LIGHT_CEDAR -> "Ⲡⲓⲕⲉⲇⲣⲟⲥ 🪵"
        }
        AppLanguage.FRENCH -> when (this) {
            DEFAULT -> "Par défaut"
            PARCHMENT -> "Parchemin Ancien"
            WARM_SEPIA -> "Sépia Chaleureux"
            NIGHT_OBSIDIAN -> "Obsidienne Nocturne"
            DARK_WOOD -> "Bois d'Ébène Sombre 🪵"
            LIGHT_CEDAR -> "Cèdre d'Église Clair 🪵"
        }
        AppLanguage.SPANISH -> when (this) {
            DEFAULT -> "Predeterminado"
            PARCHMENT -> "Pergamino Antiguo"
            WARM_SEPIA -> "Sepia Cálido"
            NIGHT_OBSIDIAN -> "Obsidiana Nocturna"
            DARK_WOOD -> "Madera de Ébano Oscura 🪵"
            LIGHT_CEDAR -> "Cedro Eclesiástico Claro 🪵"
        }
        AppLanguage.GERMAN, AppLanguage.AUSTRIAN_GERMAN, AppLanguage.SWISS_GERMAN -> when (this) {
            DEFAULT -> "Standard"
            PARCHMENT -> "Altes Pergament"
            WARM_SEPIA -> "Warmes Sepia"
            NIGHT_OBSIDIAN -> "Nacht-Obsidian"
            DARK_WOOD -> "Dunkles Ebenholz 🪵"
            LIGHT_CEDAR -> "Helles Zedernholz 🪵"
        }
        AppLanguage.ITALIAN -> when (this) {
            DEFAULT -> "Predefinito"
            PARCHMENT -> "Pergamena Antica"
            WARM_SEPIA -> "Sepia Caldo"
            NIGHT_OBSIDIAN -> "Ossidiana Notturna"
            DARK_WOOD -> "Legno di Ebano Scuro 🪵"
            LIGHT_CEDAR -> "Cedro di Chiesa Chiaro 🪵"
        }
        AppLanguage.CHINESE -> when (this) {
            DEFAULT -> "默认主题"
            PARCHMENT -> "羊皮纸手稿"
            WARM_SEPIA -> "暖色复古棕"
            NIGHT_OBSIDIAN -> "夜间黑耀石"
            DARK_WOOD -> "深色乌木 🪵"
            LIGHT_CEDAR -> "香柏木色 🪵"
        }
        AppLanguage.JAPANESE -> when (this) {
            DEFAULT -> "デフォルト"
            PARCHMENT -> "羊皮紙写本"
            WARM_SEPIA -> "温かみのあるセピア"
            NIGHT_OBSIDIAN -> "ナイト・オブシディアン"
            DARK_WOOD -> "ダーク・エボニーウッド 🪵"
            LIGHT_CEDAR -> "ライト・シダーウッド 🪵"
        }
        AppLanguage.KOREAN -> when (this) {
            DEFAULT -> "기본 테마"
            PARCHMENT -> "고대 양피지"
            WARM_SEPIA -> "따뜻한 세피아"
            NIGHT_OBSIDIAN -> "나이트 옵시디언"
            DARK_WOOD -> "다크 흑단 목재 🪵"
            LIGHT_CEDAR -> "라이트 삼나무 🪵"
        }
        AppLanguage.HINDI -> when (this) {
            DEFAULT -> "डिफ़ॉल्ट"
            PARCHMENT -> "प्राचीन चर्मपत्र"
            WARM_SEPIA -> "वार्म सेपिया"
            NIGHT_OBSIDIAN -> "नाइट ऑब्सीडियन"
            DARK_WOOD -> "डार्क आबनूस की लकड़ी 🪵"
            LIGHT_CEDAR -> "लाइट देवदार की लकड़ी 🪵"
        }
        else -> titleEn
    }
}
