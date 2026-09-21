package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

/**
 * Spiritual background themes for enhanced reverence, prayer atmosphere, and contemplation.
 * أنماط الخلفيات الروحية لزيادة الخشوع والتركيز في الصلاة والتأمل.
 */
enum class SpiritualBackgroundTheme(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    @DrawableRes val drawableResId: Int?,
    val defaultOpacity: Float = 0.22f
) {
    CANDLE_SANCTUARY(
        id = "candle_sanctuary",
        titleAr = "هيكل الشموع والبخور",
        titleEn = "Candles & Sanctuary",
        subtitleAr = "أجواء خشوع مع شموع مقدسة وأقواس كنسية هادئة",
        subtitleEn = "Deep reverent atmosphere with soft candlelight and church arches",
        drawableResId = R.drawable.img_spiritual_candle_bg,
        defaultOpacity = 0.22f
    ),
    MONASTIC_CELL(
        id = "monastic_cell",
        titleAr = "قلاية رهبانية وكتاب مقدس",
        titleEn = "Monastic Cell & Scripture",
        subtitleAr = "مائدة صلاة مع كتاب مقدس وشمعة للتأمل والهدوء",
        subtitleEn = "Ancient monastic table with open scripture and warm candlelight",
        drawableResId = R.drawable.img_monastic_prayer_bg,
        defaultOpacity = 0.20f
    ),
    GOLDEN_SANCTUARY(
        id = "golden_sanctuary",
        titleAr = "حجاب الهيكل الذهبي",
        titleEn = "Golden Iconostasis",
        subtitleAr = "هيكل كنسي بنور سماوي وأيقونات قبطية مذهبة",
        subtitleEn = "Coptic sanctuary with warm heavenly light and golden iconostasis",
        drawableResId = R.drawable.img_sanctuary_banner,
        defaultOpacity = 0.18f
    ),
    CLASSIC_AGPEYA(
        id = "classic_agpeya",
        titleAr = "أيقونة الأجبية الكلاسيكية",
        titleEn = "Classic Agpeya Icon",
        subtitleAr = "أيقونة الأجبية والمزامير القبطية التراثية",
        subtitleEn = "Classic traditional Agpeya icon art",
        drawableResId = R.drawable.agpeya_banner,
        defaultOpacity = 0.16f
    ),
    PURE_MINIMAL(
        id = "pure_minimal",
        titleAr = "لون هادئ بدون صور",
        titleEn = "Pure Minimal Dark",
        subtitleAr = "خلفية داكنة صافية ومركزة للقرأة فقط",
        subtitleEn = "Pure minimal dark background for distraction-free reading",
        drawableResId = null,
        defaultOpacity = 0.0f
    );

    companion object {
        fun fromId(id: String?): SpiritualBackgroundTheme {
            return entries.find { it.id == id } ?: CANDLE_SANCTUARY
        }
    }
}
