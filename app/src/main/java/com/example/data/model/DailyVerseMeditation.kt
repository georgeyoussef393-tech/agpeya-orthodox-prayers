package com.example.data.model

import com.example.audio.SpiritualSound
import com.example.localization.AppLanguage
import java.util.Calendar

/**
 * Daily Orthodox Scripture Verse & Spiritual Meditation model.
 * Based on the authentic Orthodox Coptic canonical scriptures and patristic tradition in Egypt.
 */
data class DailyVerseMeditation(
    val dayOfYear: Int,
    val referenceArabic: String,
    val referenceCoptic: String,
    val referenceEnglish: String,
    val verseArabic: String,
    val verseCoptic: String,
    val verseEnglish: String,
    val explanationArabic: String,
    val explanationEnglish: String,
    val associatedPrayer: PrayerId,
    val liturgicalThemeArabic: String,
    val liturgicalThemeEnglish: String
) {
    fun getReference(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> referenceArabic
        AppLanguage.COPTIC -> referenceCoptic
        else -> referenceEnglish
    }

    fun getVerse(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> verseArabic
        AppLanguage.COPTIC -> verseCoptic
        else -> verseEnglish
    }

    fun getExplanation(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC, AppLanguage.COPTIC -> explanationArabic
        else -> explanationEnglish
    }

    fun getLiturgicalTheme(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC, AppLanguage.COPTIC -> liturgicalThemeArabic
        else -> liturgicalThemeEnglish
    }
}

/**
 * Repository of authentic Orthodox Coptic daily scriptures and patristic reflections
 * matched precisely to the canonical prayer hours of the Agpeya.
 */
object DailyScriptureProvider {

    private val verses: List<DailyVerseMeditation> = listOf(
        DailyVerseMeditation(
            dayOfYear = 1,
            referenceArabic = "مزمور ٦٣: ١",
            referenceCoptic = "Ⲯⲁⲗⲙⲟⲥ ⲝ̅ⲅ̅: ⲁ̅",
            referenceEnglish = "Psalm 63:1",
            verseArabic = "«يَا اللهُ، إِلَهِي أَنْتَ. إِلَيْكَ أُبَكِّرُ. عَطِشَتْ إِلَيْكَ نَفْسِي، يَشْتَاقُ إِلَيْكَ جَسَدِي فِي أَرْضٍ نَاشِفَةٍ وَيَابِسَةٍ بِلاَ مَاءٍ».",
            verseCoptic = "«Ⲫⲛⲟⲩϯ ⲡⲁⲛⲟⲩϯ ϯⲛⲁϣⲟⲣⲡ ⲉ̀ⲣⲟⲓ ϩⲁⲣⲟⲕ: ⲁⲥⲓⲃⲓ ⲉ̀ⲣⲟⲕ ⲛ̀ϫⲉ ⲧⲁ\'\'ⲩⲭⲏ».",
            verseEnglish = "\"O God, You are my God; early will I seek You; my soul thirsts for You; my flesh longs for You in a dry and thirsty land where there is no water.\"",
            explanationArabic = "تأمل باكر (الأنبا أنطونيوس): كما يستيقظ العالم لطلب أرزاقه، يستيقظ المؤمن الأرثوذكسي مبكرًا ليرتوي من النور الإلهي قبل صخب النهار. بكور اليوم تقدمة مقدسة تبارك سائر ساعاتك وسعيك.",
            explanationEnglish = "Morning Reflection (St. Anthony the Great): Just as the world awakens for earthly pursuits, the faithful soul rises early to drink from the divine fountain. Offering the first fruits of your day sanctifies all its hours.",
            associatedPrayer = PrayerId.PRIME,
            liturgicalThemeArabic = "صلاة باكر • إشراق نور المسيح في القلب",
            liturgicalThemeEnglish = "Prime Prayer • Dawning of Christ's Light"
        ),
        DailyVerseMeditation(
            dayOfYear = 2,
            referenceArabic = "مزمور ٥١: ١٠-١١",
            referenceCoptic = "Ⲯⲁⲗⲙⲟⲥ ⲛ̅: ⲓ̅-ⲓ̅ⲁ̅",
            referenceEnglish = "Psalm 51:10-11",
            verseArabic = "«قَلْبًا نَقِيًّا اخْلُقْ فِيَّ يَا اللهُ، وَرُوحًا مُسْتَقِيمًا جَدِّدْ فِي دَاخِلِي. لاَ تَطْرَحْنِي مِنْ قُدَّامِ وَجْهِكَ، وَرُوحَكَ الْقُدُّوسَ لاَ تَنْزِعْهُ مِنِّي».",
            verseCoptic = "«Ⲟⲩϩⲏⲧ ⲉϥⲟⲩⲁⲃ ⲙⲁⲥⲟⲛϥ ⲛ̀ϧⲏⲧ Ⲫⲛⲟⲩϯ: ⲟⲩⲡⲛⲉⲩⲙⲁ ⲉϥⲥⲟⲩⲧⲱⲛ ⲁ̀ⲣⲓⲧϥ ⲙ̀ⲃⲉⲣⲓ ϧⲉⲛ ⲛⲁⲥⲡⲗⲁⲅⲭⲛⲟⲛ».",
            verseEnglish = "\"Create in me a clean heart, O God, and renew a steadfast spirit within me. Do not cast me away from Your presence, and do not take Your Holy Spirit from me.\"",
            explanationArabic = "تأمل الساعة الثالثة (القديس كيرلس الكبير): نطلب حلول الروح القدس كما حلّ على الرسل في الساعة الثالثة، ليطهر ضمائرنا من شوائب الخطية ويزرع فينا ثمار المحبة والسلام وطاعة الوصية.",
            explanationEnglish = "Terce Reflection (St. Cyril of Alexandria): At the third hour, we beseech the descent of the Holy Comforter upon our hearts, renewing a steadfast and pure spirit ready to bear divine fruit.",
            associatedPrayer = PrayerId.TERCE,
            liturgicalThemeArabic = "الساعة الثالثة • موهبة الروح القدس المعزي",
            liturgicalThemeEnglish = "Terce Prayer • Gift of the Holy Spirit"
        ),
        DailyVerseMeditation(
            dayOfYear = 3,
            referenceArabic = "غلاطية ٦: ١٤",
            referenceCoptic = "Ⲅⲁⲗⲁⲧⲏⲥ ⲋ̅: ⲓ̅ⲇ̅",
            referenceEnglish = "Galatians 6:14",
            verseArabic = "«وَأَمَّا مِنْ جِهَتِي، فَحَاشَا لِي أَنْ أَفْتَخِرَ إِلاَّ بِصَلِيبِ رَبِّنَا يَسُوعَ الْمَسِيحِ، الَّذِي بِهِ قَدْ صُلِبَ الْعَالَمُ لِي وَأَنَا لِلْعَالَمِ».",
            verseCoptic = "«Ⲁⲛⲟⲕ ⲇⲉ ⲛ̀ⲛⲉⲥϣⲱⲡⲓ ⲛ̀ϯϣⲟⲩϣⲟⲩ ⲙ̀ⲙⲟⲓ: ⲉ̀ⲃⲏⲗ ϧⲉⲛ ⲡⲓⲥⲧⲁⲩⲣⲟⲥ ⲛ̀ⲧⲉ Ⲡⲉⲛϭⲟⲓⲥ Ⲓⲏⲥⲟⲩⲥ Ⲡⲭ̅ⲥ̅».",
            verseEnglish = "\"But God forbid that I should boast except in the cross of our Lord Jesus Christ, by whom the world has been crucified to me, and I to the world.\"",
            explanationArabic = "تأمل الساعة السادسة (القديس أثناسيوس الرسولي): في منتصف النهار عُلّق المسيح على الصليب ليبيد سلطان الظلمة. الصليب ليس عارًا بل قوة الله وفخرنا الأبدي، ومنه نستمد النصرة على تجارب الظهيرة.",
            explanationEnglish = "Sext Reflection (St. Athanasius the Apostolic): At midday, Christ was lifted upon the Cross to trample darkness. The Cross is the fortress of our faith and source of enduring strength.",
            associatedPrayer = PrayerId.SEXT,
            liturgicalThemeArabic = "الساعة السادسة • الفداء وقوة الصليب المقدس",
            liturgicalThemeEnglish = "Sext Prayer • Holy Cross & Redemption"
        ),
        DailyVerseMeditation(
            dayOfYear = 4,
            referenceArabic = "لوقا ٢٣: ٤٢-٤٣",
            referenceCoptic = "Ⲗⲟⲩⲕⲁⲥ ⲕ̅ⲅ̅: ⲙ̅ⲃ̅-ⲙ̅ⲅ̅",
            referenceEnglish = "Luke 23:42-43",
            verseArabic = "«ثُمَّ قَالَ لِيَسُوعَ: اذْكُرْنِي يَا رَبُّ مَتَى جِئْتَ فِي مَلَكُوتِكَ. فَقَالَ لَهُ يَسُوعُ: الْحَقَّ أَقُولُ لَكَ: إِنَّكَ الْيَوْمَ تَكُونُ مَعِي فِي الْفِرْدَوْسِ».",
            verseCoptic = "«Ⲟⲩⲟϩ ⲛⲁϥϫⲱ ⲙ̀ⲙⲟⲥ ϫⲉ Ⲁⲣⲓⲡⲁⲙⲉⲩⲓ ⲱ Ⲡϭⲟⲓⲥ ⲁⲕϣⲁⲛⲓ̀ ϧⲉⲛ ⲧⲉⲕⲙⲉⲧⲟⲩⲣⲟ».",
            verseEnglish = "\"Then he said to Jesus, 'Lord, remember me when You come into Your kingdom.' And Jesus said to him, 'Assuredly, I say to you, today you will be with Me in Paradise.'\"",
            explanationArabic = "تأمل الساعة التاسعة (القديس يوحنا ذهبي الفم): في الساعة التاسعة سلّم الرب الروح وفُتح الفردوس للص اليمين. صرخة توبة صادقة في لحظة خشوع كافية لتمحو سنين التيه وتنال الوعد بالفردوس.",
            explanationEnglish = "None Reflection (St. John Chrysostom): At the ninth hour our Saviour tasted death in the flesh. In a single sigh of true repentance, the thief stole Paradise; never despair of divine mercy.",
            associatedPrayer = PrayerId.NONE,
            liturgicalThemeArabic = "الساعة التاسعة • قبول التوبة وفتح الفردوس",
            liturgicalThemeEnglish = "None Prayer • Penitence & Paradise"
        ),
        DailyVerseMeditation(
            dayOfYear = 5,
            referenceArabic = "مزمور ١٤١: ٢",
            referenceCoptic = "Ⲯⲁⲗⲙⲟⲥ ⲣ̅ⲙ̅: ⲃ̅",
            referenceEnglish = "Psalm 141:2",
            verseArabic = "«لِتَسْتَقِمْ صَلاَتِي كَالْبَخُورِ قُدَّامَكَ. لِيَكُنْ رَفْعُ يَدَيَّ كَذَبِيحَةٍ مَسَائِيَّةٍ».",
            verseCoptic = "«Ⲙⲁⲣⲉ ⲧⲁⲡⲣⲟⲥⲉⲩⲭⲏ ⲥⲱⲟⲩⲧⲉⲛ ⲙ̀ⲫⲣⲏϯ ⲛ̀ⲟⲩⲥⲑⲟⲩⲛⲟⲩϥⲓ ⲙ̀ⲡⲉⲕⲙ̀ⲑⲟ: ⲡⲓϭⲓⲥⲓ ⲛ̀ⲧⲉ ⲛⲁϫⲓϫ ⲟⲩϣⲟⲩϣⲱⲟⲩϣⲓ ⲛ̀ⲣⲟⲩϩⲓ».",
            verseEnglish = "\"Let my prayer be set before You as incense, the lifting up of my hands as the evening sacrifice.\"",
            explanationArabic = "تأمل صلاة الغروب (البابا شنودة الثالث): مع مغيب الشمس، نقف شاكرين الله الذي حفظنا طوال النهار. صلاة الغروب هي بخور شكر ومحاسبة للنفس قبل أن يرخي الليل سدوله.",
            explanationEnglish = "Vespers Reflection (Pope Shenouda III): As daylight wanes, we stand giving thanks for God's protection. Evening prayer is sweet incense of gratitude and humble soul-searching.",
            associatedPrayer = PrayerId.VESPERS,
            liturgicalThemeArabic = "صلاة الغروب • ذبيحة الشكر المسائية",
            liturgicalThemeEnglish = "Vespers Prayer • Evening Sacrifice of Praise"
        ),
        DailyVerseMeditation(
            dayOfYear = 6,
            referenceArabic = "مزمور ٤: ٨",
            referenceCoptic = "Ⲯⲁⲗⲙⲟⲥ ⲇ̅: ⲏ̅",
            referenceEnglish = "Psalm 4:8",
            verseArabic = "«بِسَلاَمٍ أَضْطَجِعُ أَيْضًا وَأَنَامُ، لأَنَّكَ أَنْتَ يَا رَبُّ مُنْفَرِدًا فِي طُمَأْنِينَةٍ تُسَكِّنُنِي».",
            verseCoptic = "«Ϧⲉⲛ ⲟⲩϩⲓⲣⲏⲛⲏ ⲉⲩⲥⲟⲡ ϯⲛⲁⲉⲛⲕⲟⲧ ⲟⲩⲟϩ ⲛ̀ⲧⲁϩⲱⲣⲡ: ϫⲉ ⲛ̀ⲑⲟⲕ ⲙ̀ⲙⲁⲩⲁⲧⲕ Ⲡϭⲟⲓⲥ ⲁⲕⲑⲣⲟⲓϣⲱⲡⲓ ϧⲉⲛ ⲟⲩϩⲉⲗⲡⲓⲥ».",
            verseEnglish = "\"I will both lie down in peace, and sleep; for You alone, O Lord, make me dwell in safety.\"",
            explanationArabic = "تأمل صلاة النوم (الأنبا باخوميوس): النوم صورة للموت، والفراش تذكار للقبر. نودع أنفسنا بين يدي مخلصنا سائلين حراسة الملائكة القديسين، لنستيقظ بنشاط لتسبيح اسمه القدوس.",
            explanationEnglish = "Compline Reflection (St. Pachomius): Nightly rest reminds us of our departure; placing our spirit peacefully in the hands of the Savior under the guard of holy angels.",
            associatedPrayer = PrayerId.COMPLINE,
            liturgicalThemeArabic = "صلاة النوم • السلام الإلهي وحراسة الملائكة",
            liturgicalThemeEnglish = "Compline Prayer • Peaceful Rest & Divine Peace"
        ),
        DailyVerseMeditation(
            dayOfYear = 7,
            referenceArabic = "متى ٢٥: ٦",
            referenceCoptic = "Ⲙⲁⲧⲑⲉⲟⲥ ⲕ̅ⲉ̅: ⲋ̅",
            referenceEnglish = "Matthew 25:6",
            verseArabic = "«فَفِي نِصْفِ اللَّيْلِ صَارَ صُرَاخٌ: هُوَذَا الْعَرِيسُ مُقْبِلٌ، فَاخْرُجْنَ لِلِقَائِهِ!».",
            verseCoptic = "«Ϧⲉⲛ ⲧⲫⲁϣⲓ ⲇⲉ ⲛ̀ϫⲱⲣϩ ⲟⲩϧⲣⲱⲟⲩ ⲁϥϣⲱⲡⲓ: Ϩⲏⲡⲡⲉ ⲓⲥ ⲡⲓⲡⲁⲧϣⲉⲗⲉⲧ ϥⲛⲏⲟⲩ ⲁⲙⲱⲓⲛⲓ ⲉ̀ⲃⲟⲗ ⲉ̀ϩⲣⲁϥ».",
            verseEnglish = "\"And at midnight a cry was heard: 'Behold, the bridegroom is coming; go out to meet him!'\"",
            explanationArabic = "تأمل صلاة نصف الليل (القديس مكاريوس الكبير): السهر في منتصف الليل عبادة الملائكة. طوبى للعبد الذي يجده سيده ساهرًا ومصباحه مضاءً بزيت النعمة والمحبة منتظرًا مجيء الرب المجيد.",
            explanationEnglish = "Midnight Reflection (St. Macarius the Great): Vigilance at the midnight hour mirrors the angelic service. Blessed is the servant found awake with an oiled lamp awaiting the Lord's return.",
            associatedPrayer = PrayerId.MIDNIGHT,
            liturgicalThemeArabic = "صلاة نصف الليل • السهر الروحي ومجيء العريس",
            liturgicalThemeEnglish = "Midnight Prayer • Spiritual Vigil & The Bridegroom"
        )
    )

    /**
     * Retrieves the daily verse meditation for today, based on day of year,
     * ensuring daily renewal and direct connection to canonical Agpeya prayer hours.
     */
    fun getDailyVerseForCalendar(calendar: Calendar = Calendar.getInstance()): DailyVerseMeditation {
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1).rem(verses.size)
        return verses[if (index < 0) 0 else index]
    }

    /**
     * Get verses mapped to specific canonical prayers.
     */
    fun getVerseForPrayer(prayerId: PrayerId): DailyVerseMeditation {
        return verses.find { it.associatedPrayer == prayerId } ?: verses.first()
    }
}
